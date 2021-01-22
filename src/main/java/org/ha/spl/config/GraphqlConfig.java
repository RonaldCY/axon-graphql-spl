package org.ha.spl.config;

import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import lombok.AllArgsConstructor;
import org.ha.spl.resolver.HospitalResolver;
import org.ha.spl.resolver.PatientResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class GraphqlConfig {

    private final HospitalResolver hospitalResolver;
    private final PatientResolver patientResolver;

    @Bean
    GraphQLSchema schema() {
        return new GraphQLSchemaGenerator()
                .withBasePackages("org.ha.spl")
                .withOperationsFromSingleton(this.hospitalResolver)
                .withOperationsFromSingleton(this.patientResolver)
                .generate();
    }
}