package com.rigiresearch.fitness;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Defines a normalized fitness function to reward minimizing certain variable.
 * @author Miguel Jimenez (miguel@uvic.ca)
 * @version $Id$
 * @since 0.1.0
 */
@RequiredArgsConstructor
public final class NormalizedFitnessFunction
    implements FitnessFunction<NormalizedFitnessFunction.NormalizedFunctionArgument> {

    /**
     * Error message for unimplemented methods.
     */
    private static final String ERROR = "Not implemented on purpose";

    /**
     * The minimum acceptable value.
     */
    private final double min;

    /**
     * The maximum acceptable value.
     */
    private final double max;

    @Override
    public double evaluate(final double... args) {
        this.checkArguments(args);
        return this.evaluateNormalized(args);
    }

    /**
     * Evaluates the function {@code -x+1}, where {@code 0<=x<=max}. f(x) is
     * greater when {@code x} tends to 0, and is smaller when it tends to
     * {@code max}.
     * <p>Visit https://www.wolframalpha.com/input/?i=plot+-x+%2B+1+from+0+to+1
     * to see a plot of this function.</p>
     * @param args The arguments passed to this function
     * @return a double value between 0.0 and 1.0
     */
    @Override
    public double evaluateNormalized(final double... args) {
        this.checkArguments(args);
        return FitnessFunction.normalizeInRange(
            args[0],
            this.max,
            this.min,
            -1.0,
            1.0
        );
    }

    @Override
    public double evaluate(final FitnessFunction.Argument... args) {
        throw new UnsupportedOperationException(
            NormalizedFitnessFunction.ERROR
        );
    }

    @Override
    public double evaluateNormalized(final FitnessFunction.Argument... args) {
        throw new UnsupportedOperationException(
            NormalizedFitnessFunction.ERROR
        );
    }

    /**
     * Checks preconditions on the arguments.
     * @param args The arguments
     */
    private void checkArguments(final double... args) {
        if (args[0] > this.max || args[0] < 0.0) {
            throw new IllegalArgumentException(
                String.format("Value %f is out of bounds", args[0])
            );
        }
    }

    @Override
    public Class<NormalizedFitnessFunction.NormalizedFunctionArgument> argumentType() {
        return NormalizedFitnessFunction.NormalizedFunctionArgument.class;
    }

    /**
     * A valid argument for this function.
     */
    @Accessors(fluent = true)
    @Getter
    public static final class NormalizedFunctionArgument
        implements FitnessFunction.Argument {

        /**
         * Valid argument values.
         */
        private final double[] values;

        /**
         * Default constructor.
         * @param values Valid argument values
         */
        public NormalizedFunctionArgument(final double... values) {
            this.values = values;
        }

    }

}
