package org.ha.spl.aggregate;

import lombok.AllArgsConstructor;
import org.ha.spl.api.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;

@Aggregate(snapshotTriggerDefinition = "hospitalSnapshotTrigger")
@Slf4j
@NoArgsConstructor
public class HospitalAggregate {

    private final List<String> wards = new ArrayList<>();
    @AggregateIdentifier
    private String hospCode;

    @CommandHandler
    public HospitalAggregate(CreateHospitalCommand cmd) {
        log.info("Received {}", cmd);
        AggregateLifecycle.apply(new HospitalCreatedEvent(cmd.getHospCode()));
    }

    @CommandHandler
    public void handle(AddWardCommand cmd) throws Exception {
        log.info("Received {}", cmd);
        if (this.wards.contains(cmd.getWardCode())) {
            throw new WardExistException();
        }
        AggregateLifecycle.createNew(WardAggregate.class, () -> new WardAggregate(cmd.getHospCode(), cmd.getWardCode()));
        AggregateLifecycle.apply(new WardAddedEvent(cmd.getHospCode(), cmd.getWardCode()));
    }

    @EventSourcingHandler
    private void on(HospitalCreatedEvent evt) {
        log.info("Received {}", evt);
        this.hospCode = evt.getHospCode();
    }

    @EventSourcingHandler
    private void on(WardAddedEvent evt) {
        log.info("Received {}", evt);
        this.wards.add(evt.getWardCode());
    }
}
