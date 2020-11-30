package pl.kurs.java.service;

import org.springframework.stereotype.Service;
import pl.kurs.java.model.*;
import pl.kurs.java.model.exception.ClassObjectException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ObjectService {
    private static final String CSV_EXTENSION = ".csv";
    private static final String SERVER_LOCATION = "src\\main\\resources\\";

    public ClassObject createClassObject(ClassDescriber classDescriber, Map<String, String> fieldsMap) throws ClassObjectException {
        validate(classDescriber, fieldsMap);
        return new ClassObject(classDescriber, createClassFields(classDescriber, fieldsMap));
    }

    @org.jetbrains.annotations.NotNull
    private List<ClassField> createClassFields(ClassDescriber classDescriber, Map<String, String> fieldsMap) throws ClassObjectException {
        int counter = fieldsMap.size();
        List<ClassField> fields = fieldsMap.entrySet().stream()
                .map(f -> {
                    try {
                        return new ClassField(getFieldDescriber(classDescriber, f.getKey()), f.getValue());
                    } catch (ClassObjectException e) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (counter != fields.size()){
            throw new ClassObjectException("Incorrect value type");
        }
        return fields;
    }

    private FieldDescriber getFieldDescriber(ClassDescriber classDescriber, String name) {
        return classDescriber.getFieldDescriberList().stream()
                .filter(f -> f.getName().equals(name)).findFirst()
                .orElse(null);
    }

    private void validate(ClassDescriber classDescriber, Map<String,String> fieldsMap) throws ClassObjectException {
        if (fieldsMap == null){
            throw new ClassObjectException("No fields provided");
        }
        int countClassFields = classDescriber.getFieldDescriberList().size();
        int providedFields = fieldsMap.size();

        if (countClassFields!=providedFields){
            throw new ClassObjectException("Wrong number of fields provided");
        }

        List<String> correctFields = new ArrayList<>(fieldsMap.keySet());
        List<String> classFields = classDescriber.getFieldDescriberList()
                .stream().map(FieldDescriber::getName).collect(Collectors.toList());

        if (!classFields.containsAll(correctFields)){
            throw new ClassObjectException("Wrong fields provided");
        }

        long valuesCount = fieldsMap.values().stream().filter(Objects::nonNull).filter(v-> !v.equals("")).count();

        if(valuesCount!=countClassFields){
            throw new ClassObjectException("Some empty field");
        }
    }

    public File saveToCSV(List<ClassObject> classObjects) throws ClassObjectException {
        if (classObjects==null || classObjects.size()==0){
            throw new ClassObjectException("empty object List");
        }

        ClassDescriber classDescriber = classObjects.get(0).getClassName();

        StringBuilder sb = new StringBuilder();
        sb.append(createCsvHeaders(classDescriber));
        for (ClassObject classObject: classObjects) {
            sb.append(createCsvObjectLines(classObject));
        }
        CsvWriter.save(SERVER_LOCATION + classDescriber.getName() + CSV_EXTENSION, sb.toString());
        return new File(SERVER_LOCATION + classDescriber.getName() + CSV_EXTENSION);
    }

    private String createCsvHeaders(ClassDescriber classDescriber) {
        StringBuilder sb = new StringBuilder();
        sb.append("No.");
        List<FieldDescriber> classFields = classDescriber.getFieldDescriberList();
        for (int i = 0; i < classFields.size(); i++) {
            sb.append(", ").append(classFields.get(i).getName());
        }
        return sb.toString();
    }

    private String createCsvObjectLines(ClassObject classObject) {
        StringBuilder sb = new StringBuilder();
        sb.append('\n').append(classObject.getId());
        List<ClassField> classFields = classObject.getClassFields();
        for (int i = 0; i < classFields.size(); i++) {
            sb.append(", ").append(classFields.get(i).getValue());
        }
        return sb.toString();
    }
}
