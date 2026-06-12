package main.java.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "Pokemon")
public class Pokemon {
	public static final String URL_API = "https://pokeapi.co/api/v2/pokemon/";
	@Id
	private long id;
	@Column
	private String name;
	@Column
	private int base_experience;
	@Column
	private int height; // en decímetros
	@Column
	private int weight; // en hectogramos
// Relación con Movimiento (ver Paso 7)

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "pokemon_movimiento", joinColumns = @JoinColumn(name = "pokemon_id"), inverseJoinColumns = @JoinColumn(name = "movimiento_id"))
	private List<Movimiento> movimientos = new ArrayList<>();

	public void addMovimiento(Movimiento m) {
		movimientos.add(m);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBase_experience() {
		return base_experience;
	}

	public void setBase_experience(int base_experience) {
		this.base_experience = base_experience;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}
	
	@Override
	public String toString() {
	return "Pokemon[id=" + id + ", name=" + name +
	 ", exp=" + base_experience + "]";
	}
}