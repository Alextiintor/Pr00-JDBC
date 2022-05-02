package model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Provider
 *
 */
@Entity
public class Provider extends Persona implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	public Provider() {
		super();
	}
   
}
