package org.ha.spl.config;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.axonserver.connector.AxonServerConnectionManager;
import org.axonframework.axonserver.connector.event.axon.AxonServerEventStore;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.config.Configurer;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.SequenceEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.ha.spl.interceptor.EventLoggingDispatchInterceptor;
import org.ha.spl.interceptor.MyCommandDispatchInterceptor;
import org.ha.spl.interceptor.MyEventHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AxonConfig {

    @Bean
    public SnapshotTriggerDefinition hospitalSnapshotTrigger(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 5);
    }

    @Bean
    public EventStore eventStore(AxonServerConfiguration axonServerConfiguration,
                                 AxonConfiguration configuration,
                                 AxonServerConnectionManager axonServerConnectionManager,
                                 Serializer snapshotSerializer,
                                 @Qualifier("eventSerializer") Serializer eventSerializer) {

        EventStore eventStore =  AxonServerEventStore.builder()
                .configuration(axonServerConfiguration)
                .platformConnectionManager(axonServerConnectionManager)
                .snapshotSerializer(snapshotSerializer)
                .eventSerializer(eventSerializer)
                .upcasterChain(configuration.upcasterChain())
                .build();
        eventStore.registerDispatchInterceptor(new EventLoggingDispatchInterceptor());
        return eventStore;
    }
//
//    @Bean
//    public CommandBus configureCommandBus() {
//        CommandBus commandBus = SimpleCommandBus.builder().build();
//        commandBus.registerDispatchInterceptor(new MyCommandDispatchInterceptor());
//        return commandBus;
//    }

    // Default all processors to subscribing mode.
//    @Autowired
//    public void configure(EventProcessingConfigurer config) {
//        config.usingSubscribingEventProcessors();
//    }

//    @Autowired
//    public void configureEventProcessing(Configurer configurer) {
//        log.info("Configured event processing");
//        configurer.eventProcessing().usingTrackingEventProcessors();
//        configurer.eventProcessing()
//                .registerTrackingEventProcessor("my-tracking-processor")
//                .registerHandlerInterceptor("my-tracking-processor",
//                        configuration -> new MyEventHandlerInterceptor());
//    }

}
