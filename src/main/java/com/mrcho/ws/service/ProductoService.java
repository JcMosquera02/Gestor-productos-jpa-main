package com.mrcho.ws.service;

import com.mrcho.ws.dto.ProductoDTO;
import com.mrcho.ws.model.Producto;
import com.mrcho.ws.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {
	@Autowired
	private ProductoRepository productoRepository;

	public List<ProductoDTO> getAllProductos() {
		return productoRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public ProductoDTO getProductoById(Long id) {
		Producto producto = productoRepository.findById(id).orElse(null);
		return producto != null ? convertToDTO(producto) : null;
	}

	public ProductoDTO saveProducto(ProductoDTO productoDTO) {
		if (productoDTO.getNombre() == null || productoDTO.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede estar vacío");
		}
		if (productoDTO.getPrecio() == null || productoDTO.getPrecio() < 0) {
			throw new IllegalArgumentException("El precio no puede ser negativo");
		}
		if (productoDTO.getCantidad() == null || productoDTO.getCantidad() < 0) {
			throw new IllegalArgumentException("La cantidad no puede ser negativa");
		}
		if (productoDTO.getCodigo() == null || productoDTO.getCodigo().trim().isEmpty()) {
			throw new IllegalArgumentException("El código no puede estar vacío");
		}
		if (productoRepository.existsByCodigo(productoDTO.getCodigo())
				&& (productoDTO.getId() == null || !productoRepository.existsById(productoDTO.getId()))) {
			throw new IllegalArgumentException("El código ya existe");
		}

		Producto producto = convertToEntity(productoDTO);
		Producto savedProducto = productoRepository.save(producto);
		return convertToDTO(savedProducto);
	}

	public void deleteProducto(Long id) {
		if (!productoRepository.existsById(id)) {
			throw new IllegalArgumentException("Producto no encontrado");
		}
		productoRepository.deleteById(id);
	}

	public ProductoDTO updateProducto(Long id, ProductoDTO productoDTO) {
		if (!productoRepository.existsById(id)) {
			throw new IllegalArgumentException("Producto no encontrado");
		}
		if (productoDTO.getNombre() == null || productoDTO.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede estar vacío");
		}
		if (productoDTO.getPrecio() == null || productoDTO.getPrecio() < 0) {
			throw new IllegalArgumentException("El precio no puede ser negativo");
		}
		if (productoDTO.getCantidad() == null || productoDTO.getCantidad() < 0) {
			throw new IllegalArgumentException("La cantidad no puede ser negativa");
		}
		if (productoDTO.getCodigo() == null || productoDTO.getCodigo().trim().isEmpty()) {
			throw new IllegalArgumentException("El código no puede estar vacío");
		}
		if (productoRepository.existsByCodigo(productoDTO.getCodigo())
				&& !productoDTO.getCodigo().equals(productoRepository.findById(id).get().getCodigo())) {
			throw new IllegalArgumentException("El código ya existe");
		}

		Producto existingProducto = productoRepository.findById(id).orElse(null);
		if (existingProducto != null) {
			existingProducto.setCodigo(productoDTO.getCodigo());
			existingProducto.setNombre(productoDTO.getNombre());
			existingProducto.setDescripcion(productoDTO.getDescripcion());
			existingProducto.setPrecio(productoDTO.getPrecio());
			existingProducto.setCantidad(productoDTO.getCantidad());
			existingProducto.setEstado(productoDTO.getEstado());
			Producto updatedProducto = productoRepository.save(existingProducto);
			return convertToDTO(updatedProducto);
		}
		return null;
	}

	private ProductoDTO convertToDTO(Producto producto) {
		ProductoDTO dto = new ProductoDTO();
		dto.setId(producto.getId());
		dto.setCodigo(producto.getCodigo());
		dto.setNombre(producto.getNombre());
		dto.setDescripcion(producto.getDescripcion());
		dto.setPrecio(producto.getPrecio());
		dto.setCantidad(producto.getCantidad());
		dto.setEstado(producto.getEstado());
		return dto;
	}

	private Producto convertToEntity(ProductoDTO dto) {
		Producto producto = new Producto();
		producto.setId(dto.getId());
		producto.setCodigo(dto.getCodigo());
		producto.setNombre(dto.getNombre());
		producto.setDescripcion(dto.getDescripcion());
		producto.setPrecio(dto.getPrecio());
		producto.setCantidad(dto.getCantidad());
		producto.setEstado(dto.getEstado());
		return producto;
	}
}