package org.ha.spl.aggregate;

import org.ha.spl.api.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
@NoArgsConstructor
public class PatientAggregate {

    @AggregateIdentifier
    private String hkid;
    private String name;

    private String hospCode;
    private String wardCode;
    private Integer bedNum;

    @CommandHandler
    public PatientAggregate(CreatePatientCommand cmd) {
        AggregateLifecycle.apply(new PatientCreatedEvent(cmd.getHkid(), cmd.getName()));
    }

    @CommandHandler
    public void on(CheckInWardCommand cmd) throws PatientCheckedInException {
        if (this.hospCode != null || this.wardCode != null || this.bedNum != null) {
            throw new PatientCheckedInException();
        }
        AggregateLifecycle.apply(new WardCheckedInEvent(cmd.getHkid(), this.name, cmd.getHospCode(), cmd.getWardCode(), cmd.getBedNum()));
    }

    @CommandHandler
    public void on(CancelCheckInWardCommand cmd) throws PatientNotCheckedInException {
        if (this.hospCode == null || this.wardCode == null || this.bedNum == null) {
            throw new PatientNotCheckedInException();
        }
        AggregateLifecycle.apply(new CheckInWardCancelledEvent(cmd.getHkid()));
    }

    @EventSourcingHandler
    private void on(PatientCreatedEvent evt) {
        log.info("Received {}", evt);
        this.hkid = evt.getHkid();
        this.name = evt.getName();
    }

    @EventSourcingHandler
    private void on(WardCheckedInEvent evt) {
        log.info("Received {}", evt);
        this.hospCode = evt.getHospCode();
        this.wardCode = evt.getWardCode();
        this.bedNum = evt.getBedNum();
    }

    @EventSourcingHandler
    private void on(CheckInWardCancelledEvent evt) {
        log.info("Received {}", evt);
        this.hospCode = null;
        this.wardCode = null;
        this.bedNum = null;
    }
}
