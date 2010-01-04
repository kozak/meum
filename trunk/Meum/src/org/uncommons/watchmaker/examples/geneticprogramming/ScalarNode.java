package org.uncommons.watchmaker.examples.geneticprogramming;

import org.uncommons.maths.random.Probability;
import org.uncommons.util.reflection.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.Random;

public abstract class ScalarNode implements Node {

    protected Node child;
    protected String label;

    protected ScalarNode(Node child, String label) {
        this.child = child;
        this.label = label;
    }

    /**
     * {@inheritDoc}
     */
    public String getLabel() {
        return label;
    }


    /**
     * The arity of a scalar node is two.
     *
     * @return 1
     */
    public int getArity() {
        return 1;
    }


    /**
     * The depth of a binary node is the depth of its deepest sub-tree plus one.
     *
     * @return The depth of the tree rooted at this node.
     */
    public int getDepth() {
        return 1 + child.getDepth();
    }


    /**
     * The width of a binary node is the sum of the widths of its two sub-trees.
     *
     * @return The width of the tree rooted at this node.
     */
    public int getWidth() {
        return child.getWidth();
    }


    /**
     * {@inheritDoc}
     */
    public int countNodes() {
        return 1 + child.countNodes();
    }


    /**
     * {@inheritDoc}
     */
    public Node getNode(int index) {
        if (index == 0) {
            return this;
        }
        return child.getNode(index - 1);
    }


    /**
     * {@inheritDoc}
     */
    public Node getChild(int index) {
        switch (index) {
            case 0:
                return child;
            default:
                throw new IndexOutOfBoundsException("Invalid child index: " + index);
        }
    }


    /**
     * {@inheritDoc}
     */
    public Node replaceNode(int index, Node newNode) {
        if (index == 0) {
            return newNode;
        }
        return newInstance(child.replaceNode(index - 1, newNode));
    }


    /**
     * {@inheritDoc}
     */
    public String print() {
        StringBuilder buffer = new StringBuilder(label);
        buffer.append("( ");
        buffer.append(child.print());
        buffer.append(" )");
        return buffer.toString();
    }


    /**
     * {@inheritDoc}
     */
    public Node mutate(Random rng, Probability mutationProbability, TreeFactory treeFactory) {
        if (mutationProbability.nextEvent(rng)) {
            return treeFactory.generateRandomCandidate(rng);
        } else {
            Node newChild = child.mutate(rng, mutationProbability, treeFactory);
            if (newChild != child) {
                return newInstance(newChild);
            } else {
                // Tree has not changed.
                return this;
            }
        }
    }


    private Node newInstance(Node newNode) {
        Constructor<? extends ScalarNode> constructor = ReflectionUtils.findKnownConstructor(this.getClass(),
                Node.class);
        return ReflectionUtils.invokeUnchecked(constructor, newNode);
    }


    @Override
    public String toString() {
        return print();
    }
}
