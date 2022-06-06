import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/color">
        <Translate contentKey="global.menu.entities.color" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dispatchs">
        <Translate contentKey="global.menu.entities.dispatchs" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/exporter">
        <Translate contentKey="global.menu.entities.exporter" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/final-overall">
        <Translate contentKey="global.menu.entities.finalOverall" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/label">
        <Translate contentKey="global.menu.entities.label" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/list-sizes">
        <Translate contentKey="global.menu.entities.listSizes" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/packages">
        <Translate contentKey="global.menu.entities.packages" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/phyto-china">
        <Translate contentKey="global.menu.entities.phytoChina" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/specie">
        <Translate contentKey="global.menu.entities.specie" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/variety">
        <Translate contentKey="global.menu.entities.variety" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/weight">
        <Translate contentKey="global.menu.entities.weight" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/inspection">
        <Translate contentKey="global.menu.entities.inspection" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
