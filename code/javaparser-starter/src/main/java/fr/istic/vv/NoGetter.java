package fr.istic.vv;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoGetter extends VoidVisitorAdapter<Void> {

    private TypeDeclaration<?> thisClass;
    private List<FieldDeclaration> privateFields;

    public NoGetter(TypeDeclaration<?> thisClass) {
        this.thisClass = thisClass;
        privateFields = new ArrayList<>();
    }

    @Override
    public void visit(FieldDeclaration field, Void arg) {
        super.visit(field, arg);

        // Check if field is private and has no public getter
        if (field.isPrivate() && !hasPublicGetter(field)) {
            privateFields.add(field);
        }
    }

    public List<FieldDeclaration> getPrivateFields() {
        return privateFields;
    }

    private boolean hasPublicGetter(FieldDeclaration field) {
        String fieldName = field.getVariable(0).getNameAsString();

        // Check if there is a public getter method with the same name as the field
        Optional<MethodDeclaration> getter = thisClass.getMethods().stream()
                .filter(m -> m.getNameAsString().equals("get" + capitalize(fieldName)))
                .findFirst();

        return getter.isPresent() && getter.get().isPublic() && getter.get().getType().equals(field.getCommonType());
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
