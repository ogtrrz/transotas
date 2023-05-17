import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Caso from './caso';
import CasoDetail from './caso-detail';
import CasoUpdate from './caso-update';
import CasoDeleteDialog from './caso-delete-dialog';

const CasoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Caso />} />
    <Route path="new" element={<CasoUpdate />} />
    <Route path=":id">
      <Route index element={<CasoDetail />} />
      <Route path="edit" element={<CasoUpdate />} />
      <Route path="delete" element={<CasoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CasoRoutes;
