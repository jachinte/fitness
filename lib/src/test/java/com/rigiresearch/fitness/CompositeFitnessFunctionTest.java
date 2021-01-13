package com.rigiresearch.fitness;

import java.util.function.Function;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link CompositeFitnessFunction}.
 * @author Miguel Jimenez (miguel@uvic.ca)
 * @version $Id$
 * @since 0.1.0
 */
@Tag("integration")
class CompositeFitnessFunctionTest {

    /**
     * A small number to compare doubles.
     */
    private static final double EPSILON = 0.000001;

    @Test
    void testWithASingleFunction() {
        final CompositeFitnessFunction function = new CompositeFitnessFunction()
            .withFunction(new CubicFitnessFunction(0.0, 25.0, 50.0), 1.0)
            .validate();
        final Function<Double, FitnessFunction.Argument[]> args = value ->
            new FitnessFunction.Argument[] {
                new CubicFitnessFunction.CubicFunctionArgument(value)
            };
        Assertions.assertTrue(
            1.0 + function.evaluate(args.apply(50.0))
                < CompositeFitnessFunctionTest.EPSILON,
            "Should be technically -1.0"
        );
        Assertions.assertTrue(
            0.0 - function.evaluate(args.apply(25.0))
                < CompositeFitnessFunctionTest.EPSILON,
            "Should be technically 0.0"
        );
        Assertions.assertTrue(
            1.0 - function.evaluate(args.apply(0.0))
                < CompositeFitnessFunctionTest.EPSILON,
            "Should be technically 1.0"
        );
    }

    @Test
    void testWithTwoFunctions() {
        final CompositeFitnessFunction function = new CompositeFitnessFunction()
            .withFunction(new CubicFitnessFunction(0.0, 25.0, 50.0), 0.4)
            .withFunction(new NormalizedFitnessFunction(0.0, 30.0), 0.6)
            .validate();
        final Function<Double[], FitnessFunction.Argument[]> args = values ->
            new FitnessFunction.Argument[] {
                new CubicFitnessFunction.CubicFunctionArgument(values[0]),
                new NormalizedFitnessFunction.NormalizedFunctionArgument(values[1])
            };
        Assertions.assertTrue(
            1.0 + function.evaluate(args.apply(new Double[]{50.0, 30.0}))
                < CompositeFitnessFunctionTest.EPSILON,
            "Should be technically -1.0"
        );
        Assertions.assertTrue(
            1.0 - function.evaluate(args.apply(new Double[]{0.0, 0.0}))
                < CompositeFitnessFunctionTest.EPSILON,
            "Should be technically 1.0"
        );
        Assertions.assertTrue(
            0.0 - function.evaluate(args.apply(new Double[]{25.0, 15.0}))
                < CompositeFitnessFunctionTest.EPSILON,
            "Should be technically 0.0"
        );
    }

    @Test
    void testWithWrongWeights() {
        Assertions.assertThrows(IllegalStateException.class, () ->
            new CompositeFitnessFunction()
                .withFunction(new CubicFitnessFunction(0.0, 12.5, 25.0), 0.9)
                .validate()
        );
        Assertions.assertThrows(IllegalStateException.class, () ->
            new CompositeFitnessFunction()
                .withFunction(new CubicFitnessFunction(0.0, 12.5, 25.0), 0.7)
                .withFunction(new NormalizedFitnessFunction(0.0, 10.0), 0.6)
                .validate()
        );
    }

    @Test
    void testWithDuplicateFunctions() {
        Assertions.assertThrows(IllegalStateException.class, () ->
            new CompositeFitnessFunction()
                .withFunction(new CubicFitnessFunction(0.0, 25.0, 50.0), 0.3)
                .withFunction(new CubicFitnessFunction(0.0, 12.5, 25.0), 0.3)
                .withFunction(new NormalizedFitnessFunction(0.0, 10.0), 0.4)
                .validate()
        );
    }

}
