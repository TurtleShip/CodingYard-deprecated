package com.codingyard.resources.solution;

import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.manager.TopCoderSolutionManager;
import com.codingyard.resources.ResourceTest;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.Path;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TopCoderSolutionResourceTest extends ResourceTest {

    private static final String ROOT = "/solution/topcoder";

    private static TopCoderSolutionManager tcManager = mock(TopCoderSolutionManager.class);
    private static Path filePath = mock(Path.class);

    private TopCoderSolution solution;
    private String content;
    private Form form;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
        .addProvider(AuthFactory.binder(chainedAuthFactory))
        .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
        .addResource(new TopCoderSolutionResource(userManager, tcManager))
        .build();

    @BeforeClass
    public static void setupOnce() {
        resources.getJerseyTest().client().register(HttpAuthenticationFeature.basic("", ""));
    }

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();
        solution = new TopCoderSolution(member, new Date(), Language.CPP, TopCoderDifficulty.HARD, TopCoderDivision.DIV1, 256L);
        solution.setId(1L);
        content = "Hello world!";
        form = new Form()
            .param("division", solution.getDivision().name())
            .param("difficulty", solution.getDifficulty().name())
            .param("problem_number", solution.getProblemNumber().toString())
            .param("language", solution.getLanguage().name())
            .param("content", content);

        when(tcManager.findById(solution.getId())).thenReturn(Optional.of(solution));
        when(tcManager.save(any(CodingyardUser.class), any(String.class), any(TopCoderDivision.class), any(TopCoderDifficulty.class),
            any(Long.class), any(Language.class), any(Date.class))).thenReturn(filePath);
        when(filePath.toString()).thenReturn("I am so fake path");
        when(tcManager.load(solution)).thenReturn(Lists.newArrayList(content));

        doNothing().when(userManager).saveSolution(any(CodingyardUser.class), any(TopCoderSolution.class));
        doNothing().when(userManager).flush();
    }


    @Test
    public void guestCannotUploadSolution() {
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .request(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION_HEADER, bearerToken(guest.getToken().getValue()))
            .post(Entity.form(form));

        assertThat(response.getStatus()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void memberCanUploadSolution() {
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .request(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION_HEADER, bearerToken(member.getToken().getValue()))
            .post(Entity.form(form));

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        verify(userManager).saveSolution(any(CodingyardUser.class), any(TopCoderSolution.class));
    }

    @Test
    public void getSolutionShouldReturnExistingSolutions() {
        final TopCoderSolution actual = resources.getJerseyTest()
            .target(ROOT)
            .path(solution.getId().toString())
            .request(MediaType.APPLICATION_JSON)
            .get(TopCoderSolution.class);

        assertThat(actual).isEqualTo(solution);
    }

    @Test
    public void getContentShouldReturnContentForExistingSolutions() {
        final String actual = resources.getJerseyTest()
            .target(ROOT)
            .path(solution.getId().toString())
            .path("content")
            .request(MediaType.TEXT_PLAIN)
            .get(String.class);

        assertThat(actual).isEqualTo(content);
    }
}
