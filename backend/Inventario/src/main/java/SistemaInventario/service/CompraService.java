package SistemaInventario.service;

import java.util.List;
import java.util.Optional;

import SistemaInventario.entity.Compra;

public interface CompraService {
    Compra createCompra(Compra compra);
    Optional<Compra> findById(Long id);
    List<Compra> findAll();
}
