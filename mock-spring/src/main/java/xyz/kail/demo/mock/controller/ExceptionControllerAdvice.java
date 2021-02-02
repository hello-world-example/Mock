package xyz.kail.demo.mock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ResponseBody
@ControllerAdvice(basePackageClasses = ExceptionControllerAdvice.class)
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleException(Exception ex) {
        return new HashMap<String, String>() {
            {
                put("code", "500");
                put("message", ex.getMessage());
            }
        };
    }

}
