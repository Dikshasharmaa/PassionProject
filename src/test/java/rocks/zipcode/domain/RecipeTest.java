package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.IngredientTestSamples.*;
import static rocks.zipcode.domain.InstructionTestSamples.*;
import static rocks.zipcode.domain.RecipeTestSamples.*;
import static rocks.zipcode.domain.VideoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class RecipeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recipe.class);
        Recipe recipe1 = getRecipeSample1();
        Recipe recipe2 = new Recipe();
        assertThat(recipe1).isNotEqualTo(recipe2);

        recipe2.setId(recipe1.getId());
        assertThat(recipe1).isEqualTo(recipe2);

        recipe2 = getRecipeSample2();
        assertThat(recipe1).isNotEqualTo(recipe2);
    }

    @Test
    void ingredientTest() throws Exception {
        Recipe recipe = getRecipeRandomSampleGenerator();
        Ingredient ingredientBack = getIngredientRandomSampleGenerator();

        recipe.addIngredient(ingredientBack);
        assertThat(recipe.getIngredients()).containsOnly(ingredientBack);
        assertThat(ingredientBack.getRecipe()).isEqualTo(recipe);

        recipe.removeIngredient(ingredientBack);
        assertThat(recipe.getIngredients()).doesNotContain(ingredientBack);
        assertThat(ingredientBack.getRecipe()).isNull();

        recipe.ingredients(new HashSet<>(Set.of(ingredientBack)));
        assertThat(recipe.getIngredients()).containsOnly(ingredientBack);
        assertThat(ingredientBack.getRecipe()).isEqualTo(recipe);

        recipe.setIngredients(new HashSet<>());
        assertThat(recipe.getIngredients()).doesNotContain(ingredientBack);
        assertThat(ingredientBack.getRecipe()).isNull();
    }

    @Test
    void instructionTest() throws Exception {
        Recipe recipe = getRecipeRandomSampleGenerator();
        Instruction instructionBack = getInstructionRandomSampleGenerator();

        recipe.addInstruction(instructionBack);
        assertThat(recipe.getInstructions()).containsOnly(instructionBack);
        assertThat(instructionBack.getRecipe()).isEqualTo(recipe);

        recipe.removeInstruction(instructionBack);
        assertThat(recipe.getInstructions()).doesNotContain(instructionBack);
        assertThat(instructionBack.getRecipe()).isNull();

        recipe.instructions(new HashSet<>(Set.of(instructionBack)));
        assertThat(recipe.getInstructions()).containsOnly(instructionBack);
        assertThat(instructionBack.getRecipe()).isEqualTo(recipe);

        recipe.setInstructions(new HashSet<>());
        assertThat(recipe.getInstructions()).doesNotContain(instructionBack);
        assertThat(instructionBack.getRecipe()).isNull();
    }

    @Test
    void videoTest() throws Exception {
        Recipe recipe = getRecipeRandomSampleGenerator();
        Video videoBack = getVideoRandomSampleGenerator();

        recipe.addVideo(videoBack);
        assertThat(recipe.getVideos()).containsOnly(videoBack);
        assertThat(videoBack.getRecipe()).isEqualTo(recipe);

        recipe.removeVideo(videoBack);
        assertThat(recipe.getVideos()).doesNotContain(videoBack);
        assertThat(videoBack.getRecipe()).isNull();

        recipe.videos(new HashSet<>(Set.of(videoBack)));
        assertThat(recipe.getVideos()).containsOnly(videoBack);
        assertThat(videoBack.getRecipe()).isEqualTo(recipe);

        recipe.setVideos(new HashSet<>());
        assertThat(recipe.getVideos()).doesNotContain(videoBack);
        assertThat(videoBack.getRecipe()).isNull();
    }
}
