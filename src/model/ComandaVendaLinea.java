package model;

import java.io.Serializable;
import javax.persistence.*;
import model.ComandaVenda;

/**
 * Entity implementation class for Entity: ComandaVendaLinea
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class ComandaVendaLinea extends ComandaVenda implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public ComandaVendaLinea() {
		super();
	}
   
}
