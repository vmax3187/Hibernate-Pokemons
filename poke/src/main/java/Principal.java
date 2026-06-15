package main.java;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.hibernate.Session;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
		for (int i = 1; i <= 100; i++) {
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
		for (int i = 1; i <= 151; i++) {
			try {
				Pokemon poke = connector.loadPokemon(String.valueOf(i));
				if (poke.getMoves() != null) {
					for (Pokemon.MoveWrapper mw : poke.getMoves()) {
						String url = mw.move.url;
						long movId = Long.parseLong(url.replaceAll(".*/(\\d+)/?$", "$1"));
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

	static void consultasPokemon() {
		pokemonConMasMovimientos();
		movimientosFuertesDeFuego();
		mediaExperienciaPorTipo();
		pokemonPesadosConS();
	}

	static void pokemonConMasMovimientos() {
		Session session = ConnectionUtil.getSessionFactory().openSession();
		try {
			List<Object[]> resultado = session.createQuery("SELECT p, COUNT(m) as total "
					+ "FROM Pokemon p JOIN p.movimientos m " + "GROUP BY p ORDER BY total DESC", Object[].class)
					.setMaxResults(5).list();
			for (Object[] row : resultado) {
				Pokemon p = (Pokemon) row[0];
				Long total = (Long) row[1];
				System.out.println(p.getName() + " — " + total + " movimientos");
			}
		} finally {
			session.close();
		}
	}

	static void movimientosFuertesDeFuego() {
		Session session = ConnectionUtil.getSessionFactory().openSession();
		try {
			List<Movimiento> resultado = session
					.createQuery("FROM Movimiento m WHERE m.tipoPokemon = 'fire' AND m.power > 80 ORDER BY m.power DESC",
							Movimiento.class)
					.list();
			resultado.forEach(System.out::println);
		} finally {
			session.close();
		}
	}

	static void mediaExperienciaPorTipo() {
		Session session = ConnectionUtil.getSessionFactory().openSession();
		try {
			List<Pokemon> todos = session.createQuery("FROM Pokemon p", Pokemon.class).list();
			Map<String, Double> mediaPorTipo = todos.stream().filter(pokemon -> !pokemon.getMovimientos().isEmpty())
					.collect(Collectors.groupingBy(pokemon -> pokemon.getMovimientos().get(0).getType(),
							Collectors.averagingInt(Pokemon::getBase_experience)));

			mediaPorTipo.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed()).forEach(
					entry -> System.out.printf("Tipo %-10s: %.1f exp media%n", entry.getKey(), entry.getValue()));

		} finally {
			session.close();
		}
	}

	static void pokemonPesadosConS() {
		Session session = ConnectionUtil.getSessionFactory().openSession();
		try {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Pokemon> cq = cb.createQuery(Pokemon.class);
			Root<Pokemon> root = cq.from(Pokemon.class);
			cq.select(root).where(cb.and(cb.greaterThan(root.get("weight"), 500), cb.like(root.get("name"), "s%")));
			List<Pokemon> resultado = session.createQuery(cq).list();
			resultado.forEach(System.out::println);
		} finally {
			session.close();
		}
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
