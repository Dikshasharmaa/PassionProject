package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.RecipeTestSamples.*;
import static rocks.zipcode.domain.VideoTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class VideoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Video.class);
        Video video1 = getVideoSample1();
        Video video2 = new Video();
        assertThat(video1).isNotEqualTo(video2);

        video2.setId(video1.getId());
        assertThat(video1).isEqualTo(video2);

        video2 = getVideoSample2();
        assertThat(video1).isNotEqualTo(video2);
    }

    @Test
    void recipeTest() throws Exception {
        Video video = getVideoRandomSampleGenerator();
        Recipe recipeBack = getRecipeRandomSampleGenerator();

        video.setRecipe(recipeBack);
        assertThat(video.getRecipe()).isEqualTo(recipeBack);

        video.recipe(null);
        assertThat(video.getRecipe()).isNull();
    }
}
