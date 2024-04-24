import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/recipe">
        Recipe
      </MenuItem>
      <MenuItem icon="asterisk" to="/ingredient">
        Ingredient
      </MenuItem>
      <MenuItem icon="asterisk" to="/instruction">
        Instruction
      </MenuItem>
      <MenuItem icon="asterisk" to="/video">
        Video
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
