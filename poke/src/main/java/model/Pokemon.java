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

	@ManyToMany(cascade = {CascadeType.MERGE})
	@JoinTable(
	    name = "pokemon_movimiento",
	    joinColumns = @JoinColumn(name = "pokemon_id"),
	    inverseJoinColumns = @JoinColumn(name = "movimiento_id")
	)
	private List<Movimiento> movimientos = new ArrayList<>();

	@Transient
	private List<MoveWrapper> moves;
	
	public static class MoveWrapper {
		public MoveInfo move;
		public static class MoveInfo {
		public String name;
		public String url;
		}
		}
	public void addMovimiento(Movimiento m) {
		movimientos.add(m);
	}

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public int getBase_experience() { return base_experience; }
	public void setBase_experience(int v) { this.base_experience = v; }
	public int getHeight() { return height; }
	public void setHeight(int h) { this.height = h; }
	public int getWeight() { return weight; }
	public void setWeight(int w) { this.weight = w; }
	public List<Movimiento> getMovimientos() { return movimientos; }
	public List<MoveWrapper> getMoves() { return moves; }
	public void setMoves(List<MoveWrapper> m) { this.moves = m; }
	@Override
	public String toString() {
	return "Pokemon[id=" + id + ", name=" + name +
	 ", exp=" + base_experience + "]";
	}
}