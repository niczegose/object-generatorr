package pl.kurs.java.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.kurs.java.model.*;

import javax.persistence.criteria.*;

public final class ClassObjectSpecification {
    private ClassObjectSpecification() {
    }
    public static Specification<ClassObject> classNameLike(String searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);
            Path<String> className = root.get(ClassObject_.className).get(ClassDescriber_.name);
            return cb.like(cb.lower(className), containsLikePattern);
        };
    }

    public static Specification<ClassObject> fieldNameAndValueLike(String fieldNameTerm, String searchTerm) {
        return (root, query, cb) -> {
            String containsFieldLikePattern = getContainsLikePattern(fieldNameTerm);
            ListJoin<ClassObject, ClassField> objectClassFieldListJoin = root.join(ClassObject_.classFields);
            Join<ClassField, FieldDescriber> classFieldDescriberJoin = objectClassFieldListJoin.join(ClassField_.fieldDescriber);
            query.distinct(true);
            return cb.and(
                    cb.like(classFieldDescriberJoin.get(FieldDescriber_.name), containsFieldLikePattern),
                    cb.equal(objectClassFieldListJoin.get(ClassField_.value), searchTerm)
            );
        };
    }

    public static Specification<ClassObject> fieldNameAndValueDifferentFrom(String fieldNameTerm, String searchTerm) {
        return (root, query, cb) -> {
            String containsFieldLikePattern = getContainsLikePattern(fieldNameTerm);
            ListJoin<ClassObject, ClassField> objectClassFieldListJoin = root.join(ClassObject_.classFields);
            Join<ClassField, FieldDescriber> classFieldDescriberJoin = objectClassFieldListJoin.join(ClassField_.fieldDescriber);
            query.distinct(true);
            return cb.and(
                    cb.like(classFieldDescriberJoin.get(FieldDescriber_.name), containsFieldLikePattern),
                    cb.notEqual(objectClassFieldListJoin.get(ClassField_.value), searchTerm)
            );
        };
    }

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        }
        else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }

    public static Specification<ClassObject> fieldNameAndIntegerValueGreaterThan(String fieldNameTerm, double searchTerm) {
        return (root, query, cb) -> {
            String containsFieldLikePattern = getContainsLikePattern(fieldNameTerm);
            ListJoin<ClassObject, ClassField> objectClassFieldListJoin = root.join(ClassObject_.classFields);
            Join<ClassField, FieldDescriber> classFieldDescriberJoin = objectClassFieldListJoin.join(ClassField_.fieldDescriber);
            query.distinct(true);
            return cb.and(
                    cb.like(classFieldDescriberJoin.get(FieldDescriber_.name), containsFieldLikePattern),
                    cb.greaterThan(cb.function("DOUBLE", Double.class, objectClassFieldListJoin.get(ClassField_.value)), searchTerm)
            );
        };
    }


}
