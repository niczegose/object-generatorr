package pl.kurs.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.java.model.FieldDescriber;

public interface FieldDescriberRepository extends JpaRepository<FieldDescriber, Long> {
}
