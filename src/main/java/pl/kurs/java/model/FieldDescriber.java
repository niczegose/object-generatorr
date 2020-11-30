package pl.kurs.java.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class FieldDescriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String label;
    @Enumerated(EnumType.ORDINAL)
    private DataType dataType;
    @ElementCollection
    private List<String> choiceList;

    public FieldDescriber(String name, String label, DataType dataType) {
        this.name = name;
        this.label = label;
        this.dataType = dataType;
    }
    public FieldDescriber(String name, String label, DataType dataType, List<String> choiceList) {
        this.name = name;
        this.label = label;
        this.dataType = dataType;
        this.choiceList = choiceList;
    }
}
