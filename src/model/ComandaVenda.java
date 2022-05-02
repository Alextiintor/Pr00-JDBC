package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: ComandaVenda
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class ComandaVenda implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@Id
	private int id;
	private String name;
	

	public ComandaVenda() {
		super();
	}
   
}
