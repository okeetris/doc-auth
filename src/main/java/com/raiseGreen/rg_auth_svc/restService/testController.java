package com.raiseGreen.rg_auth_svc.restService;

import com.raiseGreen.rg_auth_svc.cos.CosExample;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {


    @GetMapping("/document")
    public String greeting( @RequestParam(value= "uid", defaultValue = "") String uid) {
        System.out.println(uid);
        String result = CosExample.getPreUrl("mou-20200221170307-8f2QnFhk.pdf", uid);
        //System.out.println(uid);
        System.out.println(result);
        return result;
    }

    @GetMapping("/test")
    public String test(){
        System.out.println("Working");

        return "working";
    }
}

