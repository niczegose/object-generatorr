package pl.kurs.java.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class ClassFilter {
    private final String name;
    private final String radioName;
    private final String label;
    private final List<FilterRadioType> radioOptions;
}
