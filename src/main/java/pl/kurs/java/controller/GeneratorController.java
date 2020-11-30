package pl.kurs.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kurs.java.model.ClassDescriber;
import pl.kurs.java.service.ObjectService;
import pl.kurs.java.model.exception.ClassObjectException;
import pl.kurs.java.repository.ClassDescriberRepository;
import pl.kurs.java.repository.ClassObjectRepository;

import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/object")
@RequiredArgsConstructor
public class GeneratorController {

    @Autowired
    ClassDescriberRepository classDescriberRepository;
    @Autowired
    ClassObjectRepository classObjectRepository;
    private final ObjectService objectService;

    @GetMapping
    public String objectGenerator(@RequestParam(value = "objectType", required = false) String objectName,
                                  @RequestParam Map<String, String> params,
                                  ModelMap model) {

        if (!StringUtils.isEmpty(objectName) && params.size() == 1) {
            try {
                ClassDescriber classDescriber = classDescriberRepository.findByNameLike(objectName).orElseThrow(NoSuchElementException::new);
                model.addAttribute("message", classDescriber.getName());
                model.addAttribute("fieldList", classDescriber.getStandardFields());
                model.addAttribute("fieldListChoice", classDescriber.getChoiceFields());
            } catch (NoSuchElementException e) {
                model.addAttribute("message", "Illegal object class");
            }
        } else if (params.size() > 1) {
            try {
                ClassDescriber classDescriber = classDescriberRepository.findByNameLike(objectName).orElseThrow(NoSuchElementException::new);
                params.remove("objectType");
                classObjectRepository.saveAndFlush(objectService.createClassObject(classDescriber, params));
                model.addAttribute("message", "New " + classDescriber.getName() + " successfully added to the database");
            }catch (NoSuchElementException | ClassObjectException e){
                model.addAttribute("message", e.getMessage());
            }
        }
        model.addAttribute("objectList", classDescriberRepository.findAll());
        return "generator";
    }
}
