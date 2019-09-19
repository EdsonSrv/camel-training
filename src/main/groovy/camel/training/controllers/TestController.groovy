package camel.training.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/welcome")
class TestController {

    @GetMapping
    @ResponseBody
    def getPeriods(){
        "Hello get"
    }
    
}