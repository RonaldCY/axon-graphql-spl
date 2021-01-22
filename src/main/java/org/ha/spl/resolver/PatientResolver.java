package org.ha.spl.resolver;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.ha.spl.api.CreatePatientCommand;
import org.ha.spl.api.CreatePatientDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

@Controller
@Slf4j
@AllArgsConstructor
public class PatientResolver {

    private final CommandGateway commandGateway;

    @GraphQLMutation
    public CompletableFuture<Object> createPatient(@GraphQLArgument(name = "input") @GraphQLNonNull CreatePatientDTO dto) {
        log.info("createPatient {}", dto);
        return this.commandGateway.send(
                new CreatePatientCommand(dto.getHkid(), dto.getName()))
                .exceptionally(exception -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already exists");
                });
    }
}
