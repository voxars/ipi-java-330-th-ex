package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Manager;
import com.ipiecoles.java.java320.service.EmployeService;
import com.ipiecoles.java.java320.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RequestMapping("/managers")
@Controller
public class ManagerController{

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeService employeService;


    @GetMapping(value = "/{id}/techniciens/{idTech}/delete")
    public RedirectView deleteTechnicien(@PathVariable("id") Long id, @PathVariable("idTech") Long idTech, RedirectAttributes attributes){
        managerService.deleteTechniciens(id, idTech);
        attributes.addFlashAttribute("success", "Suppression du technicien dans l'équipe effectuée !");
        return new RedirectView("/employes/" + id);
    }

    @GetMapping(value = "/{id}/techniciens/add")
    public RedirectView addTechnicien(@PathVariable("id") Long id, @RequestParam("matriculeToAdd") String matricule, RedirectAttributes attributes){
        managerService.addTechniciens(id, matricule);
        attributes.addFlashAttribute("success", "Ajout du technicien " + matricule + " dans l'équipe effectuée !");
        return new RedirectView("/employes/" + id);
    }
}