package org.ha.spl.query.hospital;

import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.ha.spl.api.HospitalCreatedEvent;
import org.ha.spl.api.ListHospitalQuery;
import org.ha.spl.api.NotificationQuery;
import org.ha.spl.api.WardAddedEvent;
import org.ha.spl.query.HospitalViewRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class HospitalProjector {

    private final HospitalViewRepository repository;
    private final QueryUpdateEmitter emitter;

    @EventHandler
    public void on(HospitalCreatedEvent evt) {
        log.info("HospitalProjector: {}", evt);
        this.repository.save(new HospitalView(evt.getHospCode(), new ArrayList<>()));
        emitter.emit(NotificationQuery.class, q -> true , evt.getHospCode());
    }

    @EventHandler
    public void on(WardAddedEvent evt) {
        log.info("HospitalProjector: {}", evt);
        HospitalView hospitalView = this.repository.findById(evt.getHospCode()).orElseThrow();
        hospitalView.getWards().add(evt.getWardCode());
    }

    @QueryHandler
    public List<HospitalView> handle(ListHospitalQuery query) {
        return this.repository.findAll();
    }
}
