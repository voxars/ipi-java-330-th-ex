package com.ipiecoles.java.java320.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Gestion des erreurs d'URL non reconnues
     * @param e Exception
     * @return Affiche la page d'erreur 404 avec le message de l'exception
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEntityNotFoundException(NoHandlerFoundException e){
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("error", "L'URL + " + e.getRequestURL() + " n'existe pas (méthode " + e.getHttpMethod() + ")");
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("page", "error");
        modelAndView.addObject("fragment", "error");
        return modelAndView;
    }
    /**
     * Gestion des erreurs d'employés non trouvés
     * @param e Exception
     * @return Affiche la page d'erreur 404 avec le message de l'exception
     */
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

    /**
     * Gestion des erreurs clients
     *
     * @param e Exception
     * @return Affiche la page d'erreur 400 avec le message de l'exception
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleIllegalAccessException(IllegalArgumentException e){
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("error", e.getMessage());
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("page", "error");
        modelAndView.addObject("fragment", "error");
        return modelAndView;
    }

    /**
     * Gestion des tentatives de sauvegarde d'employés avec matricule existant déjà
     *
     * @param e Exception
     * @return Affiche la page d'erreur 409 avec le message de l'exception
     */
    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleEntityExistsException(EntityExistsException e){
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("error", e.getMessage());
        modelAndView.addObject("status", HttpStatus.CONFLICT.value());
        modelAndView.addObject("page", "error");
        modelAndView.addObject("fragment", "error");
        return modelAndView;
    }


    /**
     * Gestion des erreurs de sérialisation (ex : date incorrecte)
     *
     * @param e Exception contenant toutes les erreurs de sérialisation
     * @return Affiche la page d'erreur 400 avec un message précisant les champs et valeurs incorrects
     */
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

    /**
     * Gestion des echecs de contraintes d'intégrité en BDD (uniquement matricule pour l'instant)
     *
     * @param e Exception levée lors de la sauvegarde d'un employé avec matricule trop long
     * @return Affiche la page d'erreur 400 avec un message invitant à vérifier le matricule de l'employé
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBindException(DataIntegrityViolationException e){
        ModelAndView modelAndView = new ModelAndView("main");
        ArrayList<String> errors = new ArrayList<>();
        modelAndView.addObject("error", "Impossible de sauvegarder l'employé. Vérifier que le matricule n'excède pas 6 caractères.");
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("page", "error");
        modelAndView.addObject("fragment", "error");
        return modelAndView;
    }

    /**
     * Gestion des exceptions lorsqu'il y a erreur de typage sur les paramètres (ex page = 's')
     * @param e Exception contenant les informations sur l'erreur de typage
     * @return Affiche la page d'erreur 400 avec un message précisant le paramètre et la valeur erronée
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        ModelAndView modelAndView = new ModelAndView("main");
        ArrayList<String> errors = new ArrayList<>();
        modelAndView.addObject("error", "La valeur " + e.getValue() + " pour le paramètre " + e.getName() + " est incorrect !");
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("page", "error");
        modelAndView.addObject("fragment", "error");
        return modelAndView;
    }

    /**
     * Gestion de toutes les erreurs non gérées
     * @param e Exception lancée
     * @return Affiche la page d'erreur 500 avec un message générique
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleUnhandledException(Exception e){
        ModelAndView modelAndView = new ModelAndView("main");
        ArrayList<String> errors = new ArrayList<>();
        modelAndView.addObject("error", "Une erreur technique est survenue !");
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("page", "error");
        modelAndView.addObject("fragment", "error");
        return modelAndView;
    }
}
