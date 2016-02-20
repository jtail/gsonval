/*
 * Copyright 2012-2014 the original author or authors.
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

package com.github.jtail.sterren.tutorial.endpoints;

import com.github.jtail.sterren.tutorial.validators.JediMaster;
import com.github.jtail.sterren.tutorial.validators.OnlyOnePrimary;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Controller
@Slf4j
@Path("tutorial/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Path("create")
    public User createUser(@Valid User input) {
        log.info("Processing: " + input.toString());
        return input;
    }

    @Data
    public static class User {
        @NotNull
        private String name;

        @NotNull
        private String surname;

        @NotNull
        private String dateofbirth;

        @NotNull
        @Valid
        @Size(min = 3, message = "at least 3 emails are required")
        @OnlyOnePrimary(message = "must be exactly one primary email")
        private List<Email> emails;

        @Valid
        private List<@JediMaster(message = "is not a known Jedi Master") String> masters;
    }

    @Data
    public static class Email {

        private String address;
        private boolean primary;
    }


}
