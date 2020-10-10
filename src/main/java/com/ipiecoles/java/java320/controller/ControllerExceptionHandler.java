package com.ipiecoles.java.java320.controller;

import org.hibernate.exception.DataException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import java.sql.DataTruncation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("error", e.getMessage());
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("page", "error");
        modelAndView.addObject("fragment", "error");
        return modelAndView;
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleIllegalAccessException(IllegalAccessException e){
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("error", e.getMessage());
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("page", "error");
        modelAndView.addObject("fragment", "error");
        return modelAndView;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBindException(BindException e){
        ModelAndView modelAndView = new ModelAndView("main");
        ArrayList<String> errors = new ArrayList<>();
        for(FieldError fieldError : e.getFieldErrors()) {
            errors.add("La valeur " + fieldError.getRejectedValue() + " est incorrecte pour le champ " + fieldError.getField());
        }
        modelAndView.addObject("error", "Erreur lors de l'enregistrement de l'employé !");
        modelAndView.addObject("errors", errors);
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("page", "error");
        modelAndView.addObject("fragment", "error");
        return modelAndView;
    }

}
