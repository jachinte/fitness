package com.rigiresearch.fitness;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Defines a cubic fitness function to reward minimizing certain variable.
 * @author Miguel Jimenez (miguel@uvic.ca)
 * @version $Id$
 * @since 0.1.0
 */
public final class CubicFitnessFunction
    implements FitnessFunction<CubicFitnessFunction.CubicFunctionArgument> {

    /**
     * Error message for unimplemented methods.
     */
    private static final String ERROR = "Not implemented on purpose";

    /**
     * The lower bound in the x axis.
     */
    private final BigDecimal a;

    /**
     * A value between a and c;
     */
    private final BigDecimal b;

    /**
     * The upper bound in the x axis.
     */
    private final BigDecimal c;

    /**
     * Default constructor.
     * @param a The lower bound in the x axis.
     * @param b A value between a and c;
     * @param c The upper bound in the x axis.
     */
    public CubicFitnessFunction(final double a, final double b,
        final double c) {
        this.a = new BigDecimal(a);
        this.b = new BigDecimal(b);
        this.c = new BigDecimal(c);
    }

    /**
     * Evaluates the function {@code -10(x-b)^3}, where {@code a<=x<=c} and
     * {@code a<=b<=c}. This function translates {@code -X^3} to the right and
     * stretches it so that {@code f(x)} is a positive number and is greater
     * when {@code x} tends to {@code a}, and is a negative number and smaller
     * when it tends to {@code b}.
     *
     * <p>When {@code x<a} or {@code x>c}, this function returns negative or
     * positive infinity, respectively.</p>
     *
     * <p>Visit https://www.wolframalpha.com/input/?i=plot+-10%28x-18%29%5E3+from+0+to+36
     * to see a plot of this function.</p>
     *
     * @param args One value on the x axis to evaluate the function
     * @return A negative or positive number, or 0 when x = b
     */
    @Override
    public double evaluate(final double... args) {
        final BigDecimal x = BigDecimal.valueOf(args[0]);
        final double y;
        if (CubicFitnessFunction.lessThanOrEqual(this.a, x) &&
            CubicFitnessFunction.lessThanOrEqual(x, this.c)) {
            y = -10.0 * StrictMath.pow(x.subtract(this.b).doubleValue(), 3.0);
        } else if (x.compareTo(this.a) < 0) {
            y = Double.NEGATIVE_INFINITY;
        } else {
            y = Double.POSITIVE_INFINITY;
        }
        return y;
    }

    /**
     * Same as {@link #evaluate(double[])} but normalized. Since {@code b}
     * represents no change with respect to the initial plan (i.e., {@code x<b}
     * is improvement and {@code x>b} is decline), {@code normalized(f(x))=0}
     * when {@code x=b}. Therefore, this function is defined by parts:
     * <pre>
     *     f(x) = {
     *       normalized(f(x)) between (0, 1], when a<=x<=b
     *       0, when x=b
     *       normalized(f(x)) between [-1, 0), when b<=x<=c
     *     }
     * </pre>
     * <p>When {@code x<a} or {@code x>b}, this function returns -1.</p>
     * @param args One value on the x axis to evaluate the function
     * @return A number between -1 and 1
     */
    @Override
    public double evaluateNormalized(final double... args) {
        final BigDecimal x = BigDecimal.valueOf(args[0]);
        final double y = this.evaluate(args[0]);
        final double normalized;
        if (CubicFitnessFunction.lessThanOrEqual(this.a, x) &&
            CubicFitnessFunction.lessThanOrEqual(x, this.b)) {
            final double min = this.evaluate(this.a.doubleValue());
            final double max = this.evaluate(this.b.doubleValue());
            // Switch min and max so that when x=a, y=1 and when x=b, y=0
            normalized = FitnessFunction.normalizeInRange(y, max, min, 0.0, 1.0);
        } else if (CubicFitnessFunction.lessThanOrEqual(this.b, x) &&
            CubicFitnessFunction.lessThanOrEqual(x, this.c)) {
            final double min = this.evaluate(this.b.doubleValue());
            final double max = this.evaluate(this.c.doubleValue());
            // Switch min and max so that when x=b, y=0 and when x=c, y=-1
            normalized = FitnessFunction.normalizeInRange(y, max, min, -1.0, 0.0);
        } else {
            normalized = -1.0;
        }
        return normalized;
    }

    @Override
    public double evaluate(final FitnessFunction.Argument... args) {
        throw new UnsupportedOperationException(CubicFitnessFunction.ERROR);
    }

    @Override
    public double evaluateNormalized(final FitnessFunction.Argument... args) {
        throw new UnsupportedOperationException(CubicFitnessFunction.ERROR);
    }

    @Override
    public Class<CubicFitnessFunction.CubicFunctionArgument> argumentType() {
        return CubicFitnessFunction.CubicFunctionArgument.class;
    }

    /**
     * The less-than-or-equal relational operator.
     * @param first The first argument of the relational operator
     * @param second The second argument of the relational operator
     * @return first <= second
     */
    private static boolean lessThanOrEqual(final BigDecimal first,
        final BigDecimal second) {
        final int result = first.compareTo(second);
        return result < 0 || result == 0;
    }

    /**
     * A valid argument for this function.
     */
    @Accessors(fluent = true)
    @Getter
    public static final class CubicFunctionArgument
        implements FitnessFunction.Argument {

        /**
         * Valid argument values.
         */
        private final double[] values;

        /**
         * Default constructor.
         * @param values Valid argument values
         */
        public CubicFunctionArgument(final double... values) {
            this.values = values;
        }

    }

}
