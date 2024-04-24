import { IRecipe } from 'app/shared/model/recipe.model';

export interface IVideo {
  id?: number;
  url?: string;
  recipe?: IRecipe | null;
}

export const defaultValue: Readonly<IVideo> = {};
