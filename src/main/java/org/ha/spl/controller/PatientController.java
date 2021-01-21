package org.ha.spl.controller;

import org.ha.spl.api.CreatePatientCommand;
import org.ha.spl.api.CreatePatientDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping(value = "/patient")
@AllArgsConstructor
public class PatientController {

    private final CommandGateway commandGateway;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<Object> createPatient(@RequestBody CreatePatientDTO dto) {
        log.info("createPatient {}", dto);
        return this.commandGateway.send(
                new CreatePatientCommand(dto.getHkid(), dto.getName()))
                .exceptionally(exception -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already exists");
                });
    }
}
