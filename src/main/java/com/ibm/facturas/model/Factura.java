package com.ibm.facturas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @Column(name = "codigo_de_factura", length = 20)
    @Size(max = 20, message = "El código de factura no puede exceder 20 caracteres")
    @NotBlank(message = "El código de factura no puede estar vacío")
    private String codigoDeFactura;

    @Column(name = "codigo_de_proveedor", length = 7)
    @Size(min = 7, max = 7, message = "El código de proveedor debe tener exactamente 7 caracteres")
    @NotBlank(message = "El código de proveedor no puede estar vacío")
    private String codigoDeProveedor;

    @Column(name = "importe", precision = 15, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "El importe debe ser mayor que 0")
    @NotNull(message = "El importe no puede ser nulo")
    private BigDecimal importe;

    @Column(name = "divisa", length = 3)
    @Size(min = 3, max = 3, message = "La divisa debe tener exactamente 3 caracteres")
    @Pattern(regexp = "^[A-Z]{3}$", message = "La divisa debe estar en mayúsculas")
    private String divisa;

    @Column(name = "fecha_de_vencimiento")
    @NotNull(message = "La fecha de vencimiento no puede ser nula")
    private LocalDateTime fechaDeVencimiento;

    @Column(name = "estado", length = 20)
    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    @Column(name = "extraccion_pago")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 1, message = "El valor máximo es 1")
    private int extraccionPago;

    @Column(name = "IBAN", length = 20)
    @Size(max = 20, message = "El IBAN no puede exceder 20 caracteres")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[a-zA-Z0-9]{0,16}$", message = "Formato de IBAN inválido")
    private String iban;
}
