package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class AsistenciaDAO {
    private Connection conexionBD;

    public AsistenciaDAO(Connection conexionBD) {
        this.conexionBD = conexionBD;
    }

    public List<Asistencia> getAsistenciaList() {
        System.out.println("Entro aqui");
		List<Asistencia> asistenciaList = new ArrayList<Asistencia>();
		try (ResultSet result = conexionBD.createStatement().executeQuery("SELECT * FROM asistencia")) {
            while (result.next()) { 
				asistenciaList.add(creaAsistenciaFromDB(result));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return asistenciaList;
	}

    private Asistencia creaAsistenciaFromDB(ResultSet result) throws SQLException{
        // Long fecha_entradaLong = result.getDate("fecha_entrada").getTime();
        // Long fecha_salidaLong = result.getDate("fecha_salida").getTime();
        // LocalDateTime fecha_entrada = LocalDateTime.ofInstant(Instant.ofEpochSecond(fecha_entradaLong),TimeZone.getDefault().toZoneId());
        // LocalDateTime fecha_salida = LocalDateTime.ofInstant(Instant.ofEpochSecond(fecha_salidaLong),TimeZone.getDefault().toZoneId());

        return new Asistencia(
            result.getInt("id"), 
            result.getTimestamp("fecha_entrada") , 
            result.getTimestamp("fecha_salida")
        );
    }

    public boolean save(Asistencia asistencia){
        try{
            String sql  ="";
            PreparedStatement stmt = null;

            if(find(asistencia.getId()) == null){
                sql = "INSERT INTO asistencia values(?, ?, ?)";
                stmt = conexionBD.prepareStatement(sql);
                int i = 1;
                stmt.setInt(i++, asistencia.getId());
                stmt.setTimestamp(i++, asistencia.getEntry_date());
                stmt.setTimestamp(i++, asistencia.getDeparture_date());
            } else {
                sql = "UPDATE asistencia set fecha_entrada=?, fecha_salida=? where id=?";
                stmt = conexionBD.prepareStatement(sql);
                int i = 1;
                stmt.setTimestamp(i++, asistencia.getEntry_date());
                stmt.setTimestamp(i++, asistencia.getDeparture_date());

				stmt.setInt(i++, asistencia.getId());
            }
            int rows = stmt.executeUpdate();
			if (rows == 1) return true;
			else return false;
        } catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
    }

    public Asistencia find(Integer id){
		if (id == null || id == 0){
			return null;
		}

		Asistencia a = null;
		try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM asistencia WHERE id = ?")){
			stmt.setInt(1, id); //informem el primer paràmetre de la consulta amb ?
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				a = creaAsistenciaFromDB(result);
			}	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return a;
	}

    public boolean delete(Integer id){
		try {
			String sql = "";
			PreparedStatement stmt = null;
			if (this.find(id) != null){
				sql = "DELETE FROM asistencia WHERE id = ?";
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
}
