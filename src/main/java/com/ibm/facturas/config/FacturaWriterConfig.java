package com.ibm.facturas.config;

import com.ibm.facturas.model.Factura;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.batch.core.configuration.annotation.StepScope;

import java.time.format.DateTimeFormatter;

@Configuration
public class FacturaWriterConfig {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Bean
    @StepScope
    public FlatFileItemWriter<Factura> facturaItemWriter(
            @Value("#{jobParameters['outputFile']}") String outputFile) {

        BeanWrapperFieldExtractor<Factura> fieldExtractor = new BeanWrapperFieldExtractor<>() {
            @Override
            public Object[] extract(Factura item) {
                return new Object[] {
                        item.getCodigoDeFactura(),
                        item.getCodigoDeProveedor(),
                        item.getImporte(),
                        item.getDivisa(),
                        item.getFechaDeVencimiento().format(DATE_FORMATTER),
                        item.getIban()
                };
            }
        };

        DelimitedLineAggregator<Factura> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<Factura>()
                .name("facturaItemWriter")
                .resource(new FileSystemResource(outputFile))
                .lineAggregator(lineAggregator)
                .headerCallback(writer -> writer.write(
                        "CODIGO_FACTURA,CODIGO_PROVEEDOR,IMPORTE,DIVISA,FECHA_VENCIMIENTO,IBAN"))
                .shouldDeleteIfExists(true)
                .append(false)
                .build();
    }
}
