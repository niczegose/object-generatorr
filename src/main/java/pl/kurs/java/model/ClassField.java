package pl.kurs.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kurs.java.model.exception.ClassObjectException;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
public class ClassField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private FieldDescriber fieldDescriber;
    private String value;

    public ClassField(FieldDescriber fieldDescriber, String value) throws ClassObjectException {
        this.fieldDescriber = fieldDescriber;
        this.value = value;
        validateValue();
    }

    private void validateValue() throws ClassObjectException {
        fieldDescriber.getDataType().validateValue(this);
    }
}