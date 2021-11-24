package com.contactmanagementsystem.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String message;
    private boolean success;
}
