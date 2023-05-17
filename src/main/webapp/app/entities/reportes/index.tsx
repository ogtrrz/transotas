import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Reportes from './reportes';
import ReportesDetail from './reportes-detail';
import ReportesUpdate from './reportes-update';
import ReportesDeleteDialog from './reportes-delete-dialog';

const ReportesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Reportes />} />
    <Route path="new" element={<ReportesUpdate />} />
    <Route path=":id">
      <Route index element={<ReportesDetail />} />
      <Route path="edit" element={<ReportesUpdate />} />
      <Route path="delete" element={<ReportesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReportesRoutes;
