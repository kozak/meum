package com.meum.classifier.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.FileWriter;
import java.util.List;

public class SummaryWriter {

    public static void write(String name, List<TestResult> testResults) throws Exception {
        System.out.println("Writing summary: " + name);
        VelocityEngine engine = new VelocityEngine();
        engine.init();
        final Template template = engine.getTemplate("templates/results.vm.html");
        VelocityContext context = new VelocityContext();
        context.put("testResults", testResults);
        context.put("name", name);
        FileWriter writer = new FileWriter("tests/summary/"+name+"_summary.html");
        template.merge(context, writer);
        writer.close();
    }
}
