import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ListSizes from './list-sizes';
import ListSizesDetail from './list-sizes-detail';
import ListSizesUpdate from './list-sizes-update';
import ListSizesDeleteDialog from './list-sizes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ListSizesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ListSizesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ListSizesDetail} />
      <ErrorBoundaryRoute path={match.url} component={ListSizes} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ListSizesDeleteDialog} />
  </>
);

export default Routes;
