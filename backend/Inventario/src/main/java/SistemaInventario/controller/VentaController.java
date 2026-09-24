package SistemaInventario.controller;

import SistemaInventario.entity.Venta;
import SistemaInventario.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> all() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getOne(@PathVariable Long id) {
        return ventaService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Venta> create(@RequestBody Venta venta) {
        Venta saved = ventaService.createVenta(venta);
        return ResponseEntity.created(URI.create("/api/ventas/" + saved.getId())).body(saved);
    }

}
