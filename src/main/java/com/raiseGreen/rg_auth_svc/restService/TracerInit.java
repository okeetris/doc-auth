package com.raiseGreen.rg_auth_svc.restService;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;

public class TracerInit {

    public TracerInit(String[] name) {
    }

    public static JaegerTracer initTracer(String service) {
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv().withLogSpans(true);
        Configuration config = new Configuration(service).withSampler(samplerConfig).withReporter(reporterConfig);
        return config.getTracer();
    }

    public static io.opentracing.Tracer main(String[] args) {

        return initTracer(args[0]);
        //  new Hello(GlobalTracer.get()).sayHello(helloTo);
    }
}
