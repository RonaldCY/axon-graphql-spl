package org.ha.spl.interceptor;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@NoArgsConstructor
public class MyEventHandlerInterceptor
        implements MessageHandlerInterceptor<EventMessage<?>> {

    @Override
    public Object handle(UnitOfWork<? extends EventMessage<?>> unitOfWork,
                         InterceptorChain interceptorChain) throws Exception {
        EventMessage<?> event = unitOfWork.getMessage();

        log.info("MyEventHandlerInterceptor");
        log.info("MyEventHandlerInterceptor {}", event.getMetaData().toString());
        Boolean isNullEvent = event.getMetaData().isEmpty();
//        String userId = Optional.ofNullable(event.getMetaData().get("userId"))
//                .map(uId -> (String) uId)
//                .orElseThrow();
//        if ("axonUser".equals(userId)) {
        if ( !isNullEvent ){
            return interceptorChain.proceed();
        }
        return null;
    }
}