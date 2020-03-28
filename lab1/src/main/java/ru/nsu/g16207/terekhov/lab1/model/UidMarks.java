package ru.nsu.g16207.terekhov.lab1.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@RequiredArgsConstructor
public class UidMarks {
    @NotBlank String uid;

    @NotNull Long amountOfMarks;

}
