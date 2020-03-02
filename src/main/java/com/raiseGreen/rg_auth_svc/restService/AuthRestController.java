package com.raiseGreen.rg_auth_svc.restService;

import com.raiseGreen.rg_auth_svc.ConfigSwitch;
import com.raiseGreen.rg_auth_svc.cos.CosExample;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {

    public static Boolean source = false;

    @GetMapping("/document")
    public String greeting( @RequestParam(value= "uid", defaultValue = "") String uid, @RequestParam(value="file") String fileName) {

        //Change this depending on local vs hosted with config map. Hosted = true, local = false.
        ConfigSwitch.source(source);
        return CosExample.getPreUrl(fileName, uid);
    }

    @GetMapping("/test")
    public String test(){
        System.out.println("Working");
        return "working";
    }
}

