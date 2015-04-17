package com.codingyard;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CodingyardFrontend extends Application<CodingyardFrontendConfiguration> {

    public static void main(String[] args) throws Exception {
        new CodingyardFrontend().run(args);
    }

    @Override
    public void initialize(final Bootstrap<CodingyardFrontendConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/angularApp", "/", "angularApp/index.html"));
    }

    @Override
    public void run(final CodingyardFrontendConfiguration configuration, final Environment environment) throws Exception {

        // Dropwizard refuses to start if there is no resource classs unless we disable jersey.
        environment.jersey().disable();
    }

}
