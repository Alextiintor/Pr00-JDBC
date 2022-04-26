package model;

import java.io.Serializable;
import java.sql.Array;
import java.time.LocalDate;

public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String dni;
	private String name;
	private String lastName;
	private LocalDate birthDate;
	private String email;
	private Array phones;
	private Direccion dir;

	// public Persona() {
	// 	this.id = 0;
	// 	this.name= "";
	// 	this.lastName = "";
	// 	this.email = "";
	// 	this.phones = "";
	// 	this.dir = null;
	// }
	
	public Persona(int id, String dni, String name, String lastName, LocalDate birthDate, String email, Array phones,
			Direccion dir) {
		this.id = id;
		this.dni = dni;
		this.name = name;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.email = email;
		this.phones = phones;
		this.dir = dir;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Array getPhones() {
		return phones;
	}

	public void setPhones(Array phones) {
		this.phones = phones;
	}

	public Direccion getDir() {
		return dir;
	}

	public void setDir(Direccion dir) {
		this.dir = dir;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "Persona [dir=" + dir + ", dni=" + dni + ", email=" + email + ", id=" + id + ", lastName=" + lastName
				+ ", name=" + name + ", phones=" + phones + "]";
	}
}
