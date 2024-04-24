import { IUser } from 'app/shared/model/user.model';

export interface IRecipe {
  id?: number;
  title?: string;
  description?: string | null;
  cuisine?: string | null;
  difficultyLevel?: string | null;
  preparationTime?: number | null;
  cookingTime?: number | null;
  author?: IUser | null;
}

export const defaultValue: Readonly<IRecipe> = {};
