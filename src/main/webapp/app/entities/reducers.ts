import recipe from 'app/entities/recipe/recipe.reducer';
import ingredient from 'app/entities/ingredient/ingredient.reducer';
import instruction from 'app/entities/instruction/instruction.reducer';
import video from 'app/entities/video/video.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  recipe,
  ingredient,
  instruction,
  video,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
