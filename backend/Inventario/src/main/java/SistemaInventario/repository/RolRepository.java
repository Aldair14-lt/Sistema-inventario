package SistemaInventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SistemaInventario.entity.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
}
