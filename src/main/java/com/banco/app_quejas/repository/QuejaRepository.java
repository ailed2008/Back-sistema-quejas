package com.banco.app_quejas.repository;

import com.banco.app_quejas.entity.Folio;
import com.banco.app_quejas.entity.Queja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuejaRepository extends JpaRepository<Queja, Folio> {
}
