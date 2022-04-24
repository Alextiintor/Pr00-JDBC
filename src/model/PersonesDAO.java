package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase per gestionar la persistència de les dades de les persones
 * @author manuel
 *
 */
public class PersonesDAO {

	private Connection conexionBD;

	public PersonesDAO(Connection conexionBD) {
		this.conexionBD = conexionBD;
	}
	
	public List<Persona> getPersonesList() {
		List<Persona> personesList = new ArrayList<Persona>();
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM persones")) {
			while (result.next()) {
				personesList.add(new Persona(result.getInt("id"), result.getString("nom"), result.getString("apellidos"),result.getString("email"),result.getString("telefon")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return personesList;
	}

	public boolean save(Persona persona){
		try {
			String sql = "";
			PreparedStatement stmt = null;
			if (this.find(persona.getId()) == null){
				sql = "INSERT INTO persones VALUES(?,?,?,?,?)";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setInt(i++, persona.getId());
				stmt.setString(i++, persona.getNom());
				stmt.setString(i++, persona.getApellidos());
				stmt.setString(i++, persona.getEmail());
				stmt.setString(i++, persona.getTelefon());
			} else{
				sql = "UPDATE persones SET nom=?,apellidos=?,email=?,telefon=? WHERE id = ?";
				stmt = conexionBD.prepareStatement(sql);
				int i = 1;
				stmt.setString(i++, persona.getNom());
				stmt.setString(i++, persona.getApellidos());
				stmt.setString(i++, persona.getEmail());
				stmt.setString(i++, persona.getTelefon());
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

	public boolean delete(Integer id){
		try {
			String sql = "";
			PreparedStatement stmt = null;
			if (this.find(id) != null){
				sql = "DELETE FROM persones WHERE id = ?";
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
		try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM persones WHERE id = ?")){
			stmt.setInt(1, id); //informem el primer paràmetre de la consulta amb ?
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				p = new Persona(result.getInt("id"), result.getString("nom"), result.getString("apellidos"),result.getString("email"),result.getString("telefon"));
				p.imprimir();
			}	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return p;
	}

	public void showAll(){
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM persones")) {
			while (result.next()) {
				Persona p = new Persona(result.getInt("id"), result.getString("nom"), result.getString("apellidos"),result.getString("email"),result.getString("telefon"));
				p.imprimir();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}

