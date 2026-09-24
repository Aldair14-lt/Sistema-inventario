package SistemaInventario.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import SistemaInventario.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var opt = usuarioRepository.findByEmail(username);
        var u = opt.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        var role = u.getRol() != null ? u.getRol().getNombre() : "USER";
        return new User(u.getEmail(), u.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}
