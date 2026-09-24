package SistemaInventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SistemaInventario.entity.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
}
