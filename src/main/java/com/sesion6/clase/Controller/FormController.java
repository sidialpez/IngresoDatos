package com.sesion6.clase.Controller;

import com.sesion6.clase.Model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FormController {
    @GetMapping("/form")
    public String showForm(Model model){
        model.addAttribute("user", new User());
        return "form";
    }

    @PostMapping("/form")
    public String submitFrom(@ModelAttribute User user, Model model){
        model.addAttribute("user", user);
        return "result";
    }


}
