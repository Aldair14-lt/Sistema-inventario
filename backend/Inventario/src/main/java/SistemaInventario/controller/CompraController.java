package SistemaInventario.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SistemaInventario.entity.Compra;
import SistemaInventario.service.CompraService;

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
    @org.springframework.security.access.prepost.PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ALMACENERO')")
    public ResponseEntity<Compra> create(@RequestBody Compra compra) {
        Compra saved = compraService.createCompra(compra);
        return ResponseEntity.created(URI.create("/api/compras/" + saved.getId())).body(saved);
    }

}
