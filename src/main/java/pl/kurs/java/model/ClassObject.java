package pl.kurs.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(indexes = {@Index(columnList = "id"), @Index(columnList = "class_name_name")})
public class ClassObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ClassDescriber className;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ClassField> classFields;

    public ClassObject(ClassDescriber className, List<ClassField> classFields) {
        this.className = className;
        this.classFields = classFields;
    }
}
