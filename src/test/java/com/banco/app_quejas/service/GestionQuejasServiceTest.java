package com.banco.app_quejas.service;

import com.banco.app_quejas.dto.QuejaTarjetaCreditoRequest;
import com.banco.app_quejas.entity.*;
import com.banco.app_quejas.exception.ComplaintNotFoundException;
import com.banco.app_quejas.exception.DuplicateFolioException;
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
    private GestionQuejasService complaintService;

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
    @DisplayName("Should register a card complaint successfully when folio does not exist")
    void testRegisterCardComplaintSuccess() {
        when(quejaRepository.existsById(folio002)).thenReturn(false);
        when(quejaRepository.save(any(QuejaTarjetaCredito.class))).thenReturn(null);

        complaintService.registerCardComplaint(requestDto);

        verify(quejaRepository, times(1)).existsById(folio002);
        verify(quejaRepository, times(1)).save(any(QuejaTarjetaCredito.class));
    }

    @Test
    @DisplayName("Should throw DuplicateFolioException when folio already exists")
    void testRegisterComplaintDuplicateFolio() {
        when(quejaRepository.existsById(folio002)).thenReturn(true);

        assertThrows(DuplicateFolioException.class, () ->
                complaintService.registerCardComplaint(requestDto)
        );

        verify(quejaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find a complaint by folio successfully")
    void testFindByFolioSuccess() {
        Queja mock = mock(Queja.class);
        when(quejaRepository.findById(folio001)).thenReturn(Optional.of(mock));

        Optional<Queja> result = complaintService.findByFolio("Q-2026-001");

        assertThat(result).isPresent();
        verify(quejaRepository, times(1)).findById(folio001);
    }

    @Test
    @DisplayName("Should return empty Optional when folio is null or blank")
    void testFindByFolioInvalid() {
        assertThat(complaintService.findByFolio(null)).isEmpty();
        assertThat(complaintService.findByFolio("   ")).isEmpty();
        verify(quejaRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should return all complaints")
    void testFindAll() {
        when(quejaRepository.findAll()).thenReturn(List.of(mock(Queja.class), mock(Queja.class)));

        assertThat(complaintService.findAll()).hasSize(2);
        verify(quejaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should advance complaint status successfully")
    void testAdvanceStatusSuccess() {
        Queja mock = mock(Queja.class);
        when(quejaRepository.findById(folio001)).thenReturn(Optional.of(mock));
        when(quejaRepository.save(any(Queja.class))).thenReturn(mock);

        complaintService.advanceStatus("Q-2026-001", EstadoQueja.EN_INVESTIGACION);

        verify(mock, times(1)).changeStatus(EstadoQueja.EN_INVESTIGACION);
        verify(quejaRepository, times(1)).save(mock);
    }

    @Test
    @DisplayName("Should throw ComplaintNotFoundException when advancing status of non-existent folio")
    void testAdvanceStatusNotFound() {
        when(quejaRepository.findById(folio001)).thenReturn(Optional.empty());

        assertThrows(ComplaintNotFoundException.class, () ->
                complaintService.advanceStatus("Q-2026-001", EstadoQueja.EN_INVESTIGACION)
        );
    }

    @Test
    @DisplayName("Should issue ruling successfully")
    void testIssueRulingSuccess() {
        Queja mock = mock(Queja.class);
        when(quejaRepository.findById(folio001)).thenReturn(Optional.of(mock));
        when(quejaRepository.save(any(Queja.class))).thenReturn(mock);

        complaintService.issueRuling("Q-2026-001", "Ruling in favor of the client.");

        verify(mock, times(1)).issueRuling("Ruling in favor of the client.");
        verify(quejaRepository, times(1)).save(mock);
    }
}
