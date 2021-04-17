package com.batch.emars.repository;

import com.batch.emars.entity.Emars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmarsRepository extends JpaRepository<Emars, Long> {
}
