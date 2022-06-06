import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ILabel } from 'app/shared/model/label.model';
import { IExporter } from 'app/shared/model/exporter.model';
import { IPhytoChina } from 'app/shared/model/phyto-china.model';
import { IDispatchs } from 'app/shared/model/dispatchs.model';
import { IPackages } from 'app/shared/model/packages.model';
import { IWeight } from 'app/shared/model/weight.model';
import { ISpecie } from 'app/shared/model/specie.model';
import { IVariety } from 'app/shared/model/variety.model';
import { IColor } from 'app/shared/model/color.model';
import { IFinalOverall } from 'app/shared/model/final-overall.model';
import { IListSizes } from 'app/shared/model/list-sizes.model';

export interface IInspection {
  id?: number;
  reportDate?: string | null;
  description?: string | null;
  place?: string | null;
  inspectionDate?: string | null;
  grower?: string | null;
  packingDate?: string | null;
  observations?: string | null;
  finalRecomendations?: string | null;
  extraDetails?: string | null;
  user?: IUser | null;
  label?: ILabel | null;
  exporter?: IExporter | null;
  phytoChina?: IPhytoChina | null;
  dispatchs?: IDispatchs | null;
  packages?: IPackages | null;
  weight?: IWeight | null;
  specie?: ISpecie | null;
  variety?: IVariety | null;
  color?: IColor | null;
  finalOverall?: IFinalOverall | null;
  listSizes?: IListSizes | null;
}

export const defaultValue: Readonly<IInspection> = {};
