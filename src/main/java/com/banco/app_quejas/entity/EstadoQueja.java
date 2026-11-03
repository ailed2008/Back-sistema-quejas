package com.banco.app_quejas.entity;

public enum EstadoQueja {
    REGISTRADA{
        @Override
        public EstadoQueja transicionarA(EstadoQueja nuevoEstado){
            if(nuevoEstado == EN_INVESTIGACION || nuevoEstado == CERRADA){
                return nuevoEstado;
            }
            throw new IllegalStateException("Transición no permitida desde REGISTRADA hacia " + nuevoEstado);

        }
    },
    EN_INVESTIGACION{
        @Override
        public EstadoQueja transicionarA(EstadoQueja nuevoEstado){
            if(nuevoEstado == DICTAMINADA){
                return nuevoEstado;
            }
            throw new IllegalStateException("Transición no permitida desde EN_INVESTIGACION hacia " + nuevoEstado);
        }
    },
    DICTAMINADA {
        @Override
        public EstadoQueja transicionarA(EstadoQueja nuevoEstado) {
            if (nuevoEstado == CERRADA) {
                return nuevoEstado;
            }
            throw new IllegalStateException("Transición no permitida desde DICTAMINADA hacia " + nuevoEstado);
        }
    },
    CERRADA {
        @Override
        public EstadoQueja transicionarA(EstadoQueja nuevoEstado) {
            throw new IllegalStateException("Una queja CERRADA no puede cambiar de estado.");
        }
    };

    public abstract EstadoQueja transicionarA(EstadoQueja nuevoEstado);
}
