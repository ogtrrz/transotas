import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Informacion from './informacion';
import InformacionDetail from './informacion-detail';
import InformacionUpdate from './informacion-update';
import InformacionDeleteDialog from './informacion-delete-dialog';

const InformacionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Informacion />} />
    <Route path="new" element={<InformacionUpdate />} />
    <Route path=":id">
      <Route index element={<InformacionDetail />} />
      <Route path="edit" element={<InformacionUpdate />} />
      <Route path="delete" element={<InformacionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InformacionRoutes;
