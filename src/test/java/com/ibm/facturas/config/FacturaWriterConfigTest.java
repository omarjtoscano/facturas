package com.ibm.facturas.config;

import com.ibm.facturas.model.Factura;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.file.FlatFileItemWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



class FacturaWriterConfigTest {

    private final FacturaWriterConfig config = new FacturaWriterConfig();

    @Test
    void testFacturaItemWriter_configuracionCorrecta() throws Exception {
        String outputPath = "target/test-output/facturas.csv";
        FlatFileItemWriter<Factura> writer = config.facturaItemWriter(outputPath);

        writer.afterPropertiesSet();

        Factura factura = new Factura();
        factura.setCodigoDeFactura("FAC123");
        factura.setCodigoDeProveedor("PROV456");
        factura.setImporte(BigDecimal.valueOf(1200.50));
        factura.setDivisa("EUR");
        factura.setFechaDeVencimiento(LocalDateTime.now());
        factura.setIban("ES9820385778983000760236");

        File file = new File(outputPath);
        if (file.exists()) {
            assertTrue(file.delete());
        }

        writer.open(new org.springframework.batch.item.ExecutionContext());
        writer.write(new Chunk<>(List.of(factura)));
        writer.close();

        assertTrue(file.exists(), "El archivo debería haberse creado.");

        try (var reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String header = reader.readLine();
            String data = reader.readLine();

            assertEquals("CODIGO_FACTURA,CODIGO_PROVEEDOR,IMPORTE,DIVISA,FECHA_VENCIMIENTO,IBAN", header);
            assertEquals("FAC123,PROV456,1200.5,EUR,2025-04-22,ES9820385778983000760236", data);
        }
    }
}
