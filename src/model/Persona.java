package model;

import java.io.Serializable;

public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String nom;
	private String apellidos;
	private String email;
	private String telefon;

	public Persona() {
		this.id = 0;
		this.nom = "";
		this.apellidos = "";
		this.email = "";
		this.telefon = "";
	}
	
	public Persona(int id, String nom, String apellidos, String email, String telefon) {
		this.id = id;
		this.nom = nom;
		this.apellidos = apellidos;
		this.email = email;
		this.telefon = telefon;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	
	public void imprimir(){
		System.out.println("Id: " + id);
		System.out.println("Nom: " + nom);
		System.out.println("Apellidos: " + apellidos);
		System.out.println("E-mail: " + email);
		System.out.println("Telèfon: " + telefon);
	}
}
