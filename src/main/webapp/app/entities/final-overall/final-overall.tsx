import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFinalOverall } from 'app/shared/model/final-overall.model';
import { getEntities } from './final-overall.reducer';

export const FinalOverall = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const finalOverallList = useAppSelector(state => state.finalOverall.entities);
  const loading = useAppSelector(state => state.finalOverall.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="final-overall-heading" data-cy="FinalOverallHeading">
        <Translate contentKey="reportappApp.finalOverall.home.title">Final Overalls</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reportappApp.finalOverall.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/final-overall/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reportappApp.finalOverall.home.createLabel">Create new Final Overall</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {finalOverallList && finalOverallList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="reportappApp.finalOverall.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="reportappApp.finalOverall.note">Note</Translate>
                </th>
                <th>
                  <Translate contentKey="reportappApp.finalOverall.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {finalOverallList.map((finalOverall, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/final-overall/${finalOverall.id}`} color="link" size="sm">
                      {finalOverall.id}
                    </Button>
                  </td>
                  <td>{finalOverall.note}</td>
                  <td>{finalOverall.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/final-overall/${finalOverall.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/final-overall/${finalOverall.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/final-overall/${finalOverall.id}/delete`}
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
              <Translate contentKey="reportappApp.finalOverall.home.notFound">No Final Overalls found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FinalOverall;
