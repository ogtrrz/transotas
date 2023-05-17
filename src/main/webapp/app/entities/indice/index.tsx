import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Indice from './indice';
import IndiceDetail from './indice-detail';
import IndiceUpdate from './indice-update';
import IndiceDeleteDialog from './indice-delete-dialog';

const IndiceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Indice />} />
    <Route path="new" element={<IndiceUpdate />} />
    <Route path=":id">
      <Route index element={<IndiceDetail />} />
      <Route path="edit" element={<IndiceUpdate />} />
      <Route path="delete" element={<IndiceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IndiceRoutes;
