package SistemaInventario.service.impl;

import SistemaInventario.entity.*;
import SistemaInventario.repository.*;
import SistemaInventario.service.CompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    public CompraServiceImpl(CompraRepository compraRepository,
                            ProductoRepository productoRepository,
                            MovimientoInventarioRepository movimientoRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    @Transactional
    public Compra createCompra(Compra compra) {
        Compra saved = compraRepository.save(compra);

        if (saved.getDetalleCompras() != null) {
            for (DetalleCompra dc : saved.getDetalleCompras()) {
                Producto p = productoRepository.findById(dc.getProducto().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
                int nuevaCantidad = p.getStockActual() + dc.getCantidad();
                p.setStockActual(nuevaCantidad);
                productoRepository.save(p);

                MovimientoInventario mov = new MovimientoInventario();
                mov.setProducto(p);
                mov.setTipoMovimiento("ENTRADA");
                mov.setCantidad(dc.getCantidad());
                mov.setMotivo("Compra ID: " + saved.getId());
                mov.setUsuario(saved.getUsuario());
                movimientoRepository.save(mov);
            }
        }

        return saved;
    }

    @Override
    public Optional<Compra> findById(Long id) {
        return compraRepository.findById(id);
    }

    @Override
    public List<Compra> findAll() {
        return compraRepository.findAll();
    }
}
