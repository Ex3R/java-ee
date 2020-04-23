package ru.nsu.g16207.terekhov.lab2.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class UidMarks {
    int uid;

    long amountOfMarks;

}
