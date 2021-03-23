package org.ha.spl.query.ward;

import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.ha.spl.api.BedAddedEvent;
import org.ha.spl.api.FindWardQuery;
import org.ha.spl.api.NotificationQuery;
import org.ha.spl.api.WardCreatedEvent;
import org.ha.spl.query.WardViewRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class WardProjector {

    private final WardViewRepository repository;
    private final QueryUpdateEmitter emitter;

    @EventHandler
    public void on(WardCreatedEvent evt) {
        log.info("WardProjector: {}", evt);
        this.repository.save(new WardView(evt.getHospCode() + ":" + evt.getWardCode(), evt.getWardCode()));
        emitter.emit(NotificationQuery.class, q -> true , evt.getHospCode() + ":" + evt.getWardCode());
    }

    @EventHandler
    public void on(BedAddedEvent evt) {
        log.info("WardProjector: {}", evt);
        emitter.emit(NotificationQuery.class, q -> true , evt.getWardId() + ":" + evt.getBedNum());
    }

    @QueryHandler
    public WardView handle(FindWardQuery query) {
        return this.repository.findById(query.getHospCode() + ":" + query.getWardCode()).orElseThrow();
    }
}
