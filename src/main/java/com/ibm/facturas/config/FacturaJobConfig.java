package com.ibm.facturas.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacturaJobConfig {

    @Bean
    public Job facturaJob(JobRepository jobRepository,
            @Qualifier("stepLecturaEscritura") Step stepLecturaEscritura,
            @Qualifier("stepActualizarEstado") Step stepActualizarEstado) {

        return new JobBuilder("facturaJob", jobRepository)
                .start(stepLecturaEscritura)
                .next(stepActualizarEstado)
                .build();
    }
}
