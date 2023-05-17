import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Reportes from './reportes';
import Categorys from './categorys';
import Comentarios from './comentarios';
import Informacion from './informacion';
import IndiceOriginal from './indice-original';
import Indice from './indice';
import Caso from './caso';
import CasoText from './caso-text';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="reportes/*" element={<Reportes />} />
        <Route path="categorys/*" element={<Categorys />} />
        <Route path="comentarios/*" element={<Comentarios />} />
        <Route path="informacion/*" element={<Informacion />} />
        <Route path="indice-original/*" element={<IndiceOriginal />} />
        <Route path="indice/*" element={<Indice />} />
        <Route path="caso/*" element={<Caso />} />
        <Route path="caso-text/*" element={<CasoText />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
