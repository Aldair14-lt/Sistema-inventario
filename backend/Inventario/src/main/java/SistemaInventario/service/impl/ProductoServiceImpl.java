package SistemaInventario.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import SistemaInventario.entity.Producto;
import SistemaInventario.repository.ProductoRepository;
import SistemaInventario.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Optional<Producto> findByCodigoBarras(String codigoBarras) {
        return productoRepository.findByCodigoBarras(codigoBarras);
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> findLowStock(Integer threshold) {
        return productoRepository.findByStockActualLessThanEqual(threshold);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public Optional<Producto> searchByCodigoOrNombre(String codigo) {
        if(codigo == null) return Optional.empty();
        Optional<Producto> byCodigo = productoRepository.findByCodigoBarras(codigo);
        if(byCodigo.isPresent()) return byCodigo;
        // fallback: search all and match by name or id
        try{
            long id = Long.parseLong(codigo);
            Optional<Producto> byId = productoRepository.findById(id);
            if(byId.isPresent()) return byId;
        }catch(Exception e){ }
        List<Producto> all = productoRepository.findAll();
        return all.stream().filter(p-> p.getNombre()!=null && p.getNombre().toLowerCase().contains(codigo.toLowerCase())).findFirst();
    }
}
