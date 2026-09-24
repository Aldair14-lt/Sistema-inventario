package SistemaInventario.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_barras", length = 50, unique = true)
    private String codigoBarras;

    @Column(name = "nombre", length = 150, nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio_compra", precision = 10, scale = 2, nullable = false)
    @NotNull
    private BigDecimal precioCompra;

    @Column(name = "precio_venta", precision = 10, scale = 2, nullable = false)
    @NotNull
    private BigDecimal precioVenta;

    @Column(name = "stock_actual", nullable = false)
    @NotNull
    @Min(0)
    private Integer stockActual = 0;

    @Column(name = "stock_minimo", nullable = false)
    @NotNull
    @Min(0)
    private Integer stockMinimo = 5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    @NotNull
    private Proveedor proveedor;

    @Column(name = "estado")
    private Boolean estado = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.stockActual == null) this.stockActual = 0;
        if (this.stockMinimo == null) this.stockMinimo = 5;
    }

}
