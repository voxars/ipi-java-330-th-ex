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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Gestion des erreurs d'URL non reconnues
     * @param e Exception
     * @return Affiche la page d'erreur 404 avec le message de l'exception
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoHandlerFoundException(NoHandlerFoundException e){
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
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest req, RedirectAttributes attributes){
        return handleError(req, attributes, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Gestion des erreurs clients
     *
     * @param e Exception
     * @return Affiche la page d'erreur 400 avec le message de l'exception
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalAccessException(IllegalArgumentException e, HttpServletRequest req, RedirectAttributes attributes){
        return handleError(req, attributes, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestion des tentatives de sauvegarde d'employés avec matricule existant déjà
     *
     * @param e Exception
     * @return Affiche la page d'erreur 409 avec le message de l'exception
     */
    @ExceptionHandler(EntityExistsException.class)
    public ModelAndView handleEntityExistsException(EntityExistsException e, HttpServletRequest req, RedirectAttributes attributes){
        return handleError(req, attributes, e.getMessage(), HttpStatus.CONFLICT);
    }


    /**
     * Gestion des erreurs de sérialisation (ex : date incorrecte)
     *
     * @param e Exception contenant toutes les erreurs de sérialisation
     * @return Affiche la page d'erreur 400 avec un message précisant les champs et valeurs incorrects
     */
    @ExceptionHandler(BindException.class)
    public ModelAndView handleBindException(BindException e, HttpServletRequest req, RedirectAttributes attributes){
        ArrayList<String> errors = new ArrayList<>();
        for(FieldError fieldError : e.getFieldErrors()) {
            errors.add("La valeur " + fieldError.getRejectedValue() + " est incorrecte pour le champ " + fieldError.getField());
        }
        return handleError(req, attributes, "Erreur !<br><ul>" + errors.stream().map(s -> "<li>" + s + "</li>").collect(Collectors.joining("")), HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestion des echecs de contraintes d'intégrité en BDD (uniquement matricule pour l'instant)
     *
     * @param e Exception levée lors de la sauvegarde d'un employé avec matricule trop long
     * @return Affiche la page d'erreur 400 avec un message invitant à vérifier le matricule de l'employé
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleBindException(DataIntegrityViolationException e, HttpServletRequest req, RedirectAttributes attributes){
        return handleError(req, attributes, "Impossible de sauvegarder l'employé. Vérifier que le matricule n'excède pas 6 caractères.", HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestion des exceptions lorsqu'il y a erreur de typage sur les paramètres (ex page = 's')
     * @param e Exception contenant les informations sur l'erreur de typage
     * @return Affiche la page d'erreur 400 avec un message précisant le paramètre et la valeur erronée
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest req, RedirectAttributes attributes){
        return handleError(req, attributes, "La valeur " + e.getValue() + " pour le paramètre " + e.getName() + " est incorrect !", HttpStatus.BAD_REQUEST);

    }

    /**
     * Gestion de toutes les erreurs non gérées
     * @param e Exception lancée
     * @return Affiche la page d'erreur 500 avec un message générique
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnhandledException(Exception e, HttpServletRequest req, RedirectAttributes attributes){
        return handleError(req, attributes, "Une erreur technique est survenue !", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ModelAndView handleError(HttpServletRequest req, RedirectAttributes attributes, String error, HttpStatus status) {
        attributes.addFlashAttribute("type", "danger");
        attributes.addFlashAttribute("message", error);
        String referer = req.getHeader("Referer");
        if(referer != null){
            //Si on vient d'une page du site on redirige vers la page précédente
            return new ModelAndView("redirect:" + referer);
        } else {
            //Sinon on redirige vers la page d'erreur
            ModelAndView modelAndView = new ModelAndView("main", status);
            modelAndView.addObject("error", error);
            modelAndView.addObject("status", status);
            modelAndView.addObject("page", "error");
            modelAndView.addObject("fragment", "error");
            return modelAndView;
        }
    }
}
