package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Commercial;
import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.model.Manager;
import com.ipiecoles.java.java320.model.Technicien;
import com.ipiecoles.java.java320.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;


@Controller
@RequestMapping("/employes")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @GetMapping("/{id}")
    public String getEmployeById(@PathVariable("id") Long id, final ModelMap model){
        model.addAttribute("model", employeService.findById(id));
        model.addAttribute("page", "employeDetail");
        model.addAttribute("fragment", "employeDetail");
        return "main";
    }

    @GetMapping("/new/{typeEmploye}")
    public String newEmploye(@PathVariable("typeEmploye") String typeEmploye, final ModelMap model) throws IllegalAccessException {
        switch (typeEmploye.toLowerCase()){
            case "technicien": model.addAttribute("model", new Technicien()); break;
            case "commercial": model.addAttribute("model", new Commercial()); break;
            case "manager": model.addAttribute("model", new Manager()); break;
            default: throw new IllegalAccessException("Type d'employé incorrect !");

        }
        model.addAttribute("page", "employeDetail");
        model.addAttribute("fragment", "employeDetail");
        return "main";
    }

    @GetMapping(value = "", params = "matricule")
    public String searchEmployeByMatricule(@RequestParam(required = true) String matricule, final ModelMap model){
        model.addAttribute("model", employeService.findMyMatricule(matricule));
        model.addAttribute("page", "employeDetail");
        model.addAttribute("fragment", "employeDetail");
        return "main";
    }

    @GetMapping(value = "")
    public String listEmployes(final ModelMap model,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "ASC") String sortDirection,
                               @RequestParam(defaultValue = "matricule") String sortProperty
                               ){
        Page<Employe> allEmployes = employeService.findAllEmployes(page, size, sortProperty, sortDirection);
        model.addAttribute("model", allEmployes);
        model.addAttribute("start", page * size + 1);
        model.addAttribute("end", (page + 1) * size);
        model.addAttribute("pageNumber", page + 1);
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("previousPage", page - 1);
        model.addAttribute("page", "employesListe");
        model.addAttribute("fragment", "employesListe");
        return "main";
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createEmploye(Employe employe, final ModelMap model){
        employe = employeService.creerEmploye(employe);
        model.addAttribute("model", employe);
        model.addAttribute("page", "employeDetail");
        model.addAttribute("fragment", "employeDetail");
        return "main";
    }

    @PostMapping(value = "/technicien/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView saveTechnicien(Technicien technicien, @PathVariable Long id, final ModelMap model, RedirectAttributes attributes){
       return saveEmploye(technicien, id, attributes);
    }

    @PostMapping(value = "/commercial/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView saveCommercial(Commercial commercial, @PathVariable Long id, final ModelMap model, RedirectAttributes attributes){
        return saveEmploye(commercial, id, attributes);
    }

    @PostMapping(value = "/manager/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView saveManager(Manager manager, @PathVariable Long id, final ModelMap model, RedirectAttributes attributes){
        return saveEmploye(manager, id, attributes);
    }

    private RedirectView saveEmploye(Employe employe, Long id, RedirectAttributes attributes){
        try{
            employe = employeService.updateEmploye(id, employe);
            attributes.addFlashAttribute("type", "success");
            attributes.addFlashAttribute("message", "Enregistrement de l'employé effectué !");
        }
        catch (Exception e){
            attributes.addFlashAttribute("type", "danger");
            attributes.addFlashAttribute("message", e.getMessage());
        }
        return new RedirectView("/employes/"+id);
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public RedirectView deleteEmploye(@PathVariable(name = "id") Long id, Map<String, Object> model, RedirectAttributes attributes) {
        this.employeService.deleteEmploye(id);
        attributes.addFlashAttribute("success", "Suppression de l'employé effectuée !");
        //Une fois supprimé, on redirige l'utilisateur sur la liste des employés
        return new RedirectView("/employes");
    }
}
