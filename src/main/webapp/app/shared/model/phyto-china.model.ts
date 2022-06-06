import { IInspection } from 'app/shared/model/inspection.model';

export interface IPhytoChina {
  id?: number;
  description?: string | null;
  inspections?: IInspection[] | null;
}

export const defaultValue: Readonly<IPhytoChina> = {};
