package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

    @GetMapping(value = "", params = "matricule")
    public String searchEmployeByMatricule(@RequestParam(required = true) String matricule, final ModelMap model){
        model.addAttribute("model", employeService.findMyMatricule(matricule));
        model.addAttribute("page", "employeDetail");
        model.addAttribute("fragment", "employeDetail");
        return "main";
    }

    @GetMapping(value = "")
    public String listEmployes(final ModelMap model,
                               @RequestParam Integer page,
                               @RequestParam Integer size,
                               @RequestParam String sortDirection,
                               @RequestParam String sortProperty
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
}
