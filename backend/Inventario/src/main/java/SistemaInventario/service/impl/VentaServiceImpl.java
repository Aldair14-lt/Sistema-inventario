package SistemaInventario.service.impl;

import SistemaInventario.entity.*;
import SistemaInventario.repository.*;
import SistemaInventario.service.VentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    public VentaServiceImpl(VentaRepository ventaRepository,
                           ProductoRepository productoRepository,
                           MovimientoInventarioRepository movimientoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    @Transactional
    public Venta createVenta(Venta venta) {
        Venta saved = ventaRepository.save(venta);

        if (saved.getDetalleVentas() != null) {
            for (DetalleVenta dv : saved.getDetalleVentas()) {
                Producto p = productoRepository.findById(dv.getProducto().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
                int nuevaCantidad = p.getStockActual() - dv.getCantidad();
                p.setStockActual(nuevaCantidad);
                productoRepository.save(p);

                MovimientoInventario mov = new MovimientoInventario();
                mov.setProducto(p);
                mov.setTipoMovimiento("SALIDA");
                mov.setCantidad(dv.getCantidad());
                mov.setMotivo("Venta ID: " + saved.getId());
                mov.setUsuario(saved.getUsuario());
                movimientoRepository.save(mov);
            }
        }

        return saved;
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }
}
