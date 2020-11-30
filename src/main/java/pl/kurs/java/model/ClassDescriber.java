package pl.kurs.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(indexes = {@Index(columnList = "name", unique = true)})
public class ClassDescriber {
    @Id
    @Column(name = "name", unique = true, length = 80)
    private String name;
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<FieldDescriber> fieldDescriberList;

    @Override
    public String toString() {
        return "ClassDescriber{" +
                "name='" + name + '\'' +
                ", fieldDescriberList=" + fieldDescriberList +
                '}';
    }

    private List<FieldDescriber> getFieldsThat(Predicate<FieldDescriber> predicateFilter){
        return fieldDescriberList.stream()
                .filter(predicateFilter).collect(Collectors.toList());
    }
    public List<FieldDescriber> getStandardFields(){
        return getFieldsThat(f -> !f.getDataType().equals(DataType.CHOICE));
    }

    public List<FieldDescriber> getChoiceFields(){
        return getFieldsThat(f -> f.getDataType().equals(DataType.CHOICE));
    }
}
