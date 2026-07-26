package com.tms.appcliente.operaciones.application.service;

import com.tms.appcliente.operaciones.application.command.CrearSolicitudCommand;
import com.tms.appcliente.operaciones.application.command.CrearSolicitudResult;
import com.tms.appcliente.operaciones.application.port.in.CrearSolicitudUseCase;
import com.tms.appcliente.operaciones.application.port.out.GeneradorNroSolicitudPort;
import com.tms.appcliente.operaciones.domain.model.SolicitudServicio;
import com.tms.appcliente.operaciones.domain.port.out.SolicitudServicioRepository;
import com.tms.appcliente.operaciones.domain.vo.DatosCarga;
import com.tms.appcliente.operaciones.domain.vo.NroSolicitud;
import com.tms.appcliente.operaciones.domain.vo.Ubicacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso "Crear Solicitud" (vertical de referencia del requerimiento 3).
 * Adaptador REST -> este servicio -> modelo de dominio puro -> puerto de
 * salida JPA. El servicio NO conoce SQL Server ni HTTP; solo coordina.
 */
@Service
public class CrearSolicitudService implements CrearSolicitudUseCase {

    private final SolicitudServicioRepository solicitudServicioRepository;
    private final GeneradorNroSolicitudPort generadorNroSolicitud;

    public CrearSolicitudService(SolicitudServicioRepository solicitudServicioRepository,
                                  GeneradorNroSolicitudPort generadorNroSolicitud) {
        this.solicitudServicioRepository = solicitudServicioRepository;
        this.generadorNroSolicitud = generadorNroSolicitud;
    }

    @Override
    @Transactional
    public CrearSolicitudResult ejecutar(CrearSolicitudCommand command) {
        NroSolicitud nroSolicitud = new NroSolicitud(generadorNroSolicitud.generarSiguiente());

        DatosCarga carga = new DatosCarga(
                command.tipoCarga(), command.pesoCarga(), command.volumenCarga(),
                command.unidadPeso(), command.descripcionCarga());

        SolicitudServicio solicitud = SolicitudServicio.crear(
                nroSolicitud,
                command.idCliente(),
                command.fechaHoraCarga(),
                command.fechaHoraEntrega(),
                new Ubicacion(command.ubigeoOrigen(), command.origenDireccion()),
                new Ubicacion(command.ubigeoDestino(), command.destinoDireccion()),
                carga,
                command.observacionCliente(),
                command.idUsuarioSolicitante());

        solicitudServicioRepository.guardar(solicitud);

        return new CrearSolicitudResult(
                solicitud.idSolicitud(),
                solicitud.nroSolicitud().valor(),
                solicitud.estado().name(),
                solicitud.fechaSolicitud());
    }
}

