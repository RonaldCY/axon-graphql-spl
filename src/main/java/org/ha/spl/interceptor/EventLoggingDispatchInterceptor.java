package org.ha.spl.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
@Slf4j
@AllArgsConstructor
public class EventLoggingDispatchInterceptor
        implements MessageDispatchInterceptor<EventMessage<?>> {

    @Override
    public BiFunction<Integer, EventMessage<?>, EventMessage<?>> handle(
            List<? extends EventMessage<?>> messages) {

        log.info("BiFunction");
        return (index, event) -> {
            log.info("Publishing event: [{}].", event);
            return event;
        };
    }
}