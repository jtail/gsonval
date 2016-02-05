package com.github.jtail.sterren.jersey;


import com.github.jtail.testbeans.Bar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.stream.Stream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(JerseyApplication.class)
@WebIntegrationTest(randomPort = true)
public class JerseyTest {

    @Value("${local.server.port}")
    private int port;
    private Gson gson = new Gson();

    /**
     * Validate server is up to easily distinguish between cases when it is not working and jersey issues.
     */
    @Test
    public void contextLoads() {
        String url = "http://localhost:" + this.port + "/hello";
        System.out.println("URL=" + url);
        ResponseEntity<String> response = new TestRestTemplate().getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Ignore
    @Test
    public void nestedObjects() {
        String json = "{'unverified':{}, 'valid':{}}";
        Response response = post("foobar", json);
        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(response.getStatus()));
        String entity = response.readEntity(String.class);
        JsonObject r = new JsonParser().parse(entity).getAsJsonObject();
        Assert.assertEquals(Bar.NO_BEER_NO_BAR, r.getAsJsonObject("valid").getAsJsonPrimitive("beer").getAsString());
        Assert.assertNull(r.get("unverified"));
    }

    @Test
    public void success() {
        Response response = post("validated", new RawChicken("2", "Fat", "missing"));
        assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatus()));
    }

    @Test
    public void noValidationWithoutAnnotation() {
        Response response = post("unvalidated", new RawChicken("2", null, null));
        assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatus()));
    }

    @Test
    public void validationFailNull() {
        Response response = post("validated", new RawChicken("2", null, null));
        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(response.getStatus()));
    }

    private Response post(String path, RawChicken chicken, Pair<String, Object>... headers) {
        return post(path, gson.toJson(chicken), headers);
    }

    private Response post(String path, String data, Pair<String, Object>... headers) {
        log.info("Requesting [{}]", data);
        ClientConfig config = new ClientConfig()
                .register(GsonMessageBodyHandler.class)
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
        Client client = ClientBuilder.newClient(config);
//        client.register(new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true));
        WebTarget wt = client.target("http://localhost:" + this.port).path("test").path(path);
        Invocation.Builder builder = wt.request(APPLICATION_JSON);
        Stream.of(headers).forEach(h -> builder.header(h.getKey(), h.getValue()));
        return builder.post(Entity.json(data));
    }


    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RawChicken {
        private String legs;
        private String body;
        private String head;
    }
}
