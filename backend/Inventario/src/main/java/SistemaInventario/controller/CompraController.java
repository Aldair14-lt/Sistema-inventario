package SistemaInventario.controller;

import SistemaInventario.entity.Compra;
import SistemaInventario.service.CompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ResponseEntity<List<Compra>> all() {
        return ResponseEntity.ok(compraService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> getOne(@PathVariable Long id) {
        return compraService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Compra> create(@RequestBody Compra compra) {
        Compra saved = compraService.createCompra(compra);
        return ResponseEntity.created(URI.create("/api/compras/" + saved.getId())).body(saved);
    }

}
