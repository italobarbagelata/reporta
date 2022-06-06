import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IInspection } from 'app/shared/model/inspection.model';
import { getEntities } from './inspection.reducer';

export const Inspection = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const inspectionList = useAppSelector(state => state.inspection.entities);
  const loading = useAppSelector(state => state.inspection.loading);
  const totalItems = useAppSelector(state => state.inspection.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="inspection-heading" data-cy="InspectionHeading">
        <Translate contentKey="reportappApp.inspection.home.title">Inspections</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reportappApp.inspection.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/inspection/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reportappApp.inspection.home.createLabel">Create new Inspection</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {inspectionList && inspectionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="reportappApp.inspection.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('reportDate')}>
                  <Translate contentKey="reportappApp.inspection.reportDate">Report Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="reportappApp.inspection.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('place')}>
                  <Translate contentKey="reportappApp.inspection.place">Place</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('inspectionDate')}>
                  <Translate contentKey="reportappApp.inspection.inspectionDate">Inspection Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('grower')}>
                  <Translate contentKey="reportappApp.inspection.grower">Grower</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('packingDate')}>
                  <Translate contentKey="reportappApp.inspection.packingDate">Packing Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('observations')}>
                  <Translate contentKey="reportappApp.inspection.observations">Observations</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('finalRecomendations')}>
                  <Translate contentKey="reportappApp.inspection.finalRecomendations">Final Recomendations</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extraDetails')}>
                  <Translate contentKey="reportappApp.inspection.extraDetails">Extra Details</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.label">Label</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.exporter">Exporter</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.phytoChina">Phyto China</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.dispatchs">Dispatchs</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.packages">Packages</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.weight">Weight</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.specie">Specie</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.variety">Variety</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.color">Color</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.finalOverall">Final Overall</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="reportappApp.inspection.listSizes">List Sizes</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {inspectionList.map((inspection, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/inspection/${inspection.id}`} color="link" size="sm">
                      {inspection.id}
                    </Button>
                  </td>
                  <td>
                    {inspection.reportDate ? <TextFormat type="date" value={inspection.reportDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{inspection.description}</td>
                  <td>{inspection.place}</td>
                  <td>
                    {inspection.inspectionDate ? (
                      <TextFormat type="date" value={inspection.inspectionDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{inspection.grower}</td>
                  <td>
                    {inspection.packingDate ? <TextFormat type="date" value={inspection.packingDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{inspection.observations}</td>
                  <td>{inspection.finalRecomendations}</td>
                  <td>{inspection.extraDetails}</td>
                  <td>{inspection.user ? inspection.user.login : ''}</td>
                  <td>{inspection.label ? <Link to={`/label/${inspection.label.id}`}>{inspection.label.description}</Link> : ''}</td>
                  <td>
                    {inspection.exporter ? <Link to={`/exporter/${inspection.exporter.id}`}>{inspection.exporter.description}</Link> : ''}
                  </td>
                  <td>
                    {inspection.phytoChina ? (
                      <Link to={`/phyto-china/${inspection.phytoChina.id}`}>{inspection.phytoChina.description}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {inspection.dispatchs ? (
                      <Link to={`/dispatchs/${inspection.dispatchs.id}`}>{inspection.dispatchs.description}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {inspection.packages ? <Link to={`/packages/${inspection.packages.id}`}>{inspection.packages.description}</Link> : ''}
                  </td>
                  <td>{inspection.weight ? <Link to={`/weight/${inspection.weight.id}`}>{inspection.weight.description}</Link> : ''}</td>
                  <td>{inspection.specie ? <Link to={`/specie/${inspection.specie.id}`}>{inspection.specie.description}</Link> : ''}</td>
                  <td>
                    {inspection.variety ? <Link to={`/variety/${inspection.variety.id}`}>{inspection.variety.description}</Link> : ''}
                  </td>
                  <td>{inspection.color ? <Link to={`/color/${inspection.color.id}`}>{inspection.color.description}</Link> : ''}</td>
                  <td>
                    {inspection.finalOverall ? (
                      <Link to={`/final-overall/${inspection.finalOverall.id}`}>{inspection.finalOverall.description}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {inspection.listSizes ? (
                      <Link to={`/list-sizes/${inspection.listSizes.id}`}>{inspection.listSizes.description}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/inspection/${inspection.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/inspection/${inspection.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/inspection/${inspection.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="reportappApp.inspection.home.notFound">No Inspections found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={inspectionList && inspectionList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Inspection;
