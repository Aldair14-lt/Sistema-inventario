package SistemaInventario.service;

import java.util.List;
import java.util.Optional;

import SistemaInventario.entity.Proveedor;

public interface ProveedorService {
    Proveedor save(Proveedor proveedor);
    Optional<Proveedor> findById(Long id);
    List<Proveedor> findAll();
    void deleteById(Long id);
}
