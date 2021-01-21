package org.ha.spl.query.dashboard;

import org.ha.spl.api.FindHospitalQuery;
import org.ha.spl.api.HospitalCreatedEvent;
import org.ha.spl.api.HospitalDTO;
import org.ha.spl.api.WardCreatedEvent;
import org.ha.spl.query.HospitalRepository;
import org.ha.spl.query.WardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class DashboardProjector {

    private final HospitalRepository hospitalRepository;
    private final WardRepository wardRepository;

    @EventHandler
    public void on(HospitalCreatedEvent evt) {
        log.info("DashboardProjector: {}", evt);
        this.hospitalRepository.save(new Hospital(evt.getHospCode(), new ArrayList<>()));
    }


    @EventHandler
    public void on(WardCreatedEvent evt) {
        log.info("DashboardProjector: {}", evt);
        Hospital hospital = this.hospitalRepository.findById(evt.getHospCode()).orElseThrow();
        this.wardRepository.save(new Ward(evt.getHospCode() + ":" + evt.getWardCode(), hospital, evt.getWardCode()));
    }

    @QueryHandler
    public HospitalDTO handle(FindHospitalQuery query) {
        Hospital hospital = this.hospitalRepository.findById(query.getHospCode()).orElseThrow();
        List<String> wards = hospital.getWards().stream().map(Ward::getWardCode).collect(Collectors.toList());
        return new HospitalDTO(hospital.getHospCode(), wards);
    }
}
