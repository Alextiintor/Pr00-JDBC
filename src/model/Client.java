package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * Entity implementation class for Entity: Client
 *
 */
@Entity
public class Client extends Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Client() {
		super();
	}
   
}
