package pl.kurs.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.kurs.java.model.ClassDescriber;
import pl.kurs.java.model.DataType;
import pl.kurs.java.model.FieldDescriber;
import pl.kurs.java.repository.ClassDescriberRepository;
import pl.kurs.java.repository.FieldDescriberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class MvcExampleApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(MvcExampleApplication.class, args);
    }

    @Autowired
    private ClassDescriberRepository classDescriberRepository;
    @Autowired
    private FieldDescriberRepository fieldDescriberRepository;

    @Override
    public void run(String... args) throws Exception {
//        List<FieldDescriber> fieldDescriberList = new ArrayList<>();
//        fieldDescriberList.add(new FieldDescriber("name", "Input party name:", DataType.STRING));
//        fieldDescriberList.add(new FieldDescriber("place", "Input party place:", DataType.STRING));
//        fieldDescriberList.add(new FieldDescriber("date", "Input date:", DataType.LOCAL_DATE));
//        fieldDescriberList.add(new FieldDescriber("time", "Input start hour:", DataType.STRING));
//        //fieldDescriberList.add(new FieldDescriber("gender", "Please select your gender", DataType.CHOICE, Arrays.asList("Male", "Female", "Other")));
//
//        if(classDescriberRepository.findByNameLike("Party").orElse(null) == null){
//            classDescriberRepository.saveAndFlush(ClassDescriber.builder().name("Party").fieldDescriberList(fieldDescriberList).build());
//        }

//        ClassDescriber edited = classDescriberRepository.findByNameLike("Person").get();
//        edited.getFieldDescriberList().add(new FieldDescriber("gender", "Please select your gender", DataType.CHOICE, Arrays.asList("Male", "Female", "Other")));
//        classDescriberRepository.save(edited);
        List<ClassDescriber> all =classDescriberRepository.findAll();
        System.out.println("Wszystkie osoby z bazy");
        System.out.println(all.size());
//        List<FieldDescriber> ehh =fieldDescriberRepository.findAll();
//        System.out.println(ehh.size() + " to je to");
//        System.out.println(all.get(1).getFieldDescriberList().get(0).getName());
        //all.forEach(System.out::println);
    }

}
