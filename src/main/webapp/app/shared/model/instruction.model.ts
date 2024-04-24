import { IRecipe } from 'app/shared/model/recipe.model';

export interface IInstruction {
  id?: number;
  stepNumber?: number;
  description?: string;
  recipe?: IRecipe | null;
}

export const defaultValue: Readonly<IInstruction> = {};
