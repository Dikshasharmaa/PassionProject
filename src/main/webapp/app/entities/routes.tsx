import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Recipe from './recipe';
import Ingredient from './ingredient';
import Instruction from './instruction';
import Video from './video';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="recipe/*" element={<Recipe />} />
        <Route path="ingredient/*" element={<Ingredient />} />
        <Route path="instruction/*" element={<Instruction />} />
        <Route path="video/*" element={<Video />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
