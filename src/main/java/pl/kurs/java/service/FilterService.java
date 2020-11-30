package pl.kurs.java.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.kurs.java.model.*;
import pl.kurs.java.model.exception.ClassDescriberException;
import pl.kurs.java.specification.ClassObjectSpecification;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilterService {

    public List<ClassFilter> createClassFilters(ClassDescriber classDescriber) throws ClassDescriberException {
        if (classDescriber == null) {
            throw new ClassDescriberException("no class provided");
        }
        List<ClassFilter> classFilters = classDescriber.getFieldDescriberList().stream()
                .map(this::setFilter).collect(Collectors.toList());
        int count = classFilters.size();
        long countNotNull = classFilters.stream().filter(Objects::nonNull).count();
        if (count != countNotNull) {
            throw new ClassDescriberException("Incorrect data type");
        }
        return classFilters;
    }

    private ClassFilter setFilter(FieldDescriber fieldDescriber) {
        DataType dataType = fieldDescriber.getDataType();
        String filerBy = "Filter by ";
        String radioName = "choice";
        if (dataType == DataType.STRING || dataType == DataType.CHOICE) {
            List<FilterRadioType> radioTypes = new ArrayList<>(Arrays.asList(FilterRadioType.EQUAL, FilterRadioType.DIFFERENT, FilterRadioType.DISABLE));
            return new ClassFilter(fieldDescriber.getName(), radioName + fieldDescriber.getName(), filerBy + fieldDescriber.getName(), radioTypes);
        }
        if (dataType == DataType.INTEGER || dataType == DataType.DOUBLE) {
            List<FilterRadioType> radioTypes = new ArrayList<>(Arrays.asList(FilterRadioType.EQUAL, FilterRadioType.GREATER, FilterRadioType.SMALLER, FilterRadioType.DISABLE));
            return new ClassFilter(fieldDescriber.getName(), radioName + fieldDescriber.getName(), filerBy + fieldDescriber.getName(), radioTypes);
        }
        if (dataType == DataType.LOCAL_DATE) {
            List<FilterRadioType> radioTypes = new ArrayList<>(Arrays.asList(FilterRadioType.EQUAL, FilterRadioType.DIFFERENT, FilterRadioType.BEFORE, FilterRadioType.AFTER, FilterRadioType.DISABLE));
            return new ClassFilter(fieldDescriber.getName(), radioName + fieldDescriber.getName(), filerBy + fieldDescriber.getName(), radioTypes);
        } else {
            return null;
        }
    }

    public List<Filter> setObjectFilters(List<ClassFilter> classFilters, Map<String, String> params) {
        List<Filter> filters = new ArrayList<>();
        for (ClassFilter classFilter : classFilters) {
            String fieldName = classFilter.getName();
            String fieldValues = params.getOrDefault(fieldName, null);
            FilterRadioType filterRadioType;
            try {
                filterRadioType = FilterRadioType.valueOf(params.getOrDefault(classFilter.getRadioName(), null));
            } catch (IllegalArgumentException | NullPointerException e) {
                filterRadioType = null;
            }

            if (!StringUtils.isEmpty(fieldValues) && filterRadioType != null) {
                List<String> strings = new ArrayList<>(Arrays.asList(fieldValues.split(","))).stream()
                        .map(String::trim).collect(Collectors.toList());
                filters.add(new Filter(fieldName, strings, filterRadioType));
            }
        }
        return filters;
    }


    public Specification<ClassObject> getSpecification(String className, List<Filter> filters) {
        Specification<ClassObject> spec = Specification.where(ClassObjectSpecification.classNameLike(className));
        for (Filter filter : filters) {
            if (filter.getFilterRadioType() == FilterRadioType.EQUAL) {
                spec = spec.and(specForEqual(filter));
            } else if (filter.getFilterRadioType() == FilterRadioType.DIFFERENT) {
                spec = spec.and(specForDifferent(filter));
            }
//            else if (filter.getFilterRadioType() == FilterRadioType.GREATER) {
//                spec = spec.and(specForGreater(filter));
//            }
        }
        return spec;
    }

    private Specification<ClassObject> specForEqual(Filter filter) {
        Specification<ClassObject> specification = null;
        for (String s : filter.getValues()) {
            if (specification == null) {
                specification = ClassObjectSpecification.fieldNameAndValueLike(filter.getFieldName(), s);
            } else {
                specification = specification.or(ClassObjectSpecification.fieldNameAndValueLike(filter.getFieldName(), s));
            }
        }
        return specification;
    }

    private Specification<ClassObject> specForDifferent(Filter filter) {
        Specification<ClassObject> specification = null;
        for (String s : filter.getValues()) {
            if (specification == null) {
                specification = ClassObjectSpecification.fieldNameAndValueDifferentFrom(filter.getFieldName(), s);
            } else {
                specification = specification.or(ClassObjectSpecification.fieldNameAndValueDifferentFrom(filter.getFieldName(), s));
            }
        }
        return specification;
    }

    private Specification<ClassObject> specForGreater(Filter filter) {
        return ClassObjectSpecification.fieldNameAndIntegerValueGreaterThan(filter.getFieldName(), Double.parseDouble(filter.getValues().get(0)));
    }
}
