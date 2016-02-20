/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jtail.sterren.config;

import com.github.jtail.sterren.jersey.Endpoint;
import com.github.jtail.sterren.jersey.GsonMessageBodyHandler;
import com.github.jtail.sterren.jersey.PingResource;
import com.github.jtail.sterren.tutorial.endpoints.PointResource;
import com.github.jtail.sterren.tutorial.endpoints.UserResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        property(ServerProperties.BV_FEATURE_DISABLE, true);
        register(GsonMessageBodyHandler.class);
        register(Endpoint.class);
        register(PointResource.class);
        register(UserResource.class);
        register(PingResource.class);
    }

}
