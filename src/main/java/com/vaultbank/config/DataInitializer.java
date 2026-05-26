package com.vaultbank.config;

import com.vaultbank.model.Administrador;
import com.vaultbank.model.TasaInteres;
import com.vaultbank.model.enums.Rol;
import com.vaultbank.model.enums.TipoTasa;
import com.vaultbank.repository.TasaInteresRepository;
import com.vaultbank.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final TasaInteresRepository tasaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                            TasaInteresRepository tasaRepository,
                            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tasaRepository = tasaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        crearAdminDefecto();
        crearTasasDefecto();
    }

    private void crearAdminDefecto() {
        if (usuarioRepository.findByEmail("admin@vaultbank.com").isPresent()) return;

        Administrador admin = Administrador.builder()
                .nombre("Admin")
                .apellido("VaultBank")
                .email("admin@vaultbank.com")
                .password(passwordEncoder.encode("Admin2026!"))
                .cedula("000000000")
                .telefono("3000000000")
                .rol(Rol.ADMIN)
                .nivelAcceso(10)
                .permisosSistema("FULL")
                .build();

        usuarioRepository.save(admin);
    }

    private void crearTasasDefecto() {
        if (tasaRepository.count() > 0) return;

        tasaRepository.save(TasaInteres.builder()
                .tipoTasa(TipoTasa.AHORRO)
                .valor(0.0425)
                .descripcion("Tasa de interés cuentas de ahorro EA")
                .build());

        tasaRepository.save(TasaInteres.builder()
                .tipoTasa(TipoTasa.CDT)
                .valor(0.0975)
                .descripcion("Tasa nominal CDT a 360 días")
                .build());

        tasaRepository.save(TasaInteres.builder()
                .tipoTasa(TipoTasa.PRESTAMO_PERSONAL)
                .valor(0.0185)
                .descripcion("Tasa mensual préstamos personales")
                .build());

        tasaRepository.save(TasaInteres.builder()
                .tipoTasa(TipoTasa.PRESTAMO_HIPOTECARIO)
                .valor(0.0095)
                .descripcion("Tasa mensual crédito hipotecario")
                .build());

        tasaRepository.save(TasaInteres.builder()
                .tipoTasa(TipoTasa.SOBREGIRO)
                .valor(0.0290)
                .descripcion("Tasa mensual cupo de sobregiro")
                .build());
    }
}
