package com.codingyard.e2e;

import com.codingyard.CodingyardService;
import com.codingyard.config.CodingyardConfiguration;
import com.google.common.io.Resources;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;

public class SolutionUploadTest {

    private static final String PATH_TO_YML = "e2e-codingyard.yml";

    @ClassRule
    public static final DropwizardAppRule<CodingyardConfiguration> RULE =
        new DropwizardAppRule<>(CodingyardService.class, Resources.getResource(PATH_TO_YML).getPath());

    // TODO: Implement me
}
