import { IInspection } from 'app/shared/model/inspection.model';

export interface IVariety {
  id?: number;
  description?: string | null;
  inspections?: IInspection[] | null;
}

export const defaultValue: Readonly<IVariety> = {};
