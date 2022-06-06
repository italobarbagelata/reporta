import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Dispatchs from './dispatchs';
import DispatchsDetail from './dispatchs-detail';
import DispatchsUpdate from './dispatchs-update';
import DispatchsDeleteDialog from './dispatchs-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DispatchsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DispatchsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DispatchsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Dispatchs} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DispatchsDeleteDialog} />
  </>
);

export default Routes;
