import { IInspection } from 'app/shared/model/inspection.model';

export interface IFinalOverall {
  id?: number;
  note?: number | null;
  description?: string | null;
  inspections?: IInspection[] | null;
}

export const defaultValue: Readonly<IFinalOverall> = {};
