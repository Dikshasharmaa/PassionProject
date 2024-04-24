package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.IngredientTestSamples.*;
import static rocks.zipcode.domain.RecipeTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class IngredientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ingredient.class);
        Ingredient ingredient1 = getIngredientSample1();
        Ingredient ingredient2 = new Ingredient();
        assertThat(ingredient1).isNotEqualTo(ingredient2);

        ingredient2.setId(ingredient1.getId());
        assertThat(ingredient1).isEqualTo(ingredient2);

        ingredient2 = getIngredientSample2();
        assertThat(ingredient1).isNotEqualTo(ingredient2);
    }

    @Test
    void recipeTest() throws Exception {
        Ingredient ingredient = getIngredientRandomSampleGenerator();
        Recipe recipeBack = getRecipeRandomSampleGenerator();

        ingredient.setRecipe(recipeBack);
        assertThat(ingredient.getRecipe()).isEqualTo(recipeBack);

        ingredient.recipe(null);
        assertThat(ingredient.getRecipe()).isNull();
    }
}
