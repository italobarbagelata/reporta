import { IInspection } from 'app/shared/model/inspection.model';

export interface ISpecie {
  id?: number;
  description?: string | null;
  inspections?: IInspection[] | null;
}

export const defaultValue: Readonly<ISpecie> = {};
