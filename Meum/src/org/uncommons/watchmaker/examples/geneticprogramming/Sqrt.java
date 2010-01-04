package org.uncommons.watchmaker.examples.geneticprogramming;

public class Sqrt extends ScalarNode {

    public Sqrt(Node child) {
        super(child, "sqrt");
    }

    public double evaluate(double[] programParameters) {
        return Math.sqrt(child.evaluate(programParameters));
    }

    public Node simplify() {
        final Node simplified = child.simplify();

        if (simplified instanceof Constant)
        {
            return new Constant(Math.sqrt(simplified.evaluate(NO_ARGS)));
        }
        if (simplified instanceof Sqr)
        {
            return ((Sqr) simplified).child.simplify();
        }
        return child != simplified ? new Sqrt(simplified) : this;
    }
}
