import { IInspection } from 'app/shared/model/inspection.model';

export interface IExporter {
  id?: number;
  description?: string | null;
  inspections?: IInspection[] | null;
}

export const defaultValue: Readonly<IExporter> = {};
