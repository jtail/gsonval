package com.github.jtail.sterren.jersey;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.stream.Stream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Slf4j
public class AbstractJerseyTest {
    @Value("${local.server.port}")
    private int port;

    @SuppressWarnings("unchecked")
    protected Response post(String path, String data, Pair<String, String>... headers) {
        log.info("Requesting [{}]", data);
        ClientConfig config = new ClientConfig().register(GsonMessageBodyHandler.class).property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
        Client client = ClientBuilder.newClient(config);
        // client.register(new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true));
        WebTarget wt = client.target("http://localhost:" + this.port).path(path);
        Invocation.Builder builder = wt.request(APPLICATION_JSON);
        Stream.of(headers).forEach(h -> builder.header(h.getKey(), h.getValue()));
        return builder.post(Entity.json(data));
    }
}
