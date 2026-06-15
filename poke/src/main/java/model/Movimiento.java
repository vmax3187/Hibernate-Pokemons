package main.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="Movimiento")
public class Movimiento {
	public static final String URL_API ="https://pokeapi.co/api/v2/move/";
	
	
	@Id
	private long id;
	
	@Column
	private String name;
	@Column
	private int power;
	@Column
	private int pp;
	@Column
	private int accuracy;
	@Transient
	private TypeWrapper type;

	// Este SÍ se persiste en BD
	@Column
	private String tipoPokemon;

	public static class TypeWrapper {
	    public String name;
	    public String url;
	}

	// Getter que devuelve el nombre del tipo
	public String getType() { return tipoPokemon; }

	// Después de deserializar, copiar el nombre al campo persistible
	public void resolverTipo() {
	    if (this.type != null) {
	        this.tipoPokemon = this.type.name;
	    }
	}
	
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public int getPower() { return power; }
	public void setPower(int p) { this.power = p; }
	public int getPp() { return pp; }
	public void setPp(int pp) { this.pp = pp; }
	public int getAccuracy() { return accuracy; }
	public void setAccuracy(int a) { this.accuracy = a; }
	@Override
	public String toString() {
	return "Movimiento[" + name + ", power=" + power +
	 ", type=" + type + "]";
	}
}
