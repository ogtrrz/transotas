import dayjs from 'dayjs';
import { IInformacion } from 'app/shared/model/informacion.model';
import { IComentarios } from 'app/shared/model/comentarios.model';
import { ICategorys } from 'app/shared/model/categorys.model';

export interface IReportes {
  id?: number;
  titulo?: string | null;
  caso?: string | null;
  img?: string | null;
  autor?: string | null;
  tituloix?: string | null;
  autorix?: string | null;
  fechaix?: string | null;
  imgix?: string | null;
  ciudad?: string | null;
  estado?: string | null;
  pais?: string | null;
  extra1?: string | null;
  extra2?: string | null;
  extra3?: string | null;
  extra4?: string | null;
  extra5?: string | null;
  extra6?: string | null;
  extra7?: string | null;
  extra8?: string | null;
  extra9?: string | null;
  extra10?: string | null;
  informacion?: IInformacion | null;
  comentarios?: IComentarios[] | null;
  categorys?: ICategorys[] | null;
}

export const defaultValue: Readonly<IReportes> = {};
