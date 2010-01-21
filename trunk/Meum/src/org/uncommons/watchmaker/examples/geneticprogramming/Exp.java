package org.uncommons.watchmaker.examples.geneticprogramming;

public class Exp extends ScalarNode {

    public Exp(Node child) {
        super(child, "exp");
    }

    public double evaluate(double[] programParameters) {
        return Math.exp(child.evaluate(programParameters));
    }

    public Node simplify() {
        final Node simplified = child.simplify();
        if (simplified instanceof Constant)
        {
            return new Constant(Math.exp(simplified.evaluate(NO_ARGS)));
        }
        return child != simplified ? new Exp(simplified) : this;
    }
}
