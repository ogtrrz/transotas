import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/reportes">
        <Translate contentKey="global.menu.entities.reportes" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/categorys">
        <Translate contentKey="global.menu.entities.categorys" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/comentarios">
        <Translate contentKey="global.menu.entities.comentarios" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/informacion">
        <Translate contentKey="global.menu.entities.informacion" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/indice-original">
        <Translate contentKey="global.menu.entities.indiceOriginal" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/indice">
        <Translate contentKey="global.menu.entities.indice" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/caso">
        <Translate contentKey="global.menu.entities.caso" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/caso-text">
        <Translate contentKey="global.menu.entities.casoText" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
