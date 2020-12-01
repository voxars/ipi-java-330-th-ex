package com.ipiecoles.java.java320.controller;

import com.ipiecoles.java.java320.model.Commercial;
import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.model.Manager;
import com.ipiecoles.java.java320.model.Technicien;
import com.ipiecoles.java.java320.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.awt.*;
import java.util.Optional;

@Controller
@RequestMapping("/employes")
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String getEmployeById(@PathVariable Long id, final ModelMap model){

        Optional<Employe> employeOptional = employeRepository.findById(id);
        //Ici il faudrait gérer l'erreur 404 !
        if(employeOptional.isEmpty()){
            throw new EntityNotFoundException("L'employé d'identifiant " + id + " n'a pas été trouvé !");
        }

        model.put("employe", employeOptional.get());
        //model.put("nombreEmployes", employeRepository.count());

        return "detail";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new/{typeEmploye}")
    public String newEmploye(@PathVariable String typeEmploye, final ModelMap model){
        switch (typeEmploye.toLowerCase()){
            case "technicien": model.put("employe", new Technicien()); break;
            case "commercial": model.put("employe", new Commercial()); break;
            case "manager": model.put("employe", new Manager()); break;
        }
        return "detail";
    }

    ///employes/commercial
    ///employes/technicien
    ///employes/manager
    @RequestMapping(method = RequestMethod.POST, value = "/commercial", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveCommercial(Commercial employe){
        return saveEmploye(employe);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/technicien", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveTechnicien(Technicien employe){
        return saveEmploye(employe);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/manager", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveManager(Manager employe){
        return saveEmploye(employe);
    }

    private RedirectView saveEmploye(Employe employe){
        employe = employeRepository.save(employe);
        return new RedirectView("/employes/" + employe.getId());
    }

    //1 Request Mapping pour chaque type d'employé pour gérer la sauvegarde
    ///employes/commercial/{id}
    ///employes/technicien/{id}
    ///employes/manager/{id}

    @RequestMapping(method = RequestMethod.GET, value = "", params = "matricule")
    public String searchEmployeByMatricule(@RequestParam String matricule, final ModelMap model){
        Employe employe = employeRepository.findByMatricule(matricule);
        //Ici il faudrait gérer l'erreur 404 !
        model.put("employe", employe);
        return "detail";
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String listEmployes(final ModelMap model,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "ASC") String sortDirection,
                               @RequestParam(defaultValue = "matricule") String sortProperty){
        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Employe> pageEmploye = employeRepository.findAll(pageRequest);
        model.put("employes", pageEmploye);
        //pageEmploye.has
//        pageEmploye.getTotalElements();
        //employes.totalElements =>
        model.put("pageNumber", page + 1);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        model.put("start", page * size + 1);
        model.put("end", (page) * size + pageEmploye.getNumberOfElements());
        return "listeEmployes";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public RedirectView deleteEmploye(@PathVariable Long id){
        if(!employeRepository.existsById(id)){
            throw new EntityNotFoundException("L'employé d'identifiant " + id + " n'a pas été trouvé !");
        }
        employeRepository.deleteById(id);

        return new RedirectView("/employes?page=0&size=10&sortProperty=matricule&sortDirection=ASC");
    }

}
