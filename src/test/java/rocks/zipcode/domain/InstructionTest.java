package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.InstructionTestSamples.*;
import static rocks.zipcode.domain.RecipeTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class InstructionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instruction.class);
        Instruction instruction1 = getInstructionSample1();
        Instruction instruction2 = new Instruction();
        assertThat(instruction1).isNotEqualTo(instruction2);

        instruction2.setId(instruction1.getId());
        assertThat(instruction1).isEqualTo(instruction2);

        instruction2 = getInstructionSample2();
        assertThat(instruction1).isNotEqualTo(instruction2);
    }

    @Test
    void recipeTest() throws Exception {
        Instruction instruction = getInstructionRandomSampleGenerator();
        Recipe recipeBack = getRecipeRandomSampleGenerator();

        instruction.setRecipe(recipeBack);
        assertThat(instruction.getRecipe()).isEqualTo(recipeBack);

        instruction.recipe(null);
        assertThat(instruction.getRecipe()).isNull();
    }
}
