package com.service.ttucktak.base;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResEntity {
    private int status;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResEntity> toResponseEntity(BaseErrorCode error) {
        return ResponseEntity
                .status(error.getStatus())
                .body(ErrorResEntity.builder()
                        .code(error.name())
                        .message(error.getMessage())
                        .build()
                );
    }
}
