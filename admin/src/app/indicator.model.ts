import { IndicatorCategory } from './indicator-category.model';
import { Ecogesture } from './ecogesture.model';

export interface Indicator {
  id: number;
  biomId: string;
  slug: string;
  categories: Array<IndicatorCategory>;
  ecogestures: Array<Ecogesture>;
}

export interface IndicatorValue {
  territory: string;
  value: number;
  unit: string;
}

export interface ValuedIndicator {
  shortLabel: string;
  values: Array<IndicatorValue>;
}

export interface IndicatorCommand {
  biomId: string;
  slug: string;
  categoryIds: Array<number>;
  ecogestureIds: Array<number>;
}
