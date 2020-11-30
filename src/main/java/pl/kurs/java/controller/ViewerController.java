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
import pl.kurs.java.model.ClassObject;
import pl.kurs.java.model.exception.ClassDescriberException;
import pl.kurs.java.repository.ClassDescriberRepository;
import pl.kurs.java.repository.ClassObjectRepository;
import pl.kurs.java.service.DescriberService;
import pl.kurs.java.service.FilterService;
import pl.kurs.java.specification.ClassObjectSpecification;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/viewer")
@RequiredArgsConstructor
public class ViewerController {

    @Autowired
    ClassDescriberRepository classDescriberRepository;
    @Autowired
    ClassObjectRepository classObjectRepository;
    private final DescriberService describerService;
    private final FilterService filterService;

    @GetMapping
    public String objectViewer(@RequestParam(value = "objectType", required = false) String objectName,
                               ModelMap model) {

        if (!StringUtils.isEmpty(objectName)) {
            try {
                ClassDescriber object = classDescriberRepository.findByNameLike(objectName).orElseThrow(NoSuchElementException::new);
                model.addAttribute("message", object.getName());
                model.addAttribute("fieldList", object.getFieldDescriberList());
                model.addAttribute("filterList", filterService.createClassFilters(object));
                //List<ClassObject> classObjects = classObjectRepository.findByClassNameLike(object);
                List<ClassObject> classObjects = classObjectRepository.findAll(ClassObjectSpecification.classNameLike(objectName));
                model.addAttribute("itemList", classObjects);

            } catch (NoSuchElementException e) {
                model.addAttribute("message", "Illegal object class");
            }catch (ClassDescriberException e){
                model.addAttribute("message", e.getMessage());
            }

        }
        model.addAttribute("objectList", classDescriberRepository.findAll());
        return "tableViewer";
    }


}
