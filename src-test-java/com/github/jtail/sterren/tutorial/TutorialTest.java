package com.github.jtail.sterren.tutorial;


import com.github.jtail.sterren.config.JerseyApplication;
import com.github.jtail.sterren.jersey.GsonMessageBodyHandler;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.function.Function;
import java.util.logging.Logger;

import static com.github.jtail.test.Admire.given;
import static com.github.jtail.testutil.FnAssert.has;
import static com.github.jtail.testutil.JsonMatchers.isJson;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.commons.lang3.StringUtils.join;
import static org.hamcrest.Matchers.is;

/**
 * This is a tutorial test. It is not completely normalized on purpose of being easier to understand
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(JerseyApplication.class)
@WebIntegrationTest(randomPort = true)
public class TutorialTest {
    private LoggingFilter loggingFilter = new LoggingFilter(Logger.getLogger(getClass().getName()), true);
    private static final Function<Response, String> ENTITY = response -> response.readEntity(String.class);
    private static final Function<Response, Integer> STATUS = Response::getStatus;

    private ClientConfig config = new ClientConfig().register(GsonMessageBodyHandler.class).register(loggingFilter);

    @Value("${local.server.port}")
    private int port;

    @Test
    public void createPoint() throws Exception {
        given(
                webTarget().path("tutorial/point/create")
        ).execute(
                post("{'x':'200', 'y':'two'}")
        ).expect(
                has(STATUS, is(HttpServletResponse.SC_BAD_REQUEST)),
                has(ENTITY, isJson(
                        "{'x':['must be less than or equal to 100.0'], 'y':['Unable to parse `two` as [double]']}"
                ))
        );
    }

    @Test
    public void createUser() throws Exception {
        given(
                webTarget().path("tutorial/user/create")
        ).execute(
                post("{" + join(
                        "'name': 'Luke',",
                        "'surname': 'Skywalker',",
                        "'emails': [",
                        "    {'address': 'luke_skywalker@jediorder.sw', 'primary': 'true'},",
                        "    {'address': 'luke_skywalker@newrepublic.sw', 'primary': 'true'}",
                        "],",
                        "'masters': ['Obi-Wan Kenobi', 'Joda']"
                ) + "}")
        ).expect(
                has(STATUS, is(HttpServletResponse.SC_BAD_REQUEST)),
                has(ENTITY, isJson("{" + join(
                        "'dateofbirth': ['may not be null'],",
                        "'emails': ['at least 3 emails are required', 'must be exactly one primary email'],",
                        "'masters': {'1':['is not a known Jedi Master']}"
                ) + "}"))
        );
    }

    private Function<WebTarget, Response> post(String json) {
        return api -> api.request(APPLICATION_JSON).post(Entity.json(json));
    }

    private WebTarget webTarget() {
        return ClientBuilder.newClient(config).target("http://localhost:" + port);
    }

}
