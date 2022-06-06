import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IColor } from 'app/shared/model/color.model';
import { getEntities } from './color.reducer';

export const Color = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const colorList = useAppSelector(state => state.color.entities);
  const loading = useAppSelector(state => state.color.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="color-heading" data-cy="ColorHeading">
        <Translate contentKey="reportappApp.color.home.title">Colors</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reportappApp.color.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/color/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reportappApp.color.home.createLabel">Create new Color</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {colorList && colorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="reportappApp.color.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="reportappApp.color.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {colorList.map((color, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/color/${color.id}`} color="link" size="sm">
                      {color.id}
                    </Button>
                  </td>
                  <td>{color.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/color/${color.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/color/${color.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/color/${color.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="reportappApp.color.home.notFound">No Colors found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Color;
