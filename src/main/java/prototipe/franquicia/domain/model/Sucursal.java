package prototipe.franquicia.domain.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;


@Table("sucursal")
public class Sucursal {
    @Id
    private Integer id; 
    private String nombre;
    private Integer franquiciaId; 
    @Transient
    private List<Producto> productos;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getFranquiciaId() {
		return franquiciaId;
	}
	public void setFranquiciaId(Integer franquiciaId) {
		this.franquiciaId = franquiciaId;
	}
	public List<Producto> getProductos() {
		return productos;
	}
	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
    
    
}