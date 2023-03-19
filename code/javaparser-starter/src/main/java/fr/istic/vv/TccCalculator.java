package fr.istic.vv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/*
 * This class is still incomplete.
 * The tcc calculated is not correct, but we couldn't finish it in time.
 */
public class TccCalculator {
    private String projectPath;
    private Map<String, List<FieldDeclaration>> classFields = new HashMap<>();
    private Map<String, List<MethodDeclaration>> classMethods = new HashMap<>();
    private Map<String, Double> classTcc = new HashMap<>();

    public TccCalculator(String projectPath) {
        this.projectPath = projectPath;
    }

    public void computeTcc() throws IOException {
        File projectDir = new File(projectPath);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid project directory: " + projectPath);
        }

        // Create a new instance of JavaParser
        JavaParser javaParser = new JavaParser();

        // Parse each Java file in the project
        Files.walk(projectDir.toPath())
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        System.out.println("Parsing file " + path.getFileName());
                        CompilationUnit cu = javaParser.parse(path.toFile()).getResult().get();
                        new ClassVisitor().visit(cu, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        // Compute the TCC for each class
        for (String className : classMethods.keySet()) {
            List<MethodDeclaration> methods = classMethods.get(className);
            List<FieldDeclaration> fields = classFields.get(className);
            int a = countFieldAccesses(methods, fields);
            int m = methods.size();
            int n = fields.size();
            double tcc = (2.0 * a) / ((m * (m - 1)) + (2.0 * n * (n - 1)));
            System.out.println("*************** " + className + " ***************");
            //System.out.println(methods.toString());
            //System.out.println(fields.toString());
            //System.out.println(className + " : a = " + a + ", m = " + m + ", n = " + n + ", tcc = " + tcc);
            classTcc.put(className, tcc);
        }

    }

    private int countFieldAccesses(List<MethodDeclaration> methods, List<FieldDeclaration> fields) {
        int count = 0;
        for (MethodDeclaration m1 : methods) {
            for (MethodDeclaration m2 : methods) {
                System.out.println("countFieldAccesse: m1 = " + m1.getName() + " m2 = " + m2.getName());
                if (m1 != m2) {
                    List<FieldAccessExpr> accesses1 = new ArrayList<>();
                    new FieldAccessVisitor().visit(m1.getBody().get(), accesses1);
                    List<FieldAccessExpr> accesses2 = new ArrayList<>();
                    new FieldAccessVisitor().visit(m2.getBody().get(), accesses2);
                    for (FieldDeclaration f : fields) {
                        if (isFieldAccessed(accesses1, f) && isFieldAccessed(accesses2, f)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public void generateReport (String reportPath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("| Package | Class | TCC |\n");
        sb.append("| ------- | ----- | --- |\n");
        for (String className : classTcc.keySet()) {
            String packageName = className.substring(0, className.lastIndexOf('.'));
            String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
            double tcc = classTcc.get(className);
            sb.append(String.format("| %s | %s | %.2f |\n", packageName, simpleClassName, tcc));
        }
        Files.writeString(Path.of(reportPath), sb.toString());
    }

    private boolean isFieldAccessed(List<FieldAccessExpr> accesses, FieldDeclaration field) {
        for (FieldAccessExpr access : accesses) {
            if (access.getNameAsString().equals(field.getVariables().get(0).getNameAsString())) {
                return true;
            }
        }
        return false;
    }

    private class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration cd, Void arg) {
            super.visit(cd, arg);
            String className = cd.getFullyQualifiedName().get();
            List<FieldDeclaration> fields = cd.getFields();
            List<MethodDeclaration> methods = cd.getMethods();
            classFields.put(className, fields);
            classMethods.put(className, methods);
        }
    }

    private class FieldAccessVisitor extends VoidVisitorAdapter<List<FieldAccessExpr>> {
        @Override
        public void visit(FieldAccessExpr fae, List<FieldAccessExpr> collector) {
            super.visit(fae, collector);
            if (fae.getScope() instanceof NameExpr) {
                collector.add(fae);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TccCalculator <projectPath> <reportPath>");
            System.exit(1);
        }
        String projectPath = args[0];
        String reportPath = args[1];
        TccCalculator calculator = new TccCalculator(projectPath);
        try {
            calculator.computeTcc();
            calculator.generateReport(reportPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}