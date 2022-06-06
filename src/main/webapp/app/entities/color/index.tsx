import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Color from './color';
import ColorDetail from './color-detail';
import ColorUpdate from './color-update';
import ColorDeleteDialog from './color-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ColorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ColorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ColorDetail} />
      <ErrorBoundaryRoute path={match.url} component={Color} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ColorDeleteDialog} />
  </>
);

export default Routes;
