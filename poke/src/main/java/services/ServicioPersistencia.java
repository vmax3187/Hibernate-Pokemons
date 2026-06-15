package main.java.services;

import org.hibernate.Session;

import main.java.com.hibernate.ConnectionUtil;

public class ServicioPersistencia {
	public void persistir(Object objeto) {
	    Session session = ConnectionUtil.getSessionFactory().openSession();
	    try {
	        session.beginTransaction();
	        session.merge(objeto);             // ← merge en lugar de persist
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        session.getTransaction().rollback();
	        System.err.println("Error al persistir: " + e.getMessage());
	    } finally {
	        session.close();
	    }
	}
}
