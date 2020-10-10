package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @GetMapping("/employes/{id}")
    public String getEmployeById(@PathVariable("id") Long id, final ModelMap model){
        model.addAttribute("model", employeService.findById(id));
        model.addAttribute("page", "employeDetail");
        model.addAttribute("fragment", "employeDetail");
        System.out.println();
        return "main";
    }
}
