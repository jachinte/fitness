package com.rigiresearch.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Defines a composite fitness function with percentage weights.
 * @author Miguel Jimenez (miguel@uvic.ca)
 * @version $Id$
 * @since 0.1.0
 */
public final class CompositeFitnessFunction
    implements FitnessFunction<CompositeFitnessFunction.CompositeArgument> {

    /**
     * Error message for unimplemented methods.
     */
    private static final String ERROR = "Not implemented on purpose";

    /**
     * Initial capacity based on the expected number of fitness functions.
     */
    private static final int INITIAL_CAPACITY = 5;

    /**
     * A small value to compare double values.
     */
    private static final double EPSILON = 0.000001;

    /**
     * A list of pairs function-weight to compute the overall weight.
     */
    private final List<CompositeFitnessFunction.Pair> pairs;

    /**
     * Default constructor.
     */
    public CompositeFitnessFunction() {
        this.pairs = new ArrayList<>(CompositeFitnessFunction.INITIAL_CAPACITY);
    }

    /**
     * Adds a fitness function to this composite function.
     * @param function The fitness function
     * @param weight The fitness function's weight on the final fitness score
     * @return This
     */
    public CompositeFitnessFunction withFunction(
        final FitnessFunction<?> function, final double weight) {
        this.pairs.add(new CompositeFitnessFunction.Pair(function, weight));
        return this;
    }

    /**
     * Validates that this function has been built correctly. That is, the weights
     * sum 1, and all argument types are handled by only one function.
     * @return This
     */
    public CompositeFitnessFunction validate() {
        double sum = 0.0;
        final Map<Class<?>, Integer> map = new HashMap<>(this.pairs.size());
        for (final CompositeFitnessFunction.Pair pair : this.pairs) {
            sum += pair.weight();
            final Class<?> clazz = pair.function().argumentType();
            map.putIfAbsent(clazz, 0);
            map.put(clazz, map.get(clazz) + 1);
        }
        if (Math.abs(1.0 - sum) > CompositeFitnessFunction.EPSILON) {
            throw new IllegalStateException(
                String.format("The weights must sum 1.0. Current value is %f", sum)
            );
        }
        if (map.values().stream().anyMatch(v -> v >= 2)) {
            throw new IllegalStateException(
                "There are at least two functions handling the same argument type"
            );
        }
        return this;
    }

    @Override
    public double evaluate(final double... args) {
        throw new UnsupportedOperationException(CompositeFitnessFunction.ERROR);
    }

    @Override
    public double evaluateNormalized(final double... args) {
        throw new UnsupportedOperationException(CompositeFitnessFunction.ERROR);
    }

    @Override
    public double evaluate(final FitnessFunction.Argument... args) {
        double result = 0.0;
        for (final FitnessFunction.Argument arg : args) {
            final CompositeFitnessFunction.Pair pair = this.pair(arg.getClass());
            final double value = pair.function().evaluateNormalized(arg.values());
            result += pair.weight() * value;
        }
        return result;
    }

    @Override
    public double evaluateNormalized(final FitnessFunction.Argument... args) {
        // #evaluate(Argument) is already using the normalized results
        return this.evaluate(args);
    }

    @Override
    public Class<CompositeFitnessFunction.CompositeArgument> argumentType() {
        return CompositeFitnessFunction.CompositeArgument.class;
    }

    /**
     * Finds a pair by the type of argument that the function handles.
     * This assumes that only one function handles a particular argument type.
     * @param type The argument type
     * @return A {@link CompositeFitnessFunction.Pair} instance or throws a
     *  runtime exception
     */
    private CompositeFitnessFunction.Pair pair(final Class<?> type) {
        for (final CompositeFitnessFunction.Pair pair : this.pairs) {
            if (pair.function().argumentType().equals(type)) {
                return pair;
            }
        }
        throw new IllegalStateException(
            String.format(
                "No function has been registered to handle arguments of type %s",
                type.getCanonicalName()
            )
        );
    }

    /**
     * A pair function-weight.
     */
    @Accessors(fluent = true)
    @Getter
    @RequiredArgsConstructor
    private static final class Pair {

        /**
         * The fitness function.
         */
        private final FitnessFunction<?> function;

        /**
         * The percentage weight of the fitness score in the overall score.
         */
        private final double weight;

    }

    /**
     * Not used.
     */
    @Accessors(fluent = true)
    @Getter
    @RequiredArgsConstructor
    protected static final class CompositeArgument
        implements FitnessFunction.Argument {

        /**
         * Not used.
         */
        private final double[] values;
    }

}
