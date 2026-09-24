package SistemaInventario.service;

import java.util.List;
import java.util.Optional;

import SistemaInventario.entity.Categoria;

public interface CategoriaService {
    Categoria save(Categoria categoria);
    Optional<Categoria> findById(Long id);
    List<Categoria> findAll();
    void deleteById(Long id);
}
