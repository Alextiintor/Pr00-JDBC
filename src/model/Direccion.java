package model;


import javax.persistence.Embeddable;

@Embeddable
public class Direccion {
    private String localidad;
    private String provincia;
    private String cp;
    private String calle;


    public Direccion() {}
    
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
}
