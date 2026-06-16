package com.mrcho.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrcho.ws.dto.ProductoDTO;
import com.mrcho.ws.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
	@Autowired
	private ProductoService productoService;

	@GetMapping
	public List<ProductoDTO> getAllProductos() {
		return productoService.getAllProductos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
		ProductoDTO productoDTO = productoService.getProductoById(id);
		if (productoDTO != null) {
			return ResponseEntity.ok(productoDTO);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> createProducto(@RequestBody ProductoDTO productoDTO) {
		try {
			ProductoDTO savedProducto = productoService.saveProducto(productoDTO);
			return ResponseEntity.ok(savedProducto);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
		try {
			ProductoDTO updatedProducto = productoService.updateProducto(id, productoDTO);
			if (updatedProducto != null) {
				return ResponseEntity.ok(updatedProducto);
			}
			return ResponseEntity.notFound().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
		try {
			productoService.deleteProducto(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}