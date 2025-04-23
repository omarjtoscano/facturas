package com.ibm.facturas.repository;

import com.ibm.facturas.model.Factura;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, String> {

        @Query(value = "SELECT * FROM FACTURAS f WHERE TRUNC(f.FECHA_DE_VENCIMIENTO) = TO_DATE(:fecha, 'YYYY-MM-DD') AND f.EXTRACCION_PAGO = 0", nativeQuery = true)
        List<Factura> findFacturasNoExtraidasPorFecha(@Param("fecha") String fecha);

        @Query(value = "SELECT * FROM FACTURAS f WHERE TRUNC(f.FECHA_DE_VENCIMIENTO) <= TO_DATE(:fecha, 'YYYY-MM-DD') AND f.EXTRACCION_PAGO = 0", nativeQuery = true)
        List<Factura> findFacturasNoExtraidasHastaFecha(@Param("fecha") String fecha);

        @Transactional
        @Modifying
        @Query("UPDATE Factura f SET f.extraccionPago = 1 WHERE f.codigoDeFactura IN :ids")
        void marcarFacturasComoExtraidas(@Param("ids") List<String> ids);

}
