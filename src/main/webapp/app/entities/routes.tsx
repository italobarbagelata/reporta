import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Color from './color';
import Dispatchs from './dispatchs';
import Exporter from './exporter';
import FinalOverall from './final-overall';
import Label from './label';
import ListSizes from './list-sizes';
import Packages from './packages';
import PhytoChina from './phyto-china';
import Specie from './specie';
import Variety from './variety';
import Weight from './weight';
import Inspection from './inspection';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}color`} component={Color} />
        <ErrorBoundaryRoute path={`${match.url}dispatchs`} component={Dispatchs} />
        <ErrorBoundaryRoute path={`${match.url}exporter`} component={Exporter} />
        <ErrorBoundaryRoute path={`${match.url}final-overall`} component={FinalOverall} />
        <ErrorBoundaryRoute path={`${match.url}label`} component={Label} />
        <ErrorBoundaryRoute path={`${match.url}list-sizes`} component={ListSizes} />
        <ErrorBoundaryRoute path={`${match.url}packages`} component={Packages} />
        <ErrorBoundaryRoute path={`${match.url}phyto-china`} component={PhytoChina} />
        <ErrorBoundaryRoute path={`${match.url}specie`} component={Specie} />
        <ErrorBoundaryRoute path={`${match.url}variety`} component={Variety} />
        <ErrorBoundaryRoute path={`${match.url}weight`} component={Weight} />
        <ErrorBoundaryRoute path={`${match.url}inspection`} component={Inspection} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
