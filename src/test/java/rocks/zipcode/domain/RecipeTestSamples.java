package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RecipeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Recipe getRecipeSample1() {
        return new Recipe()
            .id(1L)
            .title("title1")
            .description("description1")
            .cuisine("cuisine1")
            .difficultyLevel("difficultyLevel1")
            .preparationTime(1)
            .cookingTime(1);
    }

    public static Recipe getRecipeSample2() {
        return new Recipe()
            .id(2L)
            .title("title2")
            .description("description2")
            .cuisine("cuisine2")
            .difficultyLevel("difficultyLevel2")
            .preparationTime(2)
            .cookingTime(2);
    }

    public static Recipe getRecipeRandomSampleGenerator() {
        return new Recipe()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .cuisine(UUID.randomUUID().toString())
            .difficultyLevel(UUID.randomUUID().toString())
            .preparationTime(intCount.incrementAndGet())
            .cookingTime(intCount.incrementAndGet());
    }
}
