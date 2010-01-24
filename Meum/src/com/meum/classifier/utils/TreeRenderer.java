package com.meum.classifier.utils;

import com.meum.classifier.DecisionNode;
import com.meum.classifier.LeafNode;
import com.meum.classifier.TreeNode;
import org.uncommons.watchmaker.examples.geneticprogramming.Constant;
import org.uncommons.watchmaker.examples.geneticprogramming.Node;
import org.uncommons.watchmaker.examples.geneticprogramming.Parameter;
import org.uncommons.watchmaker.framework.interactive.*;

import javax.swing.*;
import java.awt.*;

/**
 * A Swing renderer for decision trees
 *
 * @author Michal Kozakiewicz
 * @author Daniel Dyer
 */
public class TreeRenderer implements org.uncommons.watchmaker.framework.interactive.Renderer<TreeNode, JComponent> {
    /**
     * Renders a GP tree as a Swing component.
     *
     * @param tree The root node of the GP tree to render.
     * @return A {@link JComponent} that displays a graphical representation of the tree.
     */
    public JComponent render(TreeNode tree) {
        return new EATreeView(tree);
    }


    private static final class EATreeView extends JComponent {
        // Allow 50 pixels for each node horizontally.
        private static final int NODE_WIDTH = 70;
        // Allow 50 pixels for each node vertically.
        private static final int NODE_HEIGHT = 50;
        private static final int CIRCLE_RADIUS = 9;
        private static final int CIRCLE_DIAMETER = CIRCLE_RADIUS * 2;

        private final TreeNode rootNode;


        EATreeView(TreeNode rootNode) {
            this.rootNode = rootNode;
            int minHeight = rootNode.getDepth() * NODE_HEIGHT + NODE_HEIGHT;
            int minWidth = rootNode.getWidth() * NODE_WIDTH + NODE_WIDTH;
            Dimension size = new Dimension(minWidth, minHeight);
            setMinimumSize(size);
            setPreferredSize(size);
        }


        @Override
        public void paint(Graphics graphics) {
            super.paintComponent(graphics);
            if (graphics instanceof Graphics2D) {
                ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
            }
            // Center the tree if the component is bigger than required.
            int offset = (getPreferredSize().width - getMinimumSize().width) / 2 + NODE_WIDTH/2;
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0,0,getPreferredSize().width, getPreferredSize().height);
            drawNode(rootNode, offset, NODE_HEIGHT, graphics);
        }


        /**
         * Recursively draw the specified node and its children.
         *
         * @param node     The sub-tree to draw.
         * @param x        The left edge of the area in which this tree is drawn.
         * @param y        The top edge of the area in which this tree is drawn.
         * @param graphics The target for drawing.
         */
        private void drawNode(TreeNode node, int x, int y, Graphics graphics) {
            final int  start = x + node.getWidth() * NODE_WIDTH / 2;
            if (node instanceof LeafNode) {
                LeafNode leaf = (LeafNode) node;
                switch (leaf.getTarget()) {
                    case BUY:
                        graphics.setColor(Color.GREEN);
                        break;
                    case SELL:
                        graphics.setColor(Color.RED);
                        break;
                    case EVIL:
                        graphics.setColor(Color.lightGray);
                        break;
                }
                graphics.fillRoundRect(start - (NODE_WIDTH / 2 - 2),
                        y,
                        NODE_WIDTH - 4,
                        CIRCLE_DIAMETER,
                        CIRCLE_RADIUS,
                        CIRCLE_RADIUS);
                graphics.setColor(Color.BLACK);
                graphics.drawRoundRect(start - (NODE_WIDTH / 2 - 2),
                        y,
                        NODE_WIDTH - 4,
                        CIRCLE_DIAMETER,
                        CIRCLE_RADIUS,
                        CIRCLE_RADIUS);
            }
            graphics.setColor(Color.BLACK);
            FontMetrics metrics = graphics.getFontMetrics();
            int stringWidth = metrics.stringWidth(node.getLabel());
            graphics.drawString(node.getLabel(),
                    (int) Math.round(start - (double) stringWidth / 2),
                    (int) Math.round(y + CIRCLE_RADIUS + (double) metrics.getHeight() / 2 - metrics.getDescent()));

            int xOffset = x;
            for (int i = 0; i < node.getArity(); i++) {
                TreeNode child = node.getChild(i);
                drawNode(child, xOffset, y + NODE_HEIGHT, graphics);
                graphics.drawLine(start,
                        y + CIRCLE_DIAMETER, xOffset + (child.getWidth() * NODE_WIDTH / 2),
                        y + NODE_HEIGHT);
                xOffset += child.getWidth() * NODE_WIDTH;
            }
        }
    }
}

