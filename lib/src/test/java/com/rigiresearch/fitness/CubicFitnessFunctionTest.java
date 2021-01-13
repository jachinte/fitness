package com.rigiresearch.fitness;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link CubicFitnessFunction}.
 * @author Miguel Jimenez (miguel@uvic.ca)
 * @version $Id$
 * @since 0.1.0
 */
class CubicFitnessFunctionTest {

    /**
     * A small number to compare doubles.
     */
    private static final double EPSILON = 0.000001;

    @Test
    void testNonNormalized() {
        final CubicFitnessFunction function =
            new CubicFitnessFunction(0.0, 10.0, 20.0);
        Assertions.assertTrue(
            function.evaluate(10.0) - 0.0 < CubicFitnessFunctionTest.EPSILON,
            "Should be technically 0"
        );
        Assertions.assertTrue(
            function.evaluate(0.0) > 0.0,
            "Should be greater than 0"
        );
        Assertions.assertTrue(
            function.evaluate(20.0) < 0.0,
            "Should be less than 0"
        );
        Assertions.assertTrue(
            function.evaluate(5.0) > function.evaluate(6.0),
            "Should increase when x tends to 0"
        );
        Assertions.assertTrue(
            function.evaluate(15.0) > function.evaluate(16.0),
            "Should decrease when x tends to b"
        );
    }

    @Test
    void testNormalized() {
        final double c = 10.0;
        final CubicFitnessFunction function =
            new CubicFitnessFunction(0.0, c/3.0, c);
        Assertions.assertTrue(
            0.0 - function.evaluateNormalized(c/3.0) <
                CubicFitnessFunctionTest.EPSILON,
            "Should be technically 0.0"
        );
        Assertions.assertTrue(
            1.0 + function.evaluateNormalized(c) <
                CubicFitnessFunctionTest.EPSILON,
            "Should be technically -1.0"
        );
        Assertions.assertTrue(
            1.0 - function.evaluateNormalized(0.0) <
                CubicFitnessFunctionTest.EPSILON,
            "Should be technically 1"
        );
    }

    @Test
    void testXIsOutsideTheDomain() {
        final double a = 0.0;
        final double b = 18.0;
        final double c = 24.0;
        final CubicFitnessFunction function = new CubicFitnessFunction(a, b, c);
        Assertions.assertEquals(
            Double.NEGATIVE_INFINITY,
            function.evaluate(-1.0),
            "Should be negative infinitive"
        );
        Assertions.assertEquals(
            Double.POSITIVE_INFINITY,
            function.evaluate(c + 1.0),
            "Should be positive infinitive"
        );
        Assertions.assertTrue(
            1.0 + function.evaluateNormalized(-1.0) <
                CubicFitnessFunctionTest.EPSILON,
            "Should be technically -1.0"
        );
    }

}
