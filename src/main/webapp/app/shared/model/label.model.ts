import { IInspection } from 'app/shared/model/inspection.model';

export interface ILabel {
  id?: number;
  description?: string | null;
  inspections?: IInspection[] | null;
}

export const defaultValue: Readonly<ILabel> = {};
