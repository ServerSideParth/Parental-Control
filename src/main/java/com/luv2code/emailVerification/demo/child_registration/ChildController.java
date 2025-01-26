package com.luv2code.emailVerification.demo.child_registration;

import com.luv2code.emailVerification.demo.registration.token.verificationTokenRepository;
import com.luv2code.emailVerification.demo.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/child")
public class ChildController {
    private final ChildRepository childRepository;
    private final verificationTokenRepository tokenRepository;


    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor=new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
    }
    @GetMapping("/showchildform")
    public String showChildForm(Model model,@RequestParam("token") String token){
        Child theChild=new Child();
        model.addAttribute("child",theChild);
        model.addAttribute("tokii",token);
        return "childformm";
    }


    @PostMapping("/processchildform")
    public String processForm(@Valid @ModelAttribute("child") Child theChild,BindingResult bindingResult,
                              @RequestParam("token") String token,Model model
                              ){
        var theToken=tokenRepository.findByToken(token);
        theChild.setParent(theToken.getUser());
        model.addAttribute("tokii",token);
        if(bindingResult.hasErrors()){
            model.addAttribute("condition",false);
            return "childformm";
        }
        List<Child> children=theToken.getUser().getChildren();
        if(children==null){
            children=new ArrayList<>();
        }
        else{
            children.add(theChild);
            theToken.getUser().setChildren(children);
        }
        childRepository.save(theChild);
        model.addAttribute("condition",true);
        return "childformm";
//        return "redirect:/child/showchildform?token=" + token;

    }
    @GetMapping("/showinstructionpage")
    public String showPage(){
        return "instructions";
    }
    @GetMapping("/showoffpage")
    public String showSomethingWrongPage(){
        return "oops";
    }

}
