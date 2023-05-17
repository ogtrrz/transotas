import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Comentarios from './comentarios';
import ComentariosDetail from './comentarios-detail';
import ComentariosUpdate from './comentarios-update';
import ComentariosDeleteDialog from './comentarios-delete-dialog';

const ComentariosRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Comentarios />} />
    <Route path="new" element={<ComentariosUpdate />} />
    <Route path=":id">
      <Route index element={<ComentariosDetail />} />
      <Route path="edit" element={<ComentariosUpdate />} />
      <Route path="delete" element={<ComentariosDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ComentariosRoutes;
