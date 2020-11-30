package pl.kurs.java.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class Filter {
    private final String fieldName;
    private final List<String> values;
    private final FilterRadioType filterRadioType;
}
