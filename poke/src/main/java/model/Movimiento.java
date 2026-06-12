package main.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
	@Column
	private String type;
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
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getPp() {
		return pp;
	}
	public void setPp(int pp) {
		this.pp = pp;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
	return "Movimiento[" + name + ", power=" + power +
	 ", type=" + type + "]";
	}
}
