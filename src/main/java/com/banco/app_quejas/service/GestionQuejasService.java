package com.banco.app_quejas.service;

import com.banco.app_quejas.dto.QuejaTarjetaCreditoRequest;
import com.banco.app_quejas.entity.EstadoQueja;
import com.banco.app_quejas.entity.Queja;
import com.banco.app_quejas.entity.QuejaTarjetaCredito;
import com.banco.app_quejas.repository.QuejaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GestionQuejasService {

    private final QuejaRepository quejaRepository;

    public GestionQuejasService(QuejaRepository quejaRepository){
        this.quejaRepository =quejaRepository;
    }
    @Transactional
    public  void registrarQuejaTarjeta(QuejaTarjetaCreditoRequest dto){
        Objects.requireNonNull(dto, "El DTO de la queja no puede ser nulo");
        if(quejaRepository.existsById(dto.folio())){
            throw  new IllegalArgumentException("Ya existe una queja registrada con el folio: " + dto.folio());
        }

        QuejaTarjetaCredito queja = new QuejaTarjetaCredito(
          dto.folio(),
          LocalDate.now(),
          dto.numCliente(),
          dto.montoReclamado(),
          dto.prioridad(),
          dto.lugarSuceso(),
          dto.horaTransaccion()
        );

        //Validaciones de negocio
        queja.validarRequisitosFormales();

        //Guardar en bd
        quejaRepository.save(queja);
    }

    public Optional<Queja> buscarPorFolio(String folio){
        if(folio == null || folio.isBlank()){
            return Optional.empty();
        }
        return quejaRepository.findById(folio);
    }

    public List<Queja> obtenerTodas(){
        return quejaRepository.findAll();
    }

    @Transactional
    public void avanzarEstado(String folio, EstadoQueja nuevoEstado){
        Queja queja = buscarPorFolio(folio)
                .orElseThrow(()-> new IllegalArgumentException(("No se encontró la queja con el folio: " + folio)));
        queja.cambiarEstado(nuevoEstado);
        quejaRepository.save(queja);
    }

    @Transactional
    public void emitirDictamen(String folio, String textoDictamen) {
        Queja queja = buscarPorFolio(folio)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la queja con el folio: " + folio));

        queja.emitirDictamen(textoDictamen);
        quejaRepository.save(queja);
    }

    public void eliminarQueja(String folio){
        //Verificamos si existe el folio
        boolean existe = quejaRepository.existsById(folio);
        if(!existe){
            throw new RuntimeException("No se encontró ninguna queja con el folio: "+ folio);
        }

        quejaRepository.deleteByFolio(folio);
    }
}
