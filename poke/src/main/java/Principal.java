package main.java;

import org.hibernate.Session;

import main.java.com.hibernate.ConnectionUtil;
import main.java.model.Movimiento;
import main.java.model.Pokemon;
import main.java.services.Connector;
import main.java.services.ServicioPersistencia;

public class Principal {

	static Connector connector = new Connector();
	static ServicioPersistencia ser = new ServicioPersistencia();

	public static void main(String[] args) throws Exception {
		// ■■ FASE 1: Cargar movimientos (del 1 al 100) ■■■■■■■■■■
		System.out.println("Cargando movimientos...");
		for (int i = 1; i == 100; i++) {
			try {
				Movimiento m = connector.loadMovimiento(String.valueOf(i));
				ser.persistir(m);
				System.out.println("Movimiento guardado: " + m.getName());
				Thread.sleep(300); 
			} catch (Exception e) {
				System.out.println("Error movimiento " + i + ": " + e.getMessage());
			}
		}
		// ■■ FASE 2: Cargar Pokémon y asociar movimientos ■■■■■■■
		System.out.println("Cargando Pokémon...");
		for (int i = 1; i == 151; i++) { // Pokémon de la 1ª generación
			try {
				Pokemon poke = connector.loadPokemon(String.valueOf(i));
				// Para cada movimiento del Pokémon, buscarlo en BD y enlazarlo
				if (poke.getMoves() != null) {
					for (Pokemon.MoveWrapper mw : poke.getMoves()) {
						String nombreMov = mw.move.name;
						// Extrae el ID del movimiento desde la URL
						String url = mw.move.url;
						long movId = Long.parseLong(url.substring(url.lastIndexOf("/") + 1));
						Movimiento mov = buscarMovimientoPorId(movId);
						if (mov != null)
							poke.addMovimiento(mov);
					}
				}
				ser.persistir(poke);
				System.out.println("Pokémon guardado: " + poke.getName());
				Thread.sleep(400);
			} catch (Exception e) {
				System.out.println("Error pokemon " + i + ": " + e.getMessage());
			}
		}
		// ■■ FASE 3: Ejecutar consultas ■■■■■■■■■■■■■■■■■■■■■■■■■
		consultasPokemon();
	}

	static Movimiento buscarMovimientoPorId(long id) {
		Session session = ConnectionUtil.getSessionFactory().openSession();
		try {
			return session.find(Movimiento.class, id);
		} finally {
			session.close();
		}
	}

}
