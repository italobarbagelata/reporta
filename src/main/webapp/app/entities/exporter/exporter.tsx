import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IExporter } from 'app/shared/model/exporter.model';
import { getEntities } from './exporter.reducer';

export const Exporter = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const exporterList = useAppSelector(state => state.exporter.entities);
  const loading = useAppSelector(state => state.exporter.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="exporter-heading" data-cy="ExporterHeading">
        <Translate contentKey="reportappApp.exporter.home.title">Exporters</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reportappApp.exporter.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/exporter/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reportappApp.exporter.home.createLabel">Create new Exporter</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {exporterList && exporterList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="reportappApp.exporter.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="reportappApp.exporter.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {exporterList.map((exporter, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/exporter/${exporter.id}`} color="link" size="sm">
                      {exporter.id}
                    </Button>
                  </td>
                  <td>{exporter.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/exporter/${exporter.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/exporter/${exporter.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/exporter/${exporter.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="reportappApp.exporter.home.notFound">No Exporters found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Exporter;
