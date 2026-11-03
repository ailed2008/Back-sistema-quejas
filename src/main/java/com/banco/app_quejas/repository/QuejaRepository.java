package com.banco.app_quejas.repository;

import com.banco.app_quejas.entity.Queja;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuejaRepository extends JpaRepository<Queja,String> {
        Optional<Queja> findByFolio(String folio);

        boolean existsByFolio(String folio);

        @Transactional
        void deleteByFolio(String folio);

}
