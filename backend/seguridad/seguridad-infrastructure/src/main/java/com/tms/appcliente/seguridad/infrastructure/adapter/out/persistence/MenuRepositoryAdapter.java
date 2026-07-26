package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence;

import com.tms.appcliente.seguridad.domain.model.Modulo;
import com.tms.appcliente.seguridad.domain.model.Permiso;
import com.tms.appcliente.seguridad.domain.port.out.MenuRepository;
import com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity.ModuloJpaEntity;
import com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity.PermisoJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Adaptador de salida para el árbol de menús. seguridad.Modulo se auto-referencia
 * (IdModuloPadre), así que aquí se ensambla el árbol Composite (N niveles, el
 * requerimiento C solo exige mostrar hasta 3) a partir de la lista plana que
 * devuelve SQL Server, y se consolidan (OR lógico) los permisos cuando el
 * usuario tiene más de un rol con acceso al mismo módulo.
 */
@Component
public class MenuRepositoryAdapter implements MenuRepository {

    private final ModuloJpaRepository moduloJpaRepository;
    private final PermisoJpaRepository permisoJpaRepository;

    public MenuRepositoryAdapter(ModuloJpaRepository moduloJpaRepository,
                                  PermisoJpaRepository permisoJpaRepository) {
        this.moduloJpaRepository = moduloJpaRepository;
        this.permisoJpaRepository = permisoJpaRepository;
    }

    @Override
    public List<Modulo> buscarArbolModulosPorAplicacion(String codigoAplicacion) {
        List<ModuloJpaEntity> planos = moduloJpaRepository.buscarPorAplicacion(codigoAplicacion);
        return construirArbol(planos);
    }

    @Override
    public Map<Integer, Permiso> buscarPermisosPorRoles(List<Integer> idsRoles, String codigoAplicacion) {
        if (idsRoles == null || idsRoles.isEmpty()) {
            return Map.of();
        }
        Map<Integer, Permiso> consolidado = new HashMap<>();
        for (PermisoJpaEntity p : permisoJpaRepository.buscarPorRolesYAplicacion(idsRoles, codigoAplicacion)) {
            Permiso permiso = new Permiso(
                    p.getIdModulo(),
                    Boolean.TRUE.equals(p.getPuedeVer()),
                    Boolean.TRUE.equals(p.getPuedeCrear()),
                    Boolean.TRUE.equals(p.getPuedeEditar()),
                    Boolean.TRUE.equals(p.getPuedeEliminar()),
                    Boolean.TRUE.equals(p.getPuedeAprobar()),
                    Boolean.TRUE.equals(p.getPuedeExportar()));
            consolidado.merge(p.getIdModulo(), permiso, Permiso::combinar);
        }
        return consolidado;
    }

    private List<Modulo> construirArbol(List<ModuloJpaEntity> planos) {
        // orden ascendente ya viene de la consulta; LinkedHashMap preserva ese orden.
        Map<Integer, List<ModuloJpaEntity>> hijosPorPadre = new LinkedHashMap<>();
        for (ModuloJpaEntity m : planos) {
            hijosPorPadre.computeIfAbsent(m.getIdModuloPadre(), k -> new ArrayList<>()).add(m);
        }
        List<ModuloJpaEntity> raices = hijosPorPadre.getOrDefault(null, List.of());
        return raices.stream().map(r -> aModulo(r, hijosPorPadre)).toList();
    }

    private Modulo aModulo(ModuloJpaEntity entity, Map<Integer, List<ModuloJpaEntity>> hijosPorPadre) {
        List<Modulo> hijos = hijosPorPadre.getOrDefault(entity.getIdModulo(), List.of()).stream()
                .map(h -> aModulo(h, hijosPorPadre))
                .toList();
        return new Modulo(
                entity.getIdModulo(),
                entity.getIdModuloPadre(),
                entity.getNombreModulo(),
                entity.getIcono(),
                entity.getRuta(),
                entity.getOrden(),
                hijos);
    }
}

