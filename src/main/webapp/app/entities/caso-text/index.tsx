import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CasoText from './caso-text';
import CasoTextDetail from './caso-text-detail';
import CasoTextUpdate from './caso-text-update';
import CasoTextDeleteDialog from './caso-text-delete-dialog';

const CasoTextRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CasoText />} />
    <Route path="new" element={<CasoTextUpdate />} />
    <Route path=":id">
      <Route index element={<CasoTextDetail />} />
      <Route path="edit" element={<CasoTextUpdate />} />
      <Route path="delete" element={<CasoTextDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CasoTextRoutes;
