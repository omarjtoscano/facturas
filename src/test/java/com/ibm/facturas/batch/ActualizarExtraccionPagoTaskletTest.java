package com.ibm.facturas.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.ibm.facturas.model.Factura;
import com.ibm.facturas.repository.FacturaRepository;

@ExtendWith(MockitoExtension.class)
class ActualizarExtraccionPagoTaskletTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private StepContribution contribution;

    @Mock
    private ChunkContext chunkContext;

    @Mock
    private StepContext stepContext;

    @InjectMocks
    private ActualizarExtraccionPagoTasklet tasklet;

    @BeforeEach
    void setUp() {
        when(chunkContext.getStepContext()).thenReturn(stepContext);
    }

    @Test
    @DisplayName("Debe procesar facturas pendientes correctamente")
    void testExecuteConFacturasPendientes() throws Exception {
        Map<String, Object> jobParameters = Collections.singletonMap("fecha", "2023-01-01");
        when(stepContext.getJobParameters()).thenReturn(jobParameters);

        doNothing().when(facturaRepository).marcarFacturasComoExtraidas(anyList());

        Factura factura = new Factura();
        factura.setCodigoDeFactura("FAC-001");
        when(facturaRepository.findFacturasNoExtraidasHastaFecha(anyString()))
                .thenReturn(List.of(factura));

        RepeatStatus result = tasklet.execute(contribution, chunkContext);

        assertEquals(RepeatStatus.FINISHED, result);
        verify(facturaRepository).marcarFacturasComoExtraidas(List.of("FAC-001"));
        verify(contribution).incrementWriteCount(1);
    }

    @Test
    void testExecuteSinFacturasPendientes() throws Exception {

        Map<String, Object> jobParameters = Collections.singletonMap("fecha", "2023-01-01");
        when(stepContext.getJobParameters()).thenReturn(jobParameters);

        when(facturaRepository.findFacturasNoExtraidasHastaFecha(anyString()))
                .thenReturn(List.of());

        RepeatStatus result = tasklet.execute(contribution, chunkContext);

        assertEquals(RepeatStatus.FINISHED, result);
        verify(facturaRepository, never()).marcarFacturasComoExtraidas(anyList());
        verify(contribution, never()).incrementWriteCount(anyInt());
    }
}
