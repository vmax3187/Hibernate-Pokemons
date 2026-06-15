package main.java.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.GsonBuilder;
import main.java.model.Pokemon;
import main.java.model.Movimiento;

public class Connector {
	private final HttpClient client = HttpClient.newHttpClient();

	public Pokemon loadPokemon(String id) {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(Pokemon.URL_API + id)).build();
		String json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
				.join();
		return new GsonBuilder().setPrettyPrinting().create().fromJson(json, Pokemon.class);
	}

	public Movimiento loadMovimiento(String nombre) {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(Movimiento.URL_API + nombre)).build();
		String json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
				.join();
		Movimiento m = new GsonBuilder().create().fromJson(json, Movimiento.class);
		m.resolverTipo();  
		return m;		
	}
}