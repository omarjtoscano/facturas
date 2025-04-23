package com.ibm.facturas.config;

import com.ibm.facturas.model.Factura;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepLecturaEscrituraConfig {

    @Bean
    public Step stepLecturaEscritura(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("facturaItemReader") ItemReader<Factura> reader,
            @Qualifier("facturaItemWriter") FlatFileItemWriter<Factura> writer) {

        return new StepBuilder("stepLecturaEscritura", jobRepository)
                .<Factura, Factura>chunk(100, transactionManager)
                .reader(reader)
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(5)
                .build();
    }
}