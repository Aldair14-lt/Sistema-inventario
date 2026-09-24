package SistemaInventario.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SistemaInventario.entity.Usuario;
import SistemaInventario.repository.UsuarioRepository;
import SistemaInventario.security.JwtUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authManager, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authManager = authManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        var user = usuarioRepository.findByEmail(email).orElseThrow();
        String token = jwtUtils.generateToken(email);
        // Return user roles as simple array of names to frontend
        String roleName = user.getRol() != null ? user.getRol().getNombre() : "USER";
        return ResponseEntity.ok(Map.of("token", token, "usuario", user, "rol", roleName));
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario saved = usuarioRepository.save(usuario);
        return ResponseEntity.created(URI.create("/api/auth/register/" + saved.getId())).body(saved);
    }
}
