import dayjs from 'dayjs';

export interface IIndiceOriginal {
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
}

export const defaultValue: Readonly<IIndiceOriginal> = {};
