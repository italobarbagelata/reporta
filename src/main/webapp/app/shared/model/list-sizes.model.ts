import { IInspection } from 'app/shared/model/inspection.model';

export interface IListSizes {
  id?: number;
  description?: string | null;
  inspections?: IInspection[] | null;
}

export const defaultValue: Readonly<IListSizes> = {};
