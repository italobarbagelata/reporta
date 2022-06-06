import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IListSizes } from 'app/shared/model/list-sizes.model';
import { getEntities } from './list-sizes.reducer';

export const ListSizes = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const listSizesList = useAppSelector(state => state.listSizes.entities);
  const loading = useAppSelector(state => state.listSizes.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="list-sizes-heading" data-cy="ListSizesHeading">
        <Translate contentKey="reportappApp.listSizes.home.title">List Sizes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reportappApp.listSizes.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/list-sizes/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reportappApp.listSizes.home.createLabel">Create new List Sizes</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {listSizesList && listSizesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="reportappApp.listSizes.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="reportappApp.listSizes.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {listSizesList.map((listSizes, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/list-sizes/${listSizes.id}`} color="link" size="sm">
                      {listSizes.id}
                    </Button>
                  </td>
                  <td>{listSizes.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/list-sizes/${listSizes.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/list-sizes/${listSizes.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/list-sizes/${listSizes.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="reportappApp.listSizes.home.notFound">No List Sizes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ListSizes;
