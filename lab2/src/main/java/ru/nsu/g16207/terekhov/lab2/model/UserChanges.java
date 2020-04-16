package ru.nsu.g16207.terekhov.lab2.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@RequiredArgsConstructor
public class UserChanges {
    @NotBlank String user;

    @NotNull Long amountOfChanges;
}
