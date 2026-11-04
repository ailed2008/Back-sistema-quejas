package com.banco.app_quejas.service;

import com.banco.app_quejas.dto.QuejaTarjetaCreditoRequest;
import com.banco.app_quejas.entity.*;
import com.banco.app_quejas.repository.QuejaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GestionQuejasServiceTest {

    @Mock
    private QuejaRepository quejaRepository;

    @InjectMocks
    private GestionQuejasService gestionQuejasService;

    private QuejaTarjetaCreditoRequest requestDto;
    private Folio folio001;
    private Folio folio002;

    @BeforeEach
    void setUp() {
        folio001 = new Folio("Q-2026-001");
        folio002 = new Folio("Q-2026-002");

        requestDto = new QuejaTarjetaCreditoRequest(
                "Q-2026-002",
                "CLI-82793",
                new BigDecimal("2500.00"),
                Prioridad.MEDIA,
                "CDMX - Sucursal Polanco",
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Debe registrar una queja de tarjeta exitosamente cuando el folio no existe")
    void testRegistrarQuejaTarjetaExitoso() {
        when(quejaRepository.existsById(folio002)).thenReturn(false);
        when(quejaRepository.save(any(QuejaTarjetaCredito.class))).thenReturn(null);

        gestionQuejasService.registrarQuejaTarjeta(requestDto);

        verify(quejaRepository, times(1)).existsById(folio002);
        verify(quejaRepository, times(1)).save(any(QuejaTarjetaCredito.class));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si el folio de la queja ya existe")
    void testRegistrarQuejaFolioDuplicado() {
        when(quejaRepository.existsById(folio002)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gestionQuejasService.registrarQuejaTarjeta(requestDto)
        );

        assertThat(exception.getMessage()).contains("Ya existe una queja registrada con el folio");
        verify(quejaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe buscar una queja por folio exitosamente")
    void testBuscarPorFolioExitoso() {
        Queja quejaMock = mock(Queja.class);
        when(quejaRepository.findById(folio001)).thenReturn(Optional.of(quejaMock));

        Optional<Queja> resultado = gestionQuejasService.buscarPorFolio("Q-2026-001");

        assertThat(resultado).isPresent();
        verify(quejaRepository, times(1)).findById(folio001);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío si el folio es nulo o en blanco al buscar")
    void testBuscarPorFolioInvalido() {
        Optional<Queja> resultadoNulo = gestionQuejasService.buscarPorFolio(null);
        Optional<Queja> resultadoBlanco = gestionQuejasService.buscarPorFolio("   ");

        assertThat(resultadoNulo).isEmpty();
        assertThat(resultadoBlanco).isEmpty();
        verify(quejaRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Debe obtener la lista de todas las quejas registradas")
    void testObtenerTodas() {
        when(quejaRepository.findAll()).thenReturn(List.of(mock(Queja.class), mock(Queja.class)));

        List<Queja> lista = gestionQuejasService.obtenerTodas();

        assertThat(lista).hasSize(2);
        verify(quejaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe avanzar el estado de una queja existente correctamente")
    void testAvanzarEstadoExitoso() {
        Queja quejaMock = mock(Queja.class);
        when(quejaRepository.findById(folio001)).thenReturn(Optional.of(quejaMock));
        when(quejaRepository.save(any(Queja.class))).thenReturn(quejaMock);

        gestionQuejasService.avanzarEstado("Q-2026-001", EstadoQueja.EN_INVESTIGACION);

        verify(quejaRepository, times(1)).findById(folio001);
        verify(quejaMock, times(1)).cambiarEstado(EstadoQueja.EN_INVESTIGACION);
        verify(quejaRepository, times(1)).save(quejaMock);
    }

    @Test
    @DisplayName("Debe emitir dictamen en una queja existente correctamente")
    void testEmitirDictamenExitoso() {
        Queja quejaMock = mock(Queja.class);
        when(quejaRepository.findById(folio001)).thenReturn(Optional.of(quejaMock));
        when(quejaRepository.save(any(Queja.class))).thenReturn(quejaMock);

        gestionQuejasService.emitirDictamen("Q-2026-001", "Dictamen favorable al cliente");

        verify(quejaRepository, times(1)).findById(folio001);
        verify(quejaMock, times(1)).emitirDictamen("Dictamen favorable al cliente");
        verify(quejaRepository, times(1)).save(quejaMock);
    }
}
