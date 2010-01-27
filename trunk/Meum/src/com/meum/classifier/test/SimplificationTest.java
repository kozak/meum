package com.meum.classifier.test;

import com.meum.classifier.DecisionNode;
import com.meum.classifier.LeafNode;
import com.meum.classifier.Target;
import com.meum.classifier.TreeNode;
import com.meum.classifier.decision.Decision;
import com.meum.classifier.decision.Threshold;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.assertTrue;

public class SimplificationTest {
    @Test
    public void testDecisionNodeWithSameLeafs() {
        DecisionNode dnode = new DecisionNode(new LeafNode(Target.BUY), new LeafNode(Target.BUY), null);
        assertTrue(dnode.simplify(Collections.<Decision, Integer>emptyMap()) instanceof LeafNode, "Leaf nodes not simplified");
    }

    @Test
    public void testDecisionNodeWithSameLeafsNested1() {
        DecisionNode d2 = new DecisionNode(new LeafNode(Target.BUY), new LeafNode(Target.BUY), null);
        DecisionNode d1 = new DecisionNode(d2, new LeafNode(Target.SELL), null);
        assertTrue(d1.simplify(Collections.<Decision, Integer>emptyMap()).getChild(0) instanceof LeafNode, "Leaf nodes not simplified");
    }

    @Test
    public void testDecisionNodeSameArgument() {
        DecisionNode d2 = new DecisionNode(new LeafNode(Target.BUY), new LeafNode(Target.BUY), null);
        DecisionNode d1 = new DecisionNode(d2, new LeafNode(Target.SELL), null);
        assertTrue(d1.simplify(Collections.<Decision, Integer>emptyMap()).getChild(0) instanceof LeafNode, "Leaf nodes not simplified");
    }

    @Test
    public void testSameParameters() {
        DecisionNode d2 = new DecisionNode(new LeafNode(Target.BUY), new LeafNode(Target.SELL), new Threshold(0, 1.0));
        DecisionNode d1 = new DecisionNode(d2, new LeafNode(Target.SELL), new Threshold(0, 1.0));
        final TreeNode child = d1.simplify(Collections.<Decision, Integer>emptyMap()).getChild(0);
        assertTrue(child instanceof LeafNode, "Leaf nodes not simplified");
        assertTrue(((LeafNode) child).getTarget() == Target.BUY, "Leaf node not simplified to the correct target");
    }

    @Test
    public void testSameParametersSimplyToLeaf() {
        DecisionNode d2 = new DecisionNode(new LeafNode(Target.SELL), new LeafNode(Target.BUY), new Threshold(0, 1.0));
        DecisionNode d1 = new DecisionNode(d2, new LeafNode(Target.SELL), new Threshold(0, 1.0));
        final TreeNode child = d1.simplify(Collections.<Decision, Integer>emptyMap());
        assertTrue(child instanceof LeafNode, "Leaf nodes not simplified");
        assertTrue(((LeafNode) child).getTarget() == Target.SELL, "Leaf node not simplified to the correct target");
    }
}
