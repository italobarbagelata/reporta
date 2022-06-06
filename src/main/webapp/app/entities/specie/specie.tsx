import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISpecie } from 'app/shared/model/specie.model';
import { getEntities } from './specie.reducer';

export const Specie = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const specieList = useAppSelector(state => state.specie.entities);
  const loading = useAppSelector(state => state.specie.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="specie-heading" data-cy="SpecieHeading">
        <Translate contentKey="reportappApp.specie.home.title">Species</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reportappApp.specie.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/specie/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reportappApp.specie.home.createLabel">Create new Specie</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {specieList && specieList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="reportappApp.specie.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="reportappApp.specie.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {specieList.map((specie, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/specie/${specie.id}`} color="link" size="sm">
                      {specie.id}
                    </Button>
                  </td>
                  <td>{specie.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/specie/${specie.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/specie/${specie.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/specie/${specie.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="reportappApp.specie.home.notFound">No Species found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Specie;
