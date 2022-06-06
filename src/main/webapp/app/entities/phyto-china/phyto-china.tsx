import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPhytoChina } from 'app/shared/model/phyto-china.model';
import { getEntities } from './phyto-china.reducer';

export const PhytoChina = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const phytoChinaList = useAppSelector(state => state.phytoChina.entities);
  const loading = useAppSelector(state => state.phytoChina.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="phyto-china-heading" data-cy="PhytoChinaHeading">
        <Translate contentKey="reportappApp.phytoChina.home.title">Phyto Chinas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reportappApp.phytoChina.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/phyto-china/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reportappApp.phytoChina.home.createLabel">Create new Phyto China</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {phytoChinaList && phytoChinaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="reportappApp.phytoChina.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="reportappApp.phytoChina.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {phytoChinaList.map((phytoChina, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/phyto-china/${phytoChina.id}`} color="link" size="sm">
                      {phytoChina.id}
                    </Button>
                  </td>
                  <td>{phytoChina.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/phyto-china/${phytoChina.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/phyto-china/${phytoChina.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/phyto-china/${phytoChina.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="reportappApp.phytoChina.home.notFound">No Phyto Chinas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PhytoChina;
