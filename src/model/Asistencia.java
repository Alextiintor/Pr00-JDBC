package model;

import java.sql.Timestamp;

public class Asistencia {
    private int id;
    private Timestamp entry_date;
    private Timestamp departure_date;

    public Asistencia(int id, Timestamp entry_date, Timestamp departure_date) {
        this.id = id;
        this.entry_date = entry_date;
        this.departure_date = departure_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(Timestamp entry_date) {
        this.entry_date = entry_date;
    }

    public Timestamp getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(Timestamp departure_date) {
        this.departure_date = departure_date;
    }
   
}
