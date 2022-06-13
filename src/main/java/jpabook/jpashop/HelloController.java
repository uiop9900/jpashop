package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("Hello")
    public String hello(Model model) {  //model에 담아 view로 넘길 것
        model.addAttribute("data", "hello!");
        return "hello";
    }
}
