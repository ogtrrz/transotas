import { IReportes } from 'app/shared/model/reportes.model';

export interface IComentarios {
  id?: number;
  autor?: string | null;
  comentario?: string | null;
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
  reportes?: IReportes[] | null;
}

export const defaultValue: Readonly<IComentarios> = {};
