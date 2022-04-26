package model;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class Direccion implements SQLData {
    private String localidad;
    private String provincia;
    private String cp;
    private String calle;
    private String sql_type;


    public Direccion(String localidad, String provincia, String cp, String calle) {
        this.localidad = localidad;
        this.provincia = provincia;
        this.cp = cp;
        this.calle = calle;
    }


    public String getLocalidad() {
        return localidad;
    }


    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }


    public String getProvincia() {
        return provincia;
    }


    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }


    public String getCp() {
        return cp;
    }


    public void setCp(String cp) {
        this.cp = cp;
    }


    public String getCalle() {
        return calle;
    }


    public void setCalle(String calle) {
        this.calle = calle;
    }


    @Override
    public String toString() {
        return "Direction [calle=" + calle + ", cp=" + cp + ", localidad=" + localidad + ", provincia=" + provincia
                + "]";
    }


    @Override
    public String getSQLTypeName() throws SQLException {
        return sql_type;
    }


    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sql_type = typeName;
        localidad = stream.readString();
        provincia = stream.readString();
        cp = stream.readString();
        calle = stream.readString();
    }


    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(localidad);
        stream.writeString(provincia);
        stream.writeString(cp);
        stream.writeString(calle);
    }
   
}
