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
        final BufferedImage image = new BufferedImage((int)size.getWidth(),
                (int)size.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        ImageIO.write(image, "jpg", new File(name+".jpg"));
    }
}
