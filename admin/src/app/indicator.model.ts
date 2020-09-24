import { IndicatorCategory } from './indicator-category.model';

export interface Indicator {
  id: number;
  biomId: string;
  slug: string;
  categories: Array<IndicatorCategory>;
}

export interface IndicatorValue {
  territory: string;
  value: number;
  unit: string;
}

export interface IndicatorCommand {
  biomId: string;
  slug: string;
  categoryIds: Array<number>;
}
