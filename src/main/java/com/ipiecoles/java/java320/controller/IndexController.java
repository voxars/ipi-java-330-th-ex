package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private EmployeService employeService;

    @GetMapping("/")
    public String index(final ModelMap model){
        model.addAttribute("nbEmployes", employeService.countAllEmploye());
        model.addAttribute("page", "home");
        model.addAttribute("fragment", "home");
        return "main";
    }

}
