package SistemaInventario.service;

import java.util.List;
import java.util.Optional;

import SistemaInventario.entity.Venta;

public interface VentaService {
    Venta createVenta(Venta venta);
    Optional<Venta> findById(Long id);
    List<Venta> findAll();
}
