package com.mrcho.ws.repository;

import com.mrcho.ws.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	boolean existsByCodigo(String codigo);
}