import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Categorys from './categorys';
import CategorysDetail from './categorys-detail';
import CategorysUpdate from './categorys-update';
import CategorysDeleteDialog from './categorys-delete-dialog';

const CategorysRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Categorys />} />
    <Route path="new" element={<CategorysUpdate />} />
    <Route path=":id">
      <Route index element={<CategorysDetail />} />
      <Route path="edit" element={<CategorysUpdate />} />
      <Route path="delete" element={<CategorysDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CategorysRoutes;
