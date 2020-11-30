package pl.kurs.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.java.model.ClassDescriber;

import java.util.Optional;

public interface ClassDescriberRepository extends JpaRepository<ClassDescriber, Long> {
    Optional<ClassDescriber> findByNameLike (String name);
}
