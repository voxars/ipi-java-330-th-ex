package com.ipiecoles.java.java320.controller;


import com.ipiecoles.java.java320.model.Technicien;
import com.ipiecoles.java.java320.service.EmployeService;
import com.ipiecoles.java.java320.service.TechnicienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/techniciens")
@Controller
public class TechnicienController {

    @Autowired
    private TechnicienService technicienService;

    @Autowired
    private EmployeService employeService;


    @GetMapping(value = "/{id}/manager/delete")
    public RedirectView removeManager(@PathVariable(name = "id") Long id, RedirectAttributes attributes){
        technicienService.deleteManager(id);
        attributes.addFlashAttribute("success", "Suppression du manager effectuée !");
        return new RedirectView("/employes/" + id);
    }

    @GetMapping(value = "/{id}/manager/add")
    public RedirectView addManager(@PathVariable(name = "id") Long id, @RequestParam(name = "matriculeToAdd") String matricule, RedirectAttributes attributes){
        Technicien technicien = this.technicienService.addManager(id, matricule);
        attributes.addFlashAttribute("success", "Ajout du manager " + matricule + " effectué !");
        return new RedirectView("/employes/" + id);
    }
}