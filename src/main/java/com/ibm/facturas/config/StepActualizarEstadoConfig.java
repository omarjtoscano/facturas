package com.ibm.facturas.config;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.ibm.facturas.batch.ActualizarExtraccionPagoTasklet;

@Configuration
public class StepActualizarEstadoConfig {

    @Bean
    public Step stepActualizarEstado(JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ActualizarExtraccionPagoTasklet actualizarExtraccionPagoTasklet) {
        return new StepBuilder("stepActualizarEstado", jobRepository)
                .tasklet(actualizarExtraccionPagoTasklet, transactionManager)
                .transactionManager(transactionManager)
                .startLimit(1)
                .build();
    }
}
