package controlador;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.util.Optional;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Direccion;
import model.Persona;
import model.PersonesDAO;

public class PersonesController{
	//Objecte per gestionar la persistència de les dades
	private PersonesDAO personesDAO;
	//Objecte per gestionar el objecte actual
	private Persona persona = null;
	//indicador de nou registre
	private boolean nouRegistre = false;
	//objecte per afegir les files de la taula
	private ObservableList<Persona> personesData;

	//Elements gràfics de la UI
	@FXML
	private AnchorPane anchorPane;
	private Stage ventana;

	@FXML private TextField idTextField;
	@FXML private TextField dniTextField;
	@FXML private TextField nameTextField;
	@FXML private TextField lastNameTextField;
	@FXML private DatePicker birthDateDatePicker;
	@FXML private TextField emailTextField;
	@FXML private TextField phonesTextField;

	@FXML private TextField localidadTextField;
	@FXML private TextField provinciaTextField;
	@FXML private TextField cpTextField;
	@FXML private TextField calleTextField;

	@FXML private TableView<Persona> personesTable;
	@FXML private TableColumn<Persona, Integer> idColumn;
	@FXML private TableColumn<Persona, String> nomColumn;

	//Validació dades
	private ValidationSupport vs;

	public void setConexionBD(Connection conexionBD) {	
		//Crear objecte DAO de persones
		personesDAO = new PersonesDAO(conexionBD);
		
		// Aprofitar per carregar la taula de persones (no ho podem fer al initialize() perque encara no tenim l'objecte conexionBD)
		// https://code.makery.ch/es/library/javafx-tutorial/part2/
		personesData = FXCollections.observableList(personesDAO.getPersonesList());
		personesTable.setItems(personesData);
	}
	
	/**
	 * Inicialitza la classe. JAVA l'executa automàticament després de carregar el fitxer fxml amb loader.load()
	 * i abans de rebre l'objecte conexionBD o qualsevol altre que pasem des del IniciMenuController
	 */
	@FXML private void initialize() {
		idColumn.setCellValueFactory(new PropertyValueFactory<Persona,Integer>("id"));
		nomColumn.setCellValueFactory(new PropertyValueFactory<Persona,String>("name"));

		// Quan l'usuari canvia de linia executem el métode mostrarPersona
		personesTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> mostrarPersona(newValue));

		//Validació dades
		//https://github.com/controlsfx/controlsfx/issues/1148
		//produeix error si no posem a les VM arguments això: --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED
		vs = new ValidationSupport();
		vs.registerValidator(idTextField, true, Validator.createEmptyValidator("ID obligatori"));
		vs.registerValidator(dniTextField, true, Validator.createEmptyValidator("DNI obligatorio"));
		vs.registerValidator(nameTextField, true, Validator.createEmptyValidator("Nom obligatori"));
		vs.registerValidator(lastNameTextField, true, Validator.createEmptyValidator("Cognoms obligatorio"));
		vs.registerValidator(birthDateDatePicker, true, Validator.createEmptyValidator("Fecha de nacimiento obligatoria"));
		vs.registerValidator(emailTextField, Validator.createRegexValidator("E-mail incorrecto", "^(.+)@(.+)$", Severity.ERROR));
		vs.registerValidator(phonesTextField, true, Validator.createEmptyValidator("Telefono/s obligatorio"));

		vs.registerValidator(localidadTextField, true, Validator.createEmptyValidator("Localidad obligatoria"));
		vs.registerValidator(provinciaTextField, true, Validator.createEmptyValidator("Provincia obligatoria"));
		vs.registerValidator(cpTextField, true, Validator.createEmptyValidator("Codigo Postal obligatorio"));
		vs.registerValidator(calleTextField, true, Validator.createEmptyValidator("Calle obligatoria"));
	}

	public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

	@FXML private void onKeyPressedId(KeyEvent e) throws IOException {
		if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB){
			//Comprovar si existeix la persona indicada en el control idTextField
			persona = personesDAO.find(Integer.parseInt(idTextField.getText()));
			mostrarPersona(persona);
			//seleccionar la fila de la taula asociada al codi introduit
			personesTable.getSelectionModel().select(persona);
			personesTable.refresh();
		}
	}

	@FXML
	void onActionEliminar(ActionEvent event) {
		if(isDatosValidos()){
			// Mostrar missatge confirmació
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Vol esborrar la persona " + idTextField.getText() + "?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				if(personesDAO.delete(Integer.parseInt(idTextField.getText()))){ 
					personesData.remove(personesTable.getSelectionModel().getSelectedIndex());

					limpiarFormulario();
					//personesDAO.showAll();
				}
			}
		}
	}

	@FXML private void onActionSortir(ActionEvent e) throws IOException {
		sortir();
		//tancar el formulari
		((BorderPane)anchorPane.getParent()).setCenter(null);
	}

	@FXML private void onActionGuardar(ActionEvent e) throws IOException {
		//verificar si les dades són vàlides
		if(isDatosValidos()){
			if(nouRegistre){
				Array phoneArray = personesDAO.getPhoneArray(phonesTextField.getText());

				Direccion dir = new Direccion(
					localidadTextField.getText(), 
					provinciaTextField.getText(), 
					cpTextField.getText(), 
					calleTextField.getText()
				);

				persona = new Persona(
					Integer.parseInt(idTextField.getText()), 
					dniTextField.getText(),
					nameTextField.getText(), 
					lastNameTextField.getText(), 
					birthDateDatePicker.getValue(),
					emailTextField.getText(), 
					phoneArray,
					dir
				);

				personesData.add(persona);
			}else{
				//modificació registre existent
				persona = personesTable.getSelectionModel().getSelectedItem();

				Array phoneArray = personesDAO.getPhoneArray(phonesTextField.getText());

				Direccion dir = new Direccion(
					localidadTextField.getText(), 
					provinciaTextField.getText(), 
					cpTextField.getText(), 
					calleTextField.getText()
				);

				persona.setDni(dniTextField.getText());
				persona.setName(nameTextField.getText()); 
				persona.setLastName(lastNameTextField.getText()); 
				persona.setBirthDate(birthDateDatePicker.getValue());
				persona.setEmail(emailTextField.getText()); 
				persona.setPhones(phoneArray);
				persona.setDir(dir);

				//persona.setTelefon(telefonTextField.getText()); 
			}
			personesDAO.save(persona);
			limpiarFormulario();

			personesTable.refresh();

			//personesDAO.showAll();
		}
	}

	public void sortir(){
		//personesDAO.showAll();
	}
	private boolean isDatosValidos() {

		//Comprovar si totes les dades són vàlides
		if (vs.isInvalid()) {
			String errors = vs.getValidationResult().getMessages().toString();
			// Mostrar finestra amb els errors
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(ventana);
			alert.setTitle("Campos incorrectos.");
			alert.setHeaderText("Corrige los campos incorrectos.");
			alert.setContentText(errors);
			alert.showAndWait();
		
			return false;
		}
		return true;
	}

	private void mostrarPersona(Persona persona) {

		if(persona != null){ 
			//llegir persona (posar els valors als controls per modificar-los)
			nouRegistre = false;
			idTextField.setText(String.valueOf(persona.getId()));
			dniTextField.setText(persona.getDni());
			nameTextField.setText(persona.getName());
			lastNameTextField.setText(persona.getLastName());
			birthDateDatePicker.setValue(persona.getBirthDate());
			emailTextField.setText(persona.getEmail());
			phonesTextField.setText(persona.getPhones().toString());

			localidadTextField.setText(persona.getDir().getLocalidad());
			provinciaTextField.setText(persona.getDir().getProvincia());
			cpTextField.setText(persona.getDir().getCp());
			calleTextField.setText(persona.getDir().getCalle());

		}else{ 
			//nou registre
			nouRegistre = true;
			//idTextField.setText(""); no hem de netejar la PK perquè l'usuari ha posat un valor
			dniTextField.setText("");
			nameTextField.setText("");
			lastNameTextField.setText("");
			birthDateDatePicker.setValue(null);
			emailTextField.setText("");
			phonesTextField.setText("");

			localidadTextField.setText("");
			provinciaTextField.setText("");
			cpTextField.setText("");
			calleTextField.setText("");
		}
	}

	// private String getPhoneString(Array phones){
	// 	phones[i];
	// 	phones.toString();
	// }

	private void limpiarFormulario(){
		idTextField.setText("");
		dniTextField.setText("");
		nameTextField.setText("");
		lastNameTextField.setText("");
		birthDateDatePicker.setValue(null);
		emailTextField.setText("");
		phonesTextField.setText("");

		localidadTextField.setText("");
		provinciaTextField.setText("");
		cpTextField.setText("");
		calleTextField.setText("");
	}
}
