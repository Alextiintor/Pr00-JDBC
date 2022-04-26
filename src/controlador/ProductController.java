package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;
import java.util.TreeSet;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import model.ProductsDAO;
import model.Pack;
import model.Product;

public class ProductController {

    private ProductsDAO productsDAO;

    private Product product = null;

    private boolean newProduct = false;

    //objecte per afegir les files de la taula
	private ObservableList<Product> productsData;

    @FXML
	private AnchorPane anchorPane;
	private Stage window;
    //Tabla
    @FXML private TableView<Product> productsTable;
	@FXML private TableColumn<Product, Integer> idColumn;
	@FXML private TableColumn<Product, String> nameColumn;
    //Producto
	@FXML private TextField idTextField;
	@FXML private TextField nameTextField;
	@FXML private TextField priceTextField;
	@FXML private TextField stockTextField;
	@FXML private DatePicker startDatePicker;
	@FXML private DatePicker endDatePicker;
	//Pack
    @FXML private CheckBox packCheckBox;
    @FXML private GridPane packGridPane;
    @FXML private TextField dtoTextField;
    @FXML private TextArea listTextArea;

    private ValidationSupport vs;

    public void setDBConnection(Connection conexionBD) {	
		//Crear objecte DAO de persones
		productsDAO = new ProductsDAO(conexionBD);
		
		// Aprofitar per carregar la taula de persones (no ho podem fer al initialize() perque encara no tenim l'objecte conexionBD)
		// https://code.makery.ch/es/library/javafx-tutorial/part2/
		productsData = FXCollections.observableList(productsDAO.getProductsList());
		productsTable.setItems(productsData);
	}

    @FXML
    private void initialize() {
		idColumn.setCellValueFactory(new PropertyValueFactory<Product,Integer>("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));

		// Quan l'usuari canvia de linia executem el métode mostrarPersona
		productsTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showProduct(newValue));

		//Validació dades
		//https://github.com/controlsfx/controlsfx/issues/1148
		//produeix error si no posem a les VM arguments això: --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED
        defaultValidators();
	}

    public Stage getWindow() {
		return window;
	}

	public void setWindow(Stage window) {
		this.window = window;
	}

    @FXML 
    private void onKeyPressedId(KeyEvent e) throws IOException {
		if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB){
			//Comprovar si existeix la persona indicada en el control idTextField
			product = productsDAO.findInDB(Integer.parseInt(idTextField.getText()));
			showProduct(product);
			//seleccionar la fila de la taula asociada al codi introduit
			productsTable.getSelectionModel().select(product);
			productsTable.refresh();
		}
	}

    @FXML
    private void onActionCheckPack(ActionEvent e) {
        if(packCheckBox.isSelected()){
            packGridPane.setVisible(true);
            packValidators();
        } else {
            packGridPane.setVisible(false);
            defaultValidators();
        }
    }

    private void packValidators(){
        vs.registerValidator(dtoTextField, true, Validator.createEmptyValidator("Descuento Obligatorio"));
        vs.registerValidator(listTextArea, true, Validator.createEmptyValidator("Lista de productos Obligatoria"));
    }

    private void defaultValidators(){
        vs = new ValidationSupport();
		vs.registerValidator(idTextField, true, Validator.createEmptyValidator("ID obligatori"));
		vs.registerValidator(nameTextField, true, Validator.createEmptyValidator("Nom obligatori"));
		vs.registerValidator(priceTextField, true, Validator.createEmptyValidator("precio obligatori"));
		vs.registerValidator(stockTextField, true, Validator.createEmptyValidator("stock obligatori"));
		vs.registerValidator(startDatePicker, true, Validator.createEmptyValidator("Start Date obligatori"));
		vs.registerValidator(endDatePicker, true, Validator.createEmptyValidator("End Date obligatori"));
    }

    @FXML
    private void onActionEliminar(ActionEvent event) {
		if(isDataValid()){
			// Mostrar missatge confirmació
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Quieres borrar el producto " + idTextField.getText() + "?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				if(productsDAO.deleteInDB(Integer.parseInt(idTextField.getText()))){ 
					productsData.remove(productsTable.getSelectionModel().getSelectedIndex());

					clearForm();
					//productsDAO.showAll();
				}
			}
		}
	}

    @FXML 
    private void onActionSortir(ActionEvent e) throws IOException {
		//tancar el formulari
		((BorderPane)anchorPane.getParent()).setCenter(null);
	}
    
    @FXML private void onActionGuardar(ActionEvent e) throws IOException {
		//verificar si les dades són vàlides
		if(isDataValid()){
			if(newProduct){
                System.out.println("Nuevo producto");
                if(packCheckBox.isSelected()){
                    product = createPack();
                } else {
                    product = createProduct();
                }				

				productsData.add(product);
                productsDAO.saveInDB(product);
			}else{
                System.out.println("Update producto");
				//modificació registre existent
                if(packCheckBox.isSelected()){
                    Pack p = createPack();
                    productsDAO.saveInDB(p);
                } else {
                    product = productsTable.getSelectionModel().getSelectedItem();

                    product.setName(nameTextField.getText());
                    product.setPrice(Float.parseFloat(priceTextField.getText()));
                    product.setStock(Integer.parseInt(stockTextField.getText()));
                    product.setCatalogStartDate(startDatePicker.getValue());
                    product.setCatalogFinishDate(endDatePicker.getValue());

                    productsDAO.saveInDB(product); 
                }
				
			}
			
			clearForm();

			productsTable.refresh();

			//productsDAO.showAll();
		}
	}

    private Product createProduct(){
        return new Product(
            Integer.parseInt(idTextField.getText()), 
            nameTextField.getText(), 
            Float.parseFloat(priceTextField.getText()), 
            Integer.parseInt(stockTextField.getText()), 
            startDatePicker.getValue(), 
            endDatePicker.getValue()
        );
    }

    private Pack createPack(){
        TreeSet<Product> packProductList = textAreaToList();

        return new Pack(
            Integer.parseInt(idTextField.getText()), 
            nameTextField.getText(), 
            Float.parseFloat(priceTextField.getText()), 
            Integer.parseInt(stockTextField.getText()), 
            packProductList, 
            Integer.parseInt(dtoTextField.getText()), 
            startDatePicker.getValue(), 
            endDatePicker.getValue()
        );
    }

    private TreeSet<Product> textAreaToList(){
        TreeSet<Product> packProductList = new TreeSet<Product>();
        String[] productsIDs = listTextArea.getText().split(",");
        for(int i = 0; i<productsIDs.length; i++){
            Product p = productsDAO.findInDB(Integer.parseInt(productsIDs[i]));
            packProductList.add(p);
        }
        return packProductList;
    }

    private boolean isDataValid() {

		//Comprovar si totes les dades són vàlides
		if (vs.isInvalid()) {
			String errors = vs.getValidationResult().getMessages().toString();
			// Mostrar finestra amb els errors
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(window);
			alert.setTitle("Camps incorrectes");
			alert.setHeaderText("Corregeix els camps incorrectes");
			alert.setContentText(errors);
			alert.showAndWait();
		
			return false;
		}
		return true;
	}

    private void showProduct(Object _product){
        if(_product!=null){
            newProduct = false;
            if(_product instanceof Pack){
                Pack packTemp = (Pack)_product;
                idTextField.setText(String.valueOf(packTemp.getId()));
                nameTextField.setText(packTemp.getName());
                priceTextField.setText(String.valueOf(packTemp.getPrice()));
                stockTextField.setText(String.valueOf(packTemp.getStock()));
                startDatePicker.setValue(packTemp.getCatalogStartDate());
                endDatePicker.setValue(packTemp.getCatalogFinishDate());

                packCheckBox.setSelected(true);
                packGridPane.setVisible(true);
                dtoTextField.setText(String.valueOf(packTemp.getDiscount()));
                String packProductListString = productsDAO.getPackProductString(packTemp.getId());
                listTextArea.setText(packProductListString);
            } else {
                Product productTemp = (Product)_product;
                idTextField.setText(String.valueOf(productTemp.getId()));
                nameTextField.setText(productTemp.getName());
                priceTextField.setText(String.valueOf(productTemp.getPrice()));
                stockTextField.setText(String.valueOf(productTemp.getStock()));
                startDatePicker.setValue(productTemp.getCatalogStartDate());
                endDatePicker.setValue(productTemp.getCatalogFinishDate());

                packCheckBox.setSelected(false);
                packGridPane.setVisible(false);
                dtoTextField.setText("");
                listTextArea.setText("");
            }
            
        } else {
            newProduct = true;
            nameTextField.setText("");
            priceTextField.setText("");
            stockTextField.setText("");
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);

            packCheckBox.setSelected(false);
            packGridPane.setVisible(false);
            dtoTextField.setText("");
            listTextArea.setText("");
        }
    }

    private void clearForm(){
		idTextField.setText("");
		nameTextField.setText("");
		priceTextField.setText("");
		stockTextField.setText("");
		startDatePicker.setValue(null);
		endDatePicker.setValue(null);

        packCheckBox.setSelected(false);
        packGridPane.setVisible(false);
        dtoTextField.setText("");
        listTextArea.setText("");
	}
}
