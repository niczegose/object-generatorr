package pl.kurs.java.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.kurs.java.model.*;
import pl.kurs.java.model.exception.ClassDescriberException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DescriberService {
    @NotNull
    public List<FieldDescriber> generateFields(List<String> fieldsNameList, List<String> labelsList, List<String> dataTypeList) throws ClassDescriberException {
        if (fieldsNameList.size() != labelsList.size() || fieldsNameList.size() != dataTypeList.size()) {
            throw new ClassDescriberException("Incorrect fields sizes");
        }
        List<FieldDescriber> fieldDescriberList = new ArrayList<>();
        for (int i = 0; i < fieldsNameList.size(); i++) {
            if (isNotTotallyEmpty(fieldsNameList.get(i), labelsList.get(i))) {
                fieldDescriberList.add(new FieldDescriber(fieldsNameList.get(i), labelsList.get(i), setDataType(dataTypeList.get(i))));
            }
        }
        return fieldDescriberList;
    }

    private boolean isNotTotallyEmpty(String field, String label) {
        return !(field.equals("") && label.equals(""));
    }

    private DataType setDataType(String name) throws ClassDescriberException {
        EnumSet<DataType> enumSet = EnumSet.allOf(DataType.class);

        return enumSet.stream().filter(dataType -> dataType.getName().equals(name))
                .findFirst().orElseThrow(() -> new ClassDescriberException("Illegal data type"));
    }

    public void validateFields(List<FieldDescriber> describerList) throws ClassDescriberException {
        long countStart = describerList.size();
        long countEnd = describerList.stream().filter(f -> !f.getName().equals("") && !f.getLabel().equals("")).count();

        if (countEnd != countStart) {
            throw new ClassDescriberException("Incorrect input, fields name and label shouldn't be empty");
        }

        long countSet = describerList.stream().map(FieldDescriber::getName).collect(Collectors.toSet()).size();

        if (countSet != countStart) {
            throw new ClassDescriberException("Incorrect input, all fields should have unique names");
        }
    }


}
