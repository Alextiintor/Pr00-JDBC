package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Asistencia;
import model.AsistenciaDAO;

public class AsistenciaController {
    private AsistenciaDAO asistenciaDAO;
	//Objecte per gestionar el objecte actual
	private Asistencia asistencia = null;
	//indicador de nou registre
	private boolean nouRegistre = false;
	//objecte per afegir les files de la taula
	private ObservableList<Asistencia> asistenciaData;

	//Elements gràfics de la UI
	@FXML
	private AnchorPane anchorPane;
	private Stage ventana;

    @FXML private TextField idTextField;
    @FXML private DatePicker entryDatePicker;
    @FXML private DatePicker departureDatePicker;
    @FXML private TextField entryTimeTextField;
    @FXML private TextField departureTimeTextField;

    @FXML private TableView<Asistencia> asistenciaTable;
	@FXML private TableColumn<Asistencia, Integer> idColumn;
	@FXML private TableColumn<Asistencia, Date> entryDateColumn;
	@FXML private TableColumn<Asistencia, Date> departureDateColumn;

    private ValidationSupport vs;

    public void setConexionBD(Connection conexionBD){
        asistenciaDAO = new AsistenciaDAO(conexionBD);

        asistenciaData = FXCollections.observableList(asistenciaDAO.getAsistenciaList());
        asistenciaTable.setItems(asistenciaData);
    }

    @FXML
    private void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<Asistencia, Integer>("id"));
        entryDateColumn.setCellValueFactory(new PropertyValueFactory<Asistencia, Date>("entry_date"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<Asistencia, Date>("departure_date"));
    
        asistenciaTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showAsistencia(newValue));

        vs = new ValidationSupport();
        vs.registerValidator(idTextField, true, Validator.createEmptyValidator("ID obligatorio"));
        vs.registerValidator(entryDatePicker, true, Validator.createEmptyValidator("Fecha de entrada Obligratoria"));
        vs.registerValidator(entryTimeTextField, true, Validator.createEmptyValidator("Hora de entrada Obligatoria"));
    }

    public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

    @FXML 
    private void onKeyPressedId(KeyEvent e) throws IOException {
		if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB){
			//Comprovar si existeix la persona indicada en el control idTextField
			asistencia = asistenciaDAO.find(Integer.parseInt(idTextField.getText()));
			showAsistencia(asistencia);
			//seleccionar la fila de la taula asociada al codi introduit
			asistenciaTable.getSelectionModel().select(asistencia);
			asistenciaTable.refresh();
		}
	}

    @FXML 
    private void onActionGuardar(ActionEvent e ) throws IOException{
        if(isDataValid()){
            if(nouRegistre){
                LocalTime entry_dateTime = LocalTime.parse(entryTimeTextField.getText()); 
                LocalDateTime entry_dateLocalDateTime  = LocalDateTime.of(entryDatePicker.getValue(), entry_dateTime);
                
                Timestamp entry_date = Timestamp.valueOf(entry_dateLocalDateTime);

                if(departureDatePicker.getValue() != null){
                    LocalTime departure_dateTime = LocalTime.parse(departureTimeTextField.getText());
                    LocalDateTime departure_dateLocalDateTime = LocalDateTime.of(departureDatePicker.getValue(), departure_dateTime);
                    
                    Timestamp departure_date = Timestamp.valueOf(departure_dateLocalDateTime); 
                    asistencia = new Asistencia(
                        Integer.parseInt(idTextField.getText()), 
                        entry_date, 
                        departure_date
                    );
                } else {
                    asistencia = new Asistencia(
                        Integer.parseInt(idTextField.getText()), 
                        entry_date, 
                        null
                    ); 
                }
                asistenciaData.add(asistencia);
                
            } else {
                asistencia = asistenciaTable.getSelectionModel().getSelectedItem();

                LocalTime entry_dateTime = LocalTime.parse(entryTimeTextField.getText()); 
                LocalDateTime entry_dateLocalDateTime  = LocalDateTime.of(entryDatePicker.getValue(), entry_dateTime);
                
                Timestamp entry_date = Timestamp.valueOf(entry_dateLocalDateTime);

                if(departureDatePicker.getValue() != null){
                    LocalTime departure_dateTime = LocalTime.parse(departureTimeTextField.getText());
                    LocalDateTime departure_dateLocalDateTime = LocalDateTime.of(departureDatePicker.getValue(), departure_dateTime); 
                    
                    Timestamp departure_date = Timestamp.valueOf(departure_dateLocalDateTime);
                    asistencia.setEntry_date(entry_date);
                    asistencia.setDeparture_date(departure_date);

                } else {
                    asistencia.setEntry_date(entry_date);
                    asistencia.setDeparture_date(null);
                }
                
            }
            asistenciaDAO.save(asistencia);
            cleanForm();

            asistenciaTable.refresh();
        }
    }

    @FXML
	void onActionEliminar(ActionEvent event) {
		if(isDataValid()){
			// Mostrar missatge confirmació
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Quieres borrar la entrada: " + idTextField.getText() + "?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				if(asistenciaDAO.delete(Integer.parseInt(idTextField.getText()))){ 
					asistenciaData.remove(asistenciaTable.getSelectionModel().getSelectedIndex());

					cleanForm();
					//personesDAO.showAll();
				}
			}
		}
	}

    @FXML private void onActionSortir(ActionEvent e) throws IOException {
		//tancar el formulari
		((BorderPane)anchorPane.getParent()).setCenter(null);
	}

    private boolean isDataValid(){
        if(vs.isInvalid()){
            String errors = vs.getValidationResult().getMessages().toString();
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(ventana);
            alert.setTitle("Campos incorrectos");
            alert.setHeaderText("Corrige los campos incorrectos");
            alert.setContentText(errors);
            alert.showAndWait();

            return false;
        }

        return true;
    }


    private void showAsistencia(Asistencia asistencia){
        if(asistencia != null){
            nouRegistre = false;

            LocalDateTime entryLocalDateTime = asistencia.getEntry_date().toLocalDateTime();
            LocalTime entryHour = entryLocalDateTime.toLocalTime();

            idTextField.setText(String.valueOf(asistencia.getId()));
            entryDatePicker.setValue(entryLocalDateTime.toLocalDate());
            entryTimeTextField.setText(entryHour.toString());

            if(asistencia.getDeparture_date() != null){
                LocalDateTime departureLocalDateTime = asistencia.getDeparture_date().toLocalDateTime();
                LocalTime departureHour = departureLocalDateTime.toLocalTime();
            
                departureDatePicker.setValue(departureLocalDateTime.toLocalDate());
                departureTimeTextField.setText(departureHour.toString());
            } else {
                departureDatePicker.setValue(null);
                departureTimeTextField.setText("");
            }

        } else {
            nouRegistre = true;

            entryDatePicker.setValue(null);
            entryTimeTextField.setText("");
            departureDatePicker.setValue(null);
            departureTimeTextField.setText("");
        }
    }

    private void cleanForm(){
        idTextField.setText("");
        entryDatePicker.setValue(null);
        entryTimeTextField.setText("");
        departureDatePicker.setValue(null);
        departureTimeTextField.setText("");
    }

}
