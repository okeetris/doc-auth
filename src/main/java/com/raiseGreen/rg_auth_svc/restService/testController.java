package com.raiseGreen.rg_auth_svc.restService;

import com.raiseGreen.rg_auth_svc.cos.CosExample;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {


    @GetMapping("/document")
    public String greeting( @RequestParam(value= "uid", defaultValue = "") String uid, @RequestParam(value="file") String fileName) {
        return CosExample.getPreUrl(fileName, uid);
    }

    @GetMapping("/test")
    public String test(){
        System.out.println("Working");
        return "working";
    }
}

