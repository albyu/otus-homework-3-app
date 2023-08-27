package ru.boldyrev.otus.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
public class NotFoundException extends Exception{

    @Getter
    private final String message;

}
