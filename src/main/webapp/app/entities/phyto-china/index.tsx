import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PhytoChina from './phyto-china';
import PhytoChinaDetail from './phyto-china-detail';
import PhytoChinaUpdate from './phyto-china-update';
import PhytoChinaDeleteDialog from './phyto-china-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PhytoChinaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PhytoChinaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PhytoChinaDetail} />
      <ErrorBoundaryRoute path={match.url} component={PhytoChina} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PhytoChinaDeleteDialog} />
  </>
);

export default Routes;
