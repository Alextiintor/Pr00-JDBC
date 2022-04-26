package model;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

public class PersonesDAO {

	private Connection conexionBD;

	public PersonesDAO(Connection conexionBD) {
		this.conexionBD = conexionBD;
	}
	
	public List<Persona> getPersonesList() {
		List<Persona> personesList = new ArrayList<Persona>();
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM persona")) {
			while (result.next()) {
				personesList.add(createPersonFromDB(result));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return personesList;
	}

	private Persona createPersonFromDB(ResultSet result){
		Persona p = null;
		
		try {
			Direccion dir = new Direccion("Sabadell", "Barcelona", "08203", "Av. Barbera");
			p = new Persona(
				result.getInt("id"), 
				result.getString("dni"), 
				result.getString("name"), 
				result.getString("lastname"), 
				result.getDate("fecha_nacimiento").toLocalDate(),
				result.getString("email"), 
				result.getArray("telefonos"), 
				dir
			);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return p;
	}

	public boolean isClient(int id){
		try (PreparedStatement stmt = conexionBD.prepareStatement("select * from cliente where id = ?")){
			stmt.setInt(1, id);
			ResultSet result = stmt.executeQuery();

			return result.next();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public boolean save(Persona persona){
		try {
			String sql = "";
			PreparedStatement stmt = null;
			if (this.find(persona.getId()) == null){
				sql = "INSERT INTO persona VALUES(?,?,?,?,?,?,?,?)";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, persona.getId());
				stmt.setString(i++, persona.getDni());
				stmt.setString(i++, persona.getName());
				stmt.setString(i++, persona.getLastName());
				stmt.setDate(i++, Date.valueOf(persona.getBirthDate()));
				stmt.setString(i++, persona.getEmail());
				stmt.setArray(i++, persona.getPhones());
				stmt.setObject(i++, persona.getDir());
			} else{
				sql = "UPDATE persona SET dni=?,name=?,lastname=?,fecha_nacimiento=?, email=?, telefonos=? WHERE id = ?";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setString(i++, persona.getDni());
				stmt.setString(i++, persona.getName());
				stmt.setString(i++, persona.getLastName());
				stmt.setDate(i++, Date.valueOf(persona.getBirthDate()));
				stmt.setString(i++, persona.getEmail());
				stmt.setArray(i++, persona.getPhones());
				stmt.setInt(i++, persona.getId());
			}
			int rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public Array getPhoneArray(String phones){

		String [] phonesStringArray = phones.split(",");
		Array phonesArray = null;
		try {
			phonesArray = conexionBD.createArrayOf("VARCHAR", phonesStringArray); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		 
		return phonesArray;
	}

	public boolean delete(Integer id){
		try {
			String sql = "";
			PreparedStatement stmt = null;
			if (this.find(id) != null){
				sql = "DELETE FROM persona WHERE id = ?";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, id);
			}
			int rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public Persona find(Integer id){
		if (id == null || id == 0){
			return null;
		}

		Persona p = null;
		try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM persona WHERE id = ?")){
			stmt.setInt(1, id); //informem el primer paràmetre de la consulta amb ?
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				p = createPersonFromDB(result);
			}	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return p;
	}

	// public void showAll(){
	// 	try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM persones")) {
	// 		while (result.next()) {
	// 			Persona p = new Persona(result.getInt("id"), result.getString("nom"), result.getString("apellidos"),result.getString("email"),result.getString("telefon"));
	// 			p.imprimir();
	// 		}
	// 	} catch (SQLException e) {
	// 		System.out.println(e.getMessage());
	// 	}
	// }
}

