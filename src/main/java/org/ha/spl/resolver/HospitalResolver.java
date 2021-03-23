package org.ha.spl.resolver;

import io.leangen.graphql.annotations.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.ha.spl.api.*;
import org.ha.spl.query.hospital.HospitalView;
import org.ha.spl.query.ward.WardView;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@Slf4j
@Component
public class HospitalResolver {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private QueryGateway queryGateway;

    @Autowired
    private ReactorQueryGateway reactiveQueryGateway;

    private Flux<Object> flux;

    @PostConstruct
    public void setup() {
        flux = reactiveQueryGateway.subscriptionQuery(new NotificationQuery(), ResponseTypes.instanceOf(Object.class));
        flux.subscribe();
    }

    @GraphQLMutation
    @GraphQLNonNull
    public CompletableFuture<Object> createHospital(@GraphQLArgument(name = "input") @GraphQLNonNull CreateHospitalDTO dto) {
        log.info("createHospital {}", dto);
        return this.commandGateway.send(
                new CreateHospitalCommand(dto.getHospCode()))
                .exceptionally(exception -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.toString());
                });
    }

    @GraphQLMutation
    public CompletableFuture<AddWardDTO> addWard(@GraphQLNonNull String hospCode, @GraphQLArgument(name = "input") @GraphQLNonNull AddWardDTO dto) {
        log.info("addWard {}", dto);
        return this.commandGateway.send(
                new AddWardCommand(hospCode, dto.getWardCode())).thenApply(obj -> {
            return dto;
        }).exceptionally(exception -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.toString());
        });
    }

    @GraphQLMutation
    public CompletableFuture<AddBedDTO> addBed(@GraphQLNonNull String hospCode, @GraphQLNonNull String wardCode, @GraphQLArgument(name = "input") @GraphQLNonNull AddBedDTO dto) {
        log.info("addBed {} {} {}", hospCode, wardCode, dto);
        return this.commandGateway.send(
                new AddBedCommand(hospCode + ":" + wardCode, dto.getBedNum())).thenApply(obj -> {
            return dto;
        }).exceptionally(exception -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.toString());
                });
    }

    @GraphQLMutation
    public CompletableFuture<CheckInPatientDTO> checkIn(@GraphQLNonNull String hospCode, @GraphQLNonNull String wardCode, @GraphQLArgument(name = "input") @GraphQLNonNull CheckInPatientDTO dto) {
        log.info("checkIn {} {} {}", hospCode, wardCode, dto);
        return this.commandGateway.send(
                new CheckInWardCommand(dto.getHkid(), hospCode, wardCode, dto.getBedNum())).thenApply(obj -> {
            return dto;
        }).exceptionally(exception -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.toString());
                });
    }

    @GraphQLQuery
    public CompletableFuture<HospitalDTO> getHospital(@GraphQLNonNull String hospCode) {
        log.info("getHospital {}", hospCode);
        return this.queryGateway.query(
                new FindHospitalQuery(hospCode), ResponseTypes.instanceOf(HospitalDTO.class))
                .exceptionally(exception -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.toString());
                });
    }

    @GraphQLQuery
    public CompletableFuture<WardView> getWard(@GraphQLNonNull String hospCode, @GraphQLNonNull String wardCode) {
        log.info("getWard {} {}", hospCode, wardCode);
        return this.queryGateway.query(
                new FindWardQuery(hospCode, wardCode), ResponseTypes.instanceOf(WardView.class))
                .exceptionally(exception -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.toString());
                });
    }

    @GraphQLQuery
    public CompletableFuture<List<HospitalView>> listHospital() {
        log.info("listHospital");

//        reactiveQueryGateway.query("listH", HospitalView.class);
        return this.queryGateway.query(
                new ListHospitalQuery(), ResponseTypes.multipleInstancesOf(HospitalView.class));
    }

    @GraphQLSubscription
    public Publisher<Object> notification() {
        return Flux.from(flux);
    }

}
