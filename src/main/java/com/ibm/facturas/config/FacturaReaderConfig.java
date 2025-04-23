package com.ibm.facturas.config;

import com.ibm.facturas.model.Factura;
import com.ibm.facturas.repository.FacturaRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.configuration.annotation.StepScope;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Configuration
public class FacturaReaderConfig {

    @Bean("facturaItemReader")
    @StepScope
    public ListItemReader<Factura> facturaItemReader(
            FacturaRepository facturaRepository,
            @Value("#{jobParameters['fecha']}") String fechaParam) {

        LocalDate fecha = parseFecha(fechaParam);
        log.info("Leyendo facturas pendientes para la fecha: {}", fecha);

        List<Factura> facturas = fecha.isEqual(LocalDate.now())
                ? facturaRepository.findFacturasNoExtraidasPorFecha(fechaParam)
                : facturaRepository.findFacturasNoExtraidasHastaFecha(fechaParam);

        return new ListItemReader<>(facturas);
    }

    private LocalDate parseFecha(String fechaParam) {
        return fechaParam != null ? LocalDate.parse(fechaParam) : LocalDate.now();
    }
}