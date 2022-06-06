import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FinalOverall from './final-overall';
import FinalOverallDetail from './final-overall-detail';
import FinalOverallUpdate from './final-overall-update';
import FinalOverallDeleteDialog from './final-overall-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FinalOverallUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FinalOverallUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FinalOverallDetail} />
      <ErrorBoundaryRoute path={match.url} component={FinalOverall} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FinalOverallDeleteDialog} />
  </>
);

export default Routes;
