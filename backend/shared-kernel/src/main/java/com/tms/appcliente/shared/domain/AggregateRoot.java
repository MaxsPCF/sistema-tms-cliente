package com.tms.appcliente.shared.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Marcador base para raíces de agregado en el modelo de dominio puro
 * (sin anotaciones de persistencia: eso pertenece a los adaptadores JPA
 * de infraestructura, nunca al dominio).
 */
public abstract class AggregateRoot<ID extends Serializable> {

    protected final ID id;

    protected AggregateRoot(ID id) {
        this.id = Objects.requireNonNull(id, "El identificador del agregado no puede ser nulo");
    }

    public ID id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AggregateRoot<?> that)) return false;
        return Objects.equals(id, that.id) && getClass().equals(o.getClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
}

