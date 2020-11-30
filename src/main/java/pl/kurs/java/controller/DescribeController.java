package pl.kurs.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kurs.java.model.*;
import pl.kurs.java.model.exception.ClassDescriberException;
import pl.kurs.java.repository.ClassDescriberRepository;
import pl.kurs.java.service.DescriberService;

import java.util.EnumSet;
import java.util.List;

@Controller
@RequestMapping("/describe")
@RequiredArgsConstructor
public class DescribeController {

    @Autowired
    ClassDescriberRepository classDescriberRepository;
    private final DescriberService describerService;

    @GetMapping
    public String newClassCreator(@RequestParam(value = "className", required = false) String className,
                                  @RequestParam(value = "field", required = false) List<String> fieldsNameList,
                                  @RequestParam(value = "label", required = false) List<String> labelsList,
                                  @RequestParam(value = "dataType", required = false) List<String> dataTypeList,
                                  ModelMap model) {

        model.addAttribute("typeList", EnumSet.allOf(DataType.class));

        if (!StringUtils.isEmpty(className) && isFilled(fieldsNameList)) {
            try {
                if (className.length()>80){
                    throw new ClassDescriberException("Class name is too long, max length is 80 characters");
                }
                List<FieldDescriber> describerList = describerService.generateFields(fieldsNameList, labelsList, dataTypeList);
                describerService.validateFields(describerList);

                if (classDescriberRepository.findByNameLike(className).orElse(null) == null) {
                    classDescriberRepository.saveAndFlush(ClassDescriber.builder().name(className).fieldDescriberList(describerList).build());
                }else {
                    throw new ClassDescriberException("Class with such a name already exist");
                }
                model.addAttribute("message", "Class successfully added to the database templates");
            } catch (ClassDescriberException e) {
                model.addAttribute("classInput", className);
                model.addAttribute("message", e.getMessage());
                fillFieldsWithCache(fieldsNameList, labelsList, dataTypeList, model);
            }
        } else if (isFilled(fieldsNameList)) {
            //System.out.println(fieldsNameList.size() + " fields size");
            //System.out.println(labelsList.size() + " labels size");
            fillFieldsWithCache(fieldsNameList, labelsList, dataTypeList, model);
            model.addAttribute("message", "You must enter a class name");
        }

        return "newDescriber";
    }

    private void fillFieldsWithCache(List<String> fieldsNameList, List<String> labelsList, List<String> dataTypeList, ModelMap model) {
        try {
            List<FieldDescriber> describerList = describerService.generateFields(fieldsNameList, labelsList, dataTypeList);
            model.addAttribute("fldList", describerList);
        } catch (ClassDescriberException e) {
            model.addAttribute("message2", e.getMessage());
        }
    }

    private boolean isFilled(List<String> fieldsNameList) {
        return fieldsNameList != null;
    }


}
