package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @Autowired
    private EmployeRepository employeRepository;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String index(final ModelMap model){
        model.put("nbEmployes", employeRepository.count());
        return "accueil";
    }

}
