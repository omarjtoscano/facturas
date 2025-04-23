package com.ibm.facturas.config;

import com.ibm.facturas.model.Factura;
import com.ibm.facturas.repository.FacturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.support.ListItemReader;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class FacturaReaderConfigTest {

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private FacturaReaderConfig facturaReaderConfig;

    private ListItemReader<Factura> reader;

    @BeforeEach
    void setUp() {
        Factura factura = new Factura();
        factura.setCodigoDeFactura("FAC-001");

        lenient().when(facturaRepository.findFacturasNoExtraidasPorFecha(anyString()))
                .thenReturn(List.of(factura));
        lenient().when(facturaRepository.findFacturasNoExtraidasHastaFecha(anyString()))
                .thenReturn(Collections.emptyList());
    }

    @Test
    @DisplayName("El reader debe cargar facturas correctamente")
    void testFacturaItemReader() throws Exception {

        reader = facturaReaderConfig.facturaItemReader(facturaRepository, LocalDate.now().toString());

        Factura result = reader.read();
        assertNotNull(result, "El reader debería devolver una factura");
        assertEquals("FAC-001", result.getCodigoDeFactura());
    }

    @Test
    @DisplayName("El reader debe devolver null cuando no hay más items")
    void testReadWhenNoMoreItems() throws Exception {
        reader = facturaReaderConfig.facturaItemReader(facturaRepository, "2023-01-01");

        Factura result = reader.read();
        assertNull(result, "El reader debería devolver null cuando no hay más items");
    }
}