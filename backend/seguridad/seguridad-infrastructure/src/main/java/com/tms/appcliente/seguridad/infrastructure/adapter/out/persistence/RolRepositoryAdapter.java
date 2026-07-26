package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence;

import com.tms.appcliente.seguridad.domain.model.Rol;
import com.tms.appcliente.seguridad.domain.port.out.RolRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RolRepositoryAdapter implements RolRepository {

    private final RolJpaRepository jpaRepository;

    public RolRepositoryAdapter(RolJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Rol> buscarRolesActivosDeUsuario(UUID idUsuario) {
        return jpaRepository.buscarRolesActivosDeUsuario(idUsuario).stream()
                .map(r -> new Rol(r.getIdRol(), r.getIdAplicacion(), r.getNombreRol(),
                        Boolean.TRUE.equals(r.getEsAdmin()), Boolean.TRUE.equals(r.getActivo())))
                .toList();
    }
}

