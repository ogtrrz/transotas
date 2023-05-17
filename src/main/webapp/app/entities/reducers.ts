import reportes from 'app/entities/reportes/reportes.reducer';
import categorys from 'app/entities/categorys/categorys.reducer';
import comentarios from 'app/entities/comentarios/comentarios.reducer';
import informacion from 'app/entities/informacion/informacion.reducer';
import indiceOriginal from 'app/entities/indice-original/indice-original.reducer';
import indice from 'app/entities/indice/indice.reducer';
import caso from 'app/entities/caso/caso.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  reportes,
  categorys,
  comentarios,
  informacion,
  indiceOriginal,
  indice,
  caso,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
