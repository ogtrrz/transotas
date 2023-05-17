import dayjs from 'dayjs';

export interface IIndice {
  id?: number;
  img?: string | null;
  titulo?: string | null;
  url?: string | null;
  autor?: string | null;
  fecha?: string | null;
  ciudad?: string | null;
  estado?: string | null;
  pais?: string | null;
  comentarios?: number | null;
  vistas?: number | null;
  rating?: number | null;
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
}

export const defaultValue: Readonly<IIndice> = {};
