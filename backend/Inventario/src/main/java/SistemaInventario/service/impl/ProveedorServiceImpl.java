package SistemaInventario.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import SistemaInventario.entity.Proveedor;
import SistemaInventario.repository.ProveedorRepository;
import SistemaInventario.service.ProveedorService;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Optional<Proveedor> findById(Long id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public List<Proveedor> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        proveedorRepository.deleteById(id);
    }
}
