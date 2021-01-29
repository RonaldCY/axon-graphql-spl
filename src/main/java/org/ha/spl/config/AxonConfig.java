package org.ha.spl.config;

import org.axonframework.axonserver.connector.event.axon.AxonServerEventStore;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.common.transaction.NoTransactionManager;
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
import org.axonframework.spring.config.AxonConfiguration;
import org.ha.spl.interceptor.EventLoggingDispatchInterceptor;
import org.ha.spl.interceptor.MyCommandDispatchInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public SnapshotTriggerDefinition hospitalSnapshotTrigger(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 5);
    }
    // EventStorageEngine implementation that uses JDBC to store and fetch events.
//    @Bean
//    public JdbcEventStorageEngine eventStorageEngine(ConnectionProvider connectionProvider) {
//        return JdbcEventStorageEngine.builder()
//                .connectionProvider(connectionProvider)
//                .transactionManager(NoTransactionManager.INSTANCE)
//                .build();
//    }

    // The `InMemoryEventStorageEngine` stores each event in memory
//    @Bean
//    public EventStorageEngine storageEngine() {
//        return new InMemoryEventStorageEngine();
//    }
//
//    @Bean
//    public AxonServerEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
//        AxonServerEventStore eventStore = AxonServerEventStore.builder()
//                .storageEngine(storageEngine)
//                .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
//                .build();
////        eventStore.registerDispatchInterceptor(new EventLoggingDispatchInterceptor());
//        return eventStore;
//    }
    @Bean
    public CommandBus configureCommandBus() {
        CommandBus commandBus = SimpleCommandBus.builder().build();
        commandBus.registerDispatchInterceptor(new MyCommandDispatchInterceptor());
        return commandBus;
    }

}
