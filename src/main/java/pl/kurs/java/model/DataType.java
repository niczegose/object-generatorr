package pl.kurs.java.model;


import pl.kurs.java.model.exception.ClassObjectException;

import java.time.LocalDate;
import java.util.function.Consumer;

public enum DataType {
    STRING("String", ClassField::getValue),
    INTEGER("Int", classField -> Integer.parseInt(classField.getValue())),
    DOUBLE("Double", classField -> Double.parseDouble(classField.getValue())),
    LOCAL_DATE("Local Date", classField -> LocalDate.parse(classField.getValue())),
    CHOICE("Choice", ClassField::getValue);

    private String name;
    private Consumer<ClassField> validator;

    DataType(String name, Consumer<ClassField> validator) {
        this.name = name;
        this.validator=validator;
    }

    public void validateValue(ClassField classField) throws ClassObjectException {
        try {
            validator.accept(classField);
        }catch (Exception e){
            throw new ClassObjectException("Incorrect value type");
        }
    }

    public String getName(){
        return name;
    }

}
