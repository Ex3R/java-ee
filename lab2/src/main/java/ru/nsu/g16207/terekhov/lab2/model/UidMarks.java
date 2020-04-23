package ru.nsu.g16207.terekhov.lab2.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@RequiredArgsConstructor
public class UidMarks {
    @NotBlank String uid;

    long amountOfMarks;

}
