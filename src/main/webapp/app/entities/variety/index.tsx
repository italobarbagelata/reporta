import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Variety from './variety';
import VarietyDetail from './variety-detail';
import VarietyUpdate from './variety-update';
import VarietyDeleteDialog from './variety-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VarietyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VarietyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VarietyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Variety} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VarietyDeleteDialog} />
  </>
);

export default Routes;
