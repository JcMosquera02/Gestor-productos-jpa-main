const API_URL = 'http://localhost:8080/api/productos';

async function cargarProductos() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) {
            throw new Error('Error al cargar productos');
        }
        const productos = await response.json();
        const tableBody = document.getElementById('productosTable');
        tableBody.innerHTML = '';
        productos.forEach(producto => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${producto.id}</td>
                <td>${producto.codigo}</td>
                <td>${producto.nombre}</td>
                <td>${producto.precio}</td>
                <td>${producto.cantidad}</td>
                <td>${producto.estado}</td>
                <td>
                    <button onclick="cargarProducto(${producto.id})">Editar</button>
                    <button onclick="eliminarProducto(${producto.id})">Eliminar</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error:', error);
        alert('Error al cargar productos: ' + error.message);
    }
}

async function cargarProducto(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) {
            throw new Error('Error al cargar producto');
        }
        const producto = await response.json();
        document.getElementById('id').value = producto.id;
        document.getElementById('codigo').value = producto.codigo;
        document.getElementById('nombre').value = producto.nombre;
        document.getElementById('descripcion').value = producto.descripcion || '';
        document.getElementById('precio').value = producto.precio;
        document.getElementById('cantidad').value = producto.cantidad;
        document.getElementById('estado').value = producto.estado;
    } catch (error) {
        console.error('Error:', error);
        alert('Error al cargar producto: ' + error.message);
    }
}

async function guardarProducto() {
    const id = document.getElementById('id').value;
    const producto = {
        id: id ? parseInt(id) : null,
        codigo: document.getElementById('codigo').value,
        nombre: document.getElementById('nombre').value,
        descripcion: document.getElementById('descripcion').value,
        precio: parseFloat(document.getElementById('precio').value),
        cantidad: parseInt(document.getElementById('cantidad').value),
        estado: document.getElementById('estado').value
    };

    try {
        let response;
        if (id) {
            response = await fetch(`${API_URL}/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(producto)
            });
        } else {
            response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(producto)
            });
        }

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage || 'Error al guardar producto');
        }

        limpiarFormulario();
        cargarProductos();
    } catch (error) {
        console.error('Error:', error);
        alert('Error al guardar producto: ' + error.message);
    }
}

async function eliminarProducto(id) {
    if (!confirm('¿Estás seguro de eliminar este producto?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage || 'Error al eliminar producto');
        }

        limpiarFormulario();
        cargarProductos();
    } catch (error) {
        console.error('Error:', error);
        alert('Error al eliminar producto: ' + error.message);
    }
}

function limpiarFormulario() {
    document.getElementById('id').value = '';
    document.getElementById('codigo').value = '';
    document.getElementById('nombre').value = '';
    document.getElementById('descripcion').value = '';
    document.getElementById('precio').value = '';
    document.getElementById('cantidad').value = '';
    document.getElementById('estado').value = 'Activo';
}

window.onload = function() {
    cargarProductos();
};