package com.tms.appcliente.operaciones.application.port.in;

import com.tms.appcliente.operaciones.application.command.CrearSolicitudCommand;
import com.tms.appcliente.operaciones.application.command.CrearSolicitudResult;

public interface CrearSolicitudUseCase {
    CrearSolicitudResult ejecutar(CrearSolicitudCommand command);
}

