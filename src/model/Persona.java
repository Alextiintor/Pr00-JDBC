package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;
import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private int id;
	private String dni;
	private String name;
	private String lastName;
	private Date birthDate;
	private String email;
	@ElementCollection
		@CollectionTable(name="persona_telefons",
		joinColumns=@JoinColumn(name="idpersona"))
		@Column(name="telefon")
	private Set<String> phones;
	@Embedded
	private Direccion dir;

	public Persona() {}	
	public Persona(int id, String dni, String name, String lastName, Date birthDate, String email, Set<String> phones,
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

	public Set<String> getPhones() {
		return phones;
	}

	public void setPhones(Set<String> phones) {
		this.phones = phones;
	}

	public Direccion getDir() {
		return dir;
	}

	public void setDir(Direccion dir) {
		this.dir = dir;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "Persona [dir=" + dir + ", dni=" + dni + ", email=" + email + ", id=" + id + ", lastName=" + lastName
				+ ", name=" + name + ", phones=" + phones + "]";
	}
}
