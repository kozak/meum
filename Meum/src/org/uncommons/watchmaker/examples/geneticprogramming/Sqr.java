package org.uncommons.watchmaker.examples.geneticprogramming;

public class Sqr extends ScalarNode {

    public Sqr(Node child) {
        super(child, "^2");
    }

    @Override
    public String print() {
        StringBuilder buffer = new StringBuilder("( ");
        buffer.append(child.print());
        buffer.append(" )");
        buffer.append(label);
        return buffer.toString();
    }

    public double evaluate(double[] programParameters) {
        final double v = child.evaluate(programParameters);
        return v * v;
    }

    public Node simplify() {
        final Node simplified = child.simplify();

        if (simplified instanceof Constant)
        {
            final double v = simplified.evaluate(NO_ARGS);
            return new Constant(v * v);
        }
        if (simplified instanceof Sqrt)
        {
            return ((Sqrt) simplified).child.simplify();
        }
        return child != simplified ? new Sqr(simplified) : this;
    }

}
