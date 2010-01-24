package com.meum.classifier.test;

import com.meum.classifier.DecisionNode;
import com.meum.classifier.LeafNode;
import com.meum.classifier.Target;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class SimplificationTest {
    @Test
    public void testDecisionNodeWithSameLeafs() {
        DecisionNode dnode = new DecisionNode(new LeafNode(Target.BUY), new LeafNode(Target.BUY), null);
        assertTrue(dnode.simplify() instanceof LeafNode, "Leaf nodes not simplified");
    }

    @Test
    public void testDecisionNodeWithSameLeafsNested1() {

        DecisionNode d2 = new DecisionNode(new LeafNode(Target.BUY), new LeafNode(Target.BUY), null);
        DecisionNode d1 = new DecisionNode(d2, new LeafNode(Target.SELL), null);
        assertTrue(d1.simplify().getChild(0) instanceof LeafNode, "Leaf nodes not simplified");
    }
}
