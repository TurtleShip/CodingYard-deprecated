package com.codingyard;

import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.resources.HelloResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CodingyardService extends Application<CodingyardConfiguration> {

    public static void main(String[] args) throws Exception {
        new CodingyardService().run(args);
    }

    @Override
    public void initialize(final Bootstrap<CodingyardConfiguration> bootstrap) {
    }

    @Override
    public void run(final CodingyardConfiguration configuration,
                    final Environment environment) throws Exception {
        environment.jersey().register(new HelloResource());
    }
}
