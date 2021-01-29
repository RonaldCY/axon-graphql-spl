package org.ha.spl.aggregate;

import org.ha.spl.api.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashMap;
import java.util.Map;

@Aggregate
@Slf4j
@NoArgsConstructor
public class WardAggregate {

    private final Map<Integer, Bed> beds = new HashMap<>();
    @AggregateIdentifier
    private String wardId;
    private String wardCode;

    public WardAggregate(String hospCode, String wardCode) {
        AggregateLifecycle.apply(new WardCreatedEvent(hospCode, wardCode));
    }

    @CommandHandler
    public WardAggregate (AddBedCommand cmd) throws Exception {
        log.info("Received {}", cmd);
        if (this.beds.containsKey(cmd.getBedNum())) {
            throw new BedExistException();
        }
        AggregateLifecycle.apply(new BedAddedEvent(cmd.getWardId(), cmd.getBedNum()));
    }


    @CommandHandler
    public void on(CheckInPatientCommand cmd) throws Exception {
        log.info("Received {}", cmd);
        Bed bed = this.beds.get(cmd.getBedNum());
        if (bed == null) {
            throw new BedNotFoundException();
        }
        if (bed.getHkid() != null) {
            throw new BedOccupiedException();
        }
        AggregateLifecycle.apply(new PatientCheckedInEvent(cmd.getWardId(), cmd.getBedNum(), cmd.getHkid(), cmd.getName()));
    }

    @EventSourcingHandler
    private void on(WardCreatedEvent evt) {
        log.info("Received {}", evt);
        this.wardId = evt.getHospCode() + ":" + evt.getWardCode();
    }

    @EventSourcingHandler
    private void on(BedAddedEvent evt) {
        log.info("Received {}", evt);
        this.beds.put(evt.getBedNum(), new Bed(evt.getBedNum(), null, null));
    }

    @EventSourcingHandler
    private void on(PatientCheckedInEvent evt) {
        log.info("Received {}", evt);
        Bed bed = this.beds.get(evt.getBedNum());
        bed.setHkid(evt.getHkid());
        bed.setName(evt.getName());
    }

    @Data
    @AllArgsConstructor
    public class Bed {
        private Integer BedNum;
        private String hkid;
        private String name;
    }
}
