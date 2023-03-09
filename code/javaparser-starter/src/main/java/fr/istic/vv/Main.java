package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        Path root = Paths.get(args[0]);
        if (!root.toFile().exists() || !root.toFile().isDirectory() || !root.toFile().canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        // Parse all Java files in the provided directory
        List<CompilationUnit> units = new ArrayList<>();
        File[] files = root.toFile().listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                CompilationUnit unit = StaticJavaParser.parse(file);
                units.add(unit);
            }
        }

        // Traverse each class in each Java file and check for private fields with no public getters
        for (CompilationUnit unit : units) {
            unit.findAll(TypeDeclaration.class).forEach(type -> {
                if (!type.isPublic()) {
                    NoGetter printer = new NoGetter(type);
                    type.accept(printer, null);
                    printer.getPrivateFields().forEach(field -> {
                        System.out.println(String.format("Private field %s in class %s has no public getter",
                                field.getVariable(0).getNameAsString(), type.getNameAsString()));
                    });
                }
            });
        }
    }
}
