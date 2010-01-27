package com.meum.classifier.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {

    public static void save(final String name, final JComponent component) throws IOException {
        final Dimension size = component.getPreferredSize();
        if (size.getWidth() > 2000 && size.getHeight() > 2000) {
            System.out.println("Can't save image of this size, width: " + size.getWidth() + "; height: " + size.getHeight());
            return;
        }
        final BufferedImage image = new BufferedImage((int) size.getWidth(),
                (int) size.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        final File output = new File("tests/images/" + name + ".jpg");
        ImageIO.write(image, "jpg", output);
    }
}
