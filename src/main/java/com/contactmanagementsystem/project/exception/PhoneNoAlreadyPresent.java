package com.contactmanagementsystem.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneNoAlreadyPresent extends RuntimeException {
    public PhoneNoAlreadyPresent(String phone_number_already_present) {
    }
}
