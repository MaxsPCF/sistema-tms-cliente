USE TransporteCargaPesada;
GO


/*
--Estados
-- SOLICITUD_SERVICIO
-- PENDIENTE | EN_COTIZACION | COTIZADO | APROBADO | ANULADO
-- COTIZACION
-- BORRADOR | ENVIADO | APROBADO | RECHAZADO | VENCIDO
-- ORDEN_SERVICIO
-- EMITIDA | EN_PROCESO | COMPLETADA | ANULADA
-- ORDEN_TRABAJO
-- ASIGNADA | EN_RUTA | COMPLETADA | ANULADA
-- ORDEN_VIAJE
-- PROGRAMADO | EN_CURSO | COMPLETADO | ANULADO
-- COMPROBANTE
 -- EMITIDO | PAGADO | ANULADO

 --Cambio de estados - Procesos
 -- Solicitud
 --1.- Creación/Edición-> Estado = PENDIENTE
 --2.- Envia Cotización-> Estado = EN_COTIZACION
 -- Cotización
 --1.- Creación-> Estado = BORRADOR
 --2.- Envia Cotización Para Aprobación del Cliente-> Estado = ENVIADO
        -- Cliente Aprueba -> Cotización -> Estado = APROBADO y Soliciud -> Estado = COTIZADO
        -- Cliente Rechaza -> Cotización -> Estado = RECHAZADO 
 -- Orden Servicio
 --1.- Creación/Generacíon-> Estado = EMITIDA y Soliciud -> Estado = APROBADO
 -- Orden Trabajo
 --1.- Creación/Generacíon-> Estado = ASIGNADA y Orden Servicio -> Estado = EN_PROCESO
 --Orden Viaje
 --1.- Creación/Generacíon-> Estado = PROGRAMADO
 --2.- Inicia Viaje -> Estado = EN_CURSO y Orden Trabajo -> Estado = EN_RUTA

*/



Select * from [maestros].[Numeracion]
Select * from [operaciones].[SolicitudServicio]



-- 1. Verificar que el rol 1006 existe
SELECT * FROM seguridad.Rol WHERE IdRol = 1006;

-- 2. Verificar usuario de prueba
SELECT u.IdUsuario, u.NombreUsuario, u.Email, u.Activo
FROM seguridad.Usuario u
WHERE u.Email = 'maxs.cayetano@genesis.com';

-- 3. Verificar roles asignados al usuario
SELECT u.NombreUsuario, r.IdRol, r.NombreRol, ur.Activo
FROM seguridad.UsuarioRol ur
JOIN seguridad.Usuario u ON ur.IdUsuario = u.IdUsuario
JOIN seguridad.Rol r ON ur.IdRol = r.IdRol
WHERE u.Email = 'maxs.cayetano@genesis.com';

-- 4. Verificar módulos de APP_CONDUCTOR (IdAplicacion = 3)
SELECT m.IdModulo, m.NombreModulo, m.IdModuloPadre, m.Ruta, m.Icono, m.Orden
FROM seguridad.Modulo m
WHERE m.IdAplicacion = 3 AND m.Activo = 1
ORDER BY m.Orden;

-- 5. Verificar permisos del rol Conductor
SELECT p.IdPermiso, m.NombreModulo, 
       p.PuedeVer, p.PuedeCrear, p.PuedeEditar, 
       p.PuedeEliminar, p.PuedeAprobar, p.PuedeExportar
FROM seguridad.Permiso p
JOIN seguridad.Modulo m ON p.IdModulo = m.IdModulo
WHERE p.IdRol = 1006 AND p.Activo = 1;
GO

Select * from [seguridad].[Aplicacion]
Select * from [seguridad].[Modulo]
Select * from [seguridad].[Permiso]
Select * from [seguridad].[Rol]
Select * from [seguridad].[Usuario]
Select * from [seguridad].[UsuarioRol]
Select * from [seguridad].[LogAuditoria]
Select * from [seguridad].[SesionUsuario]
go

--delete from [seguridad].[SesionUsuario]
--delete from [seguridad].[LogAuditoria]
--go

--Insert into [seguridad].[Permiso] ([IdRol], [IdModulo], [PuedeVer]) 
Select 1006, IdModulo, 1 from [seguridad].[Modulo] 
where 
Ruta is not null and 
IdAplicacion = 3

--delete from  [seguridad].[Permiso] where IdModulo = 1060

--insert [seguridad].[UsuarioRol] ([IdUsuario], [IdRol], [FechaAsignacion], [UsuarioAsigna]) values 
--('F4105E29-D9EC-4EF9-8931-459C318F753F', 1, '2026-06-04 04:58:21.8290514', '90F3EC08-AFFB-43F7-9FC6-5CA1F875A3EC'),
--('F4105E29-D9EC-4EF9-8931-459C318F753F', 1006, '2026-06-04 04:58:21.8290514', '90F3EC08-AFFB-43F7-9FC6-5CA1F875A3EC')

-- 1. ¿Hay un mismo Usuario con el mismo Rol asignado más de una vez?
SELECT IdUsuario, IdRol, COUNT(*) AS Repetidos
FROM seguridad.UsuarioRol
GROUP BY IdUsuario, IdRol
HAVING COUNT(*) > 1;

-- 2. ¿Hay una misma Persona con más de un Usuario (login) creado?
SELECT IdPersona, COUNT(*) AS CuentasDuplicadas
FROM seguridad.Usuario
WHERE IdPersona IS NOT NULL
GROUP BY IdPersona
HAVING COUNT(*) > 1;

-- 3. Auditoría de contaminación cruzada: ¿el rol Conductor o Cliente
--    ya tiene permisos asignados sobre módulos que en realidad son del Admin Web?
--    (corre esto DESPUÉS de aplicar la migración, para validar la reclasificación)
SELECT r.NombreRol, m.NombreModulo, a.Codigo AS CanalDelModulo
FROM seguridad.Permiso p
JOIN seguridad.Rol r ON r.IdRol = p.IdRol
JOIN seguridad.Modulo m ON m.IdModulo = p.IdModulo
JOIN seguridad.Aplicacion a ON a.IdAplicacion = m.IdAplicacion
WHERE r.NombreRol IN ('Conductor', 'Cliente (Portal Externo)')
  AND a.Codigo <> (CASE WHEN r.NombreRol = 'Conductor' THEN 'APP_CONDUCTOR' ELSE 'PORTAL_CLIENTE' END);

