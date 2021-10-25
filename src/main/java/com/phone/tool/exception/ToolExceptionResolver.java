package com.phone.tool.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ToolExceptionResolver extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ToolException.class)
    public ResponseEntity handleServerException(ToolException ex, HttpServletResponse response) throws JsonProcessingException {
        int status = ex.getStatusCode();
        response.setStatus(status);
        Map<String, Object> exceptionResult = buildExceptionResult(status, ex.getMessage());
        log.error("Server Exception : {}, status : {}", ex, status);
        return new ResponseEntity(new ObjectMapper().writeValueAsString(exceptionResult), HttpStatus.valueOf(status));
    }

    private Map<String, Object> buildExceptionResult(int status, Object message) {
        Map<String, Object> map = new HashMap();
        map.put("statusCode", status);
        map.put("message", message);
        return map;
    }
}
