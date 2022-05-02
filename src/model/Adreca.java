package model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable 
public class Adreca implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String poblacio;
	private String provincia;
	private String cp;
	private String domicili;
	
	public Adreca() {
		this.poblacio = "";
		this.provincia = "";
		this.cp = "";
		this.domicili = "";
	}

	
	public Adreca(String poblacio, String provincia, String cp, String domicili) {
		this.poblacio = poblacio;
		this.provincia = provincia;
		this.cp = cp;
		this.domicili = domicili;
	}

	/**
	 * 
	 * @return Devuelve la poblacion
	 */
	public String getPoblacio() {
		return poblacio;
	}

	public void setPoblacio(String poblacio) {
		this.poblacio = poblacio;
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

	public String getDomicili() {
		return domicili;
	}

	
	public void setDomicili(String domicili) {
		this.domicili = domicili;
	}
	
	public void imprimir(){
		System.out.println("Població: " + poblacio);
		System.out.println("Provincia: " + provincia);
		System.out.println("CP: " + cp);
		System.out.println("Domicili: " + domicili);
	}


}
