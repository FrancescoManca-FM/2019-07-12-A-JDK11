/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.boxFood.getItems().clear();
    	int numeroPorzioni;
    	try {
    		numeroPorzioni = Integer.parseInt(txtPorzioni.getText());
    	}catch(NumberFormatException nfe) {
    		nfe.printStackTrace();
    		txtResult.appendText("Devi inserire un numero intero");
    		throw new RuntimeException("Devi inserire un numero intero");

    	}
    	this.model.creaGrafo(numeroPorzioni);
    	if(this.model.getVertici()==0) {
    		txtResult.appendText("Hai inserito un numero troppo alto. Il grafo non ha vertici!");
    		return;
    	}
    	this.boxFood.getItems().addAll(this.model.getListaVertici(numeroPorzioni));
    	txtResult.appendText("Creazione grafo...");
    	txtResult.appendText("\n GRAFO CREATO!\n NUMERO VERTICI: "+this.model.getVertici()+"\nNUMERO ARCHI: "+this.model.getArchi()+"\n");
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Analisi calorie...");
    	Food cibo = boxFood.getValue();
    	if(cibo==null) {
    		txtResult.appendText("Devi selezionare un cibo dal menù a tendina! se è vuoto crea prima il grafo!");
    		return;
    	}
    	List<Adiacenza> risultato = this.model.calorie(cibo);
    	txtResult.appendText("\nCIBO SELEZIONATO: "+cibo.toString()+"\n");
    	if(risultato.size()<=5) {
	    	for(Adiacenza a : risultato) {
	    		if(a.getF1().equals(cibo)) {
	    			txtResult.appendText(a.getF2()+", CALORIE CONGIUNTE: "+a.getPeso()+"\n");
	    		}else {
	    			txtResult.appendText(a.getF1()+", CALORIE CONGIUNTE: "+a.getPeso()+"\n");
	
	    		}
	    	}
    	}else {
	    	for(int i=0; i<5;i++) {
	    		if(risultato.get(i).getF1().equals(cibo)) {
	    			txtResult.appendText(risultato.get(i).getF2()+", CALORIE CONGIUNTE: "+risultato.get(i).getPeso()+"\n");
	    		}else {
	    			txtResult.appendText(risultato.get(i).getF1()+", CALORIE CONGIUNTE: "+risultato.get(i).getPeso()+"\n");
	
	    		}
	    	}
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...");
    	Food cibo = boxFood.getValue();
    	if(cibo==null) {
    		txtResult.appendText("Devi selezionare un cibo dal menu a tendina");
    		return;
    	}
    	
    	int k=0;
    	try {
    		k = Integer.parseInt(txtK.getText());
    	}catch(NumberFormatException nfe) {
    		nfe.printStackTrace();
    		txtResult.appendText("k deve essere un numero intero compreso tra 1 e 10");
    		return;
    	}
    	this.model.initSim(k, cibo);
    	this.model.runSim();
    	
    	txtResult.appendText(String.format("Simulazione completata!\n NUMERO CIBI PREPARATI: %d \n DURATA PREPARAZIONE: %f ", this.model.getCibiPreparati(), this.model.getTempo()));
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
