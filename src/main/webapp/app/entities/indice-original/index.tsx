import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IndiceOriginal from './indice-original';
import IndiceOriginalDetail from './indice-original-detail';
import IndiceOriginalUpdate from './indice-original-update';
import IndiceOriginalDeleteDialog from './indice-original-delete-dialog';

const IndiceOriginalRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IndiceOriginal />} />
    <Route path="new" element={<IndiceOriginalUpdate />} />
    <Route path=":id">
      <Route index element={<IndiceOriginalDetail />} />
      <Route path="edit" element={<IndiceOriginalUpdate />} />
      <Route path="delete" element={<IndiceOriginalDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IndiceOriginalRoutes;
