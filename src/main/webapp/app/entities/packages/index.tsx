import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Packages from './packages';
import PackagesDetail from './packages-detail';
import PackagesUpdate from './packages-update';
import PackagesDeleteDialog from './packages-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PackagesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PackagesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PackagesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Packages} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PackagesDeleteDialog} />
  </>
);

export default Routes;
