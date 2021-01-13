package com.rigiresearch.fitness;

/**
 * A fitness function to either minimize or maximize a value.
 * @param <T> The type of input argument
 *
 * @author Miguel Jimenez (miguel@uvic.ca)
 * @version $Id$
 * @since 0.1.0
 */
public interface FitnessFunction<T extends FitnessFunction.Argument> {

    /**
     * An object-oriented argument contract.
     */
    interface Argument {
        /**
         * Returns this argument's value.
         * @return A double number
         */
        double[] values();
    }

    /**
     * Evaluate this function.
     * @param args The arguments passed to this function
     * @return A positive or negative number, including 0
     */
    double evaluate(double... args);

    /**
     * Evaluate this function normalizing the output.
     * @param args The arguments passed to this function
     * @return A number between 0 and 1
     */
    double evaluateNormalized(double... args);

    /**
     * Evaluate this function.
     * @param args The arguments passed to this function
     * @return A positive or negative number, including 0
     */
    double evaluate(Argument... args);

    /**
     * Evaluate this function normalizing the output.
     * @param args The arguments passed to this function
     * @return A number between 0 and 1
     */
    double evaluateNormalized(Argument... args);

    /**
     * The type of argument accepted by this function.
     * @return A class
     */
    Class<T> argumentType();

    /**
     * Calculates a value between 0 and 1, given the precondition that value
     * is between min and max.
     * @param value The value to scale
     * @param min The minimum value in the domain
     * @param max The maximum value in the domain
     * @return A double between 0.0 and 1.0
     */
    static double normalize(final double value, final double min,
        final double max) {
        return FitnessFunction.normalizeInRange(value, min, max, 0.0, 1.0);
    }

    /**
     * Calculates a value between {@code a} and {@code b}, given the
     * precondition that value is between {@code min} and {@code max}. {@code a}
     * means {@code value = max}, and {@code b} means {@code value = min}.
     *
     * <p>From https://stats.stackexchange.com/a/178629</p>
     *
     * @param value The value to scale
     * @param min The minimum value in the domain
     * @param max The maximum value in the domain
     * @param a The lower bound of the range
     * @param b The upper bound of the range
     * @return A double between {@code a} and {@code b}
     */
    static double normalizeInRange(final double value, final double min,
        final double max, final double a, final double b) {
        return (b - a) * ((value-min)/(max-min)) + a;
    }

}
