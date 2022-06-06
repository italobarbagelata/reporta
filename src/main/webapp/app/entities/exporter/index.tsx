import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Exporter from './exporter';
import ExporterDetail from './exporter-detail';
import ExporterUpdate from './exporter-update';
import ExporterDeleteDialog from './exporter-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExporterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExporterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExporterDetail} />
      <ErrorBoundaryRoute path={match.url} component={Exporter} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExporterDeleteDialog} />
  </>
);

export default Routes;
