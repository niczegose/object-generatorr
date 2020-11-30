package pl.kurs.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kurs.java.model.ClassDescriber;
import pl.kurs.java.model.ClassFilter;
import pl.kurs.java.model.ClassObject;
import pl.kurs.java.model.Filter;
import pl.kurs.java.model.exception.ClassDescriberException;
import pl.kurs.java.model.exception.ClassObjectException;
import pl.kurs.java.repository.ClassDescriberRepository;
import pl.kurs.java.repository.ClassObjectRepository;
import pl.kurs.java.service.FilterService;
import pl.kurs.java.service.ObjectService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/download")
@RequiredArgsConstructor
public class DownloadController {

    @Autowired
    ClassDescriberRepository classDescriberRepository;
    @Autowired
    ClassObjectRepository classObjectRepository;
    private final ObjectService objectService;
    private final FilterService filterService;

    //@RequestMapping(path = "/download", method = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<Resource> download(@RequestParam(value = "objectType") String className,
                                             @RequestParam Map<String, String> params
                                             ) throws IOException, ClassObjectException, NoSuchElementException, ClassDescriberException {
        ClassDescriber classDescriber = classDescriberRepository.findByNameLike(className).orElseThrow(NoSuchElementException::new);
        params.remove("objectType");
        List<ClassFilter> classFilters = filterService.createClassFilters(classDescriber);
        List<Filter> filters = filterService.setObjectFilters(classFilters, params);
        Specification<ClassObject> specification = filterService.getSpecification(className, filters);

        List<ClassObject> classObjects = classObjectRepository.findAll(specification);

        File file = objectService.saveToCSV(classObjects);

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        System.out.println(file.getName());
        HttpHeaders headers = new HttpHeaders(); headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}