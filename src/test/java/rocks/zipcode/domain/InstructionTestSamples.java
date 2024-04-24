package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InstructionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Instruction getInstructionSample1() {
        return new Instruction().id(1L).stepNumber(1).description("description1");
    }

    public static Instruction getInstructionSample2() {
        return new Instruction().id(2L).stepNumber(2).description("description2");
    }

    public static Instruction getInstructionRandomSampleGenerator() {
        return new Instruction()
            .id(longCount.incrementAndGet())
            .stepNumber(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
