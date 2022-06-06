import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IWeight } from 'app/shared/model/weight.model';
import { getEntities } from './weight.reducer';

export const Weight = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const weightList = useAppSelector(state => state.weight.entities);
  const loading = useAppSelector(state => state.weight.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="weight-heading" data-cy="WeightHeading">
        <Translate contentKey="reportappApp.weight.home.title">Weights</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reportappApp.weight.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/weight/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reportappApp.weight.home.createLabel">Create new Weight</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {weightList && weightList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="reportappApp.weight.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="reportappApp.weight.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {weightList.map((weight, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/weight/${weight.id}`} color="link" size="sm">
                      {weight.id}
                    </Button>
                  </td>
                  <td>{weight.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/weight/${weight.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/weight/${weight.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/weight/${weight.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="reportappApp.weight.home.notFound">No Weights found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Weight;
