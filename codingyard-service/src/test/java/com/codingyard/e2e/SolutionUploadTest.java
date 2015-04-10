package com.codingyard.e2e;

import com.codingyard.CodingyardService;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.client.CodingyardClient;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.config.GlobalAdminConfiguration;
import com.google.common.io.Resources;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SolutionUploadTest {

    private static final String PATH_TO_YML = "e2e-codingyard.yml";
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static CodingyardClient CLIENT;

    @ClassRule
    public static final DropwizardAppRule<CodingyardConfiguration> RULE =
        new DropwizardAppRule<>(CodingyardService.class, Resources.getResource(PATH_TO_YML).getPath());

    @BeforeClass
    public static void setupOnce() throws Exception {
        final Client dwClient = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
        final URL localServer = new URL(String.format("http://localhost:%d/", RULE.getLocalPort()));
        CLIENT = new CodingyardClient(dwClient, localServer);
    }

    // TODO: Implement me
    /*
        Execute the below scenario.

        2. Upload two solutions.
        3. Get all solutions for the user.
        4. Verify that I can get the solutions.
     */
    @Test
    @Ignore("Not passing due to serialization issue...")
    public void uploadAndDownloadSolutions() {
        final String token = loginAsAdmin().readEntity(String.class);
        final Long adminId = CLIENT.getId(token).readEntity(Long.class);
        final CodingyardUser admin = CLIENT.getUser(adminId).readEntity(CodingyardUser.class);

        final TopCoderSolution firstSolution =
            new TopCoderSolution(admin, new Date(), Language.CPP, TopCoderDifficulty.HARD, TopCoderDivision.DIV1, 250L);
        final TopCoderSolution secondSolution =
            new TopCoderSolution(admin, new Date(), Language.JAVA, TopCoderDifficulty.EASY, TopCoderDivision.DIV2, 179L);
        final String firstSolutionContent = "Hello world!";
        final String secondSolutionContent = "Bye, friend...";

        uploadAndDownloadSolution(token, firstSolution, firstSolutionContent);
        uploadAndDownloadSolution(token, secondSolution, secondSolutionContent);

        // TODO: test get all solutions endpoint

    }

    private void uploadAndDownloadSolution(final String authorToken, final TopCoderSolution solution, final String content) {
        final Response uploadResponse = CLIENT.uploadTopCoderSolution(authorToken, solution, content);
        final Long solutionId = uploadResponse.readEntity(Long.class);

        final Response downloadResponse = CLIENT.getTopCoderSolution(solutionId);
        final TopCoderSolution downloaded = downloadResponse.readEntity(TopCoderSolution.class);

        assertThat(uploadResponse.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(downloadResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        System.out.println("!!!!!! Author : " + downloaded.getAuthor());
        assertThat(downloaded).isEqualTo(solution);
    }


    private Response loginAsAdmin() {
        final GlobalAdminConfiguration globalAdmin = RULE.getConfiguration().getGlobalAdminConfiguration();
        final String username = globalAdmin.getUsername();
        final String password = globalAdmin.getPassword();
        return CLIENT.login(username, password);
    }
}
