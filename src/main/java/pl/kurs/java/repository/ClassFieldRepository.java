package pl.kurs.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.java.model.ClassField;

public interface ClassFieldRepository extends JpaRepository<ClassField, Long> {
}
