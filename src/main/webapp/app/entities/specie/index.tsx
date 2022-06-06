import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Specie from './specie';
import SpecieDetail from './specie-detail';
import SpecieUpdate from './specie-update';
import SpecieDeleteDialog from './specie-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SpecieUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SpecieUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SpecieDetail} />
      <ErrorBoundaryRoute path={match.url} component={Specie} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SpecieDeleteDialog} />
  </>
);

export default Routes;
