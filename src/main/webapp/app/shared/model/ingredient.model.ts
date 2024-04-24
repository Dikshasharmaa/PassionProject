import { IRecipe } from 'app/shared/model/recipe.model';

export interface IIngredient {
  id?: number;
  name?: string;
  quantity?: string;
  unit?: string;
  recipe?: IRecipe | null;
}

export const defaultValue: Readonly<IIngredient> = {};
