package com.ibm.facturas.batch;

import com.ibm.facturas.repository.FacturaRepository;

import lombok.extern.slf4j.Slf4j;

import com.ibm.facturas.model.Factura;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class ActualizarExtraccionPagoTasklet implements Tasklet {

    private final FacturaRepository facturaRepository;

    public ActualizarExtraccionPagoTasklet(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    @Override
    @Transactional
    public RepeatStatus execute(@NonNull StepContribution contribution,
            @NonNull ChunkContext chunkContext) {
        String fechaParam = (String) chunkContext.getStepContext()
                .getJobParameters()
                .get("fecha");

        List<Factura> facturas = obtenerFacturasPendientes(fechaParam);
        log.info("Se encontraron {} facturas pendientes para la fecha {}", facturas.size(), fechaParam);

        if (!facturas.isEmpty()) {
            log.info("Actualizando {} facturas", facturas.size());
            actualizarFacturas(facturas);
            contribution.incrementWriteCount(facturas.size());
        } else {
            log.info("No se encontraron facturas pendientes para la fecha {}", fechaParam);
        }

        return RepeatStatus.FINISHED;
    }

    private List<Factura> obtenerFacturasPendientes(String fecha) {

        if (esFechaActual(fecha)) {
            return facturaRepository.findFacturasNoExtraidasPorFecha(fecha);
        }
        return facturaRepository.findFacturasNoExtraidasHastaFecha(fecha);
    }

    private void actualizarFacturas(List<Factura> facturas) {
        List<String> ids = facturas.stream()
                .map(Factura::getCodigoDeFactura)
                .toList();
        facturaRepository.marcarFacturasComoExtraidas(ids);
    }

    private boolean esFechaActual(String fecha) {
        LocalDate fechaLocalDate = LocalDate.parse(fecha);
        return LocalDate.now().isEqual(fechaLocalDate);
    }
}
