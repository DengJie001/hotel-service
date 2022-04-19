package com.easonhotel.admin.exception;

import com.easonhotel.common.ResData;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidatedExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    public ResData ErrorHandler(MethodArgumentNotValidException e) {
        return ResData.create(-1, e.getBindingResult().getFieldError().getDefaultMessage());
    }
}
