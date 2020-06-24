package com.raiseGreen.rg_auth_svc.restService;

import com.raiseGreen.rg_auth_svc.ConfigSwitch;
import com.raiseGreen.rg_auth_svc.cos.CosExample;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableMap;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
public class AuthRestController {

    public static Boolean source = false;


    @GetMapping("/document")
    public String greeting( @RequestParam(value= "uid", defaultValue = "") String uid, @RequestParam(value="file") String fileName) {

        //Change this depending on local vs hosted with config map. Hosted = true, local = false.
        //System.out.print(System.getProperty("catalina.base"));

        ConfigSwitch.source(source);

        return CosExample.getPreUrl(fileName, uid);
    }

    @GetMapping("/random")
    public String random(@QueryParam("helloTo") String helloTo, @Context HttpHeaders httpHeaders) throws Exception {

        //Span span = Tracing.startServerSpan(tracer, httpHeaders, "format");
        String test[] = new String[1];
        test[0] = "tristan";
        String test1[] = new String[1];
        test1[0] = "avi";
        //Hello.main(test);
        //App.main(test);
        HelloActive.main(test1);

        return "random hit";
    }

    @GetMapping("/test")
    public String test(){
        System.out.println("Working");
        return "working";
    }


}

