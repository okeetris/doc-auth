package com.raiseGreen.rg_auth_svc.restService;

import com.google.common.collect.ImmutableMap;

import com.lightstep.tracer.jre.JRETracer;
import com.raiseGreen.rg_auth_svc.Tracing;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


public class HelloActive {

    //private final Tracer tracer;
    private final JRETracer tracer;
    private final OkHttpClient client;


    private HelloActive(/*Tracer tracer,*/ JRETracer lightTracer) {
        //this.tracer = tracer;
        this.tracer = lightTracer;
        this.client = new OkHttpClient();
    }

    private String getHttp(int port, String path, String param, String value) {
        try {
            HttpUrl url = new HttpUrl.Builder().scheme("http").host("localhost").port(port).addPathSegment(path)
                    .addQueryParameter(param, value).build();
            Request.Builder requestBuilder = new Request.Builder().url(url);

            Tags.SPAN_KIND.set(tracer.activeSpan(), Tags.SPAN_KIND_CLIENT);
            Tags.HTTP_METHOD.set(tracer.activeSpan(), "GET");
            Tags.HTTP_URL.set(tracer.activeSpan(), url.toString());
            tracer.inject(tracer.activeSpan().context(), Format.Builtin.HTTP_HEADERS, Tracing.requestBuilderCarrier(requestBuilder));

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                throw new RuntimeException("Bad HTTP result: " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sayHello(String helloTo) {
        Span span = tracer.buildSpan("say-hello").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            span.setTag("hello-to", helloTo);
            String helloStr = formatString(helloTo);
            printHello(helloStr);
        } finally{
            span.finish();
        }
    }



    private String formatString(String helloTo) {
        Span span = tracer.buildSpan("formatString").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            String helloStr = getHttp(8081, "format", "helloTo", helloTo);
            span.log(ImmutableMap.of("event", "string-format", "value", helloStr));
            return helloStr;
        } finally {
            span.finish();
        }
    }

    private void printHello(String helloStr) {
        Span span = tracer.buildSpan("printHello").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            getHttp(8082, "publish", "helloStr", helloStr);
            span.log(ImmutableMap.of("event", "println"));
        } finally {
            span.finish();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expecting one argument");
        }

        //Tracer tracer1 = new Tracer;

        String helloTo = args[0];
        try (JRETracer tracer1 = Tracing.initLight("doc-auth")) {
            new HelloActive(tracer1).sayHello(helloTo);
        }
        /*try (JaegerTracer tracerJ = TracerInit.initTracer("doc-auth")) {
            new Hello(tracerJ).sayHello(helloTo);
        }*/
    }
}
