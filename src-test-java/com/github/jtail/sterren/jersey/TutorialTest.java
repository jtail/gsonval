package com.github.jtail.sterren.jersey;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(JerseyApplication.class)
@WebIntegrationTest(randomPort = true)
public class TutorialTest extends AbstractJerseyTest {

    @Test
    public void createPoint() {
        Response response = post("tutorial/point/create", "{'x':'200', 'y':'two'}");
        assertThat(HttpStatus.valueOf(response.getStatus()), is(HttpStatus.BAD_REQUEST));
        String entity = response.readEntity(String.class);
        JsonObject r = new JsonParser().parse(entity).getAsJsonObject();
        assertThat(r.entrySet().size(), is(2));
    }


}
