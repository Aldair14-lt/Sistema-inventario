package SistemaInventario.service;

import java.util.List;
import java.util.Optional;

import SistemaInventario.entity.Producto;

public interface ProductoService {
    Producto save(Producto producto);
    Optional<Producto> findById(Long id);
    Optional<Producto> findByCodigoBarras(String codigoBarras);
    List<Producto> findAll();
    List<Producto> findLowStock(Integer threshold);
    void deleteById(Long id);
    Optional<Producto> searchByCodigoOrNombre(String codigo);
}
