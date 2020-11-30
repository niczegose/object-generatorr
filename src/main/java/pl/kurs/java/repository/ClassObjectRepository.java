package pl.kurs.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.kurs.java.model.ClassDescriber;
import pl.kurs.java.model.ClassObject;

import java.util.List;
import java.util.Optional;

public interface ClassObjectRepository extends JpaRepository<ClassObject, Long>, JpaSpecificationExecutor<ClassObject> {
    List<ClassObject> findByClassNameLike (ClassDescriber name);
}
