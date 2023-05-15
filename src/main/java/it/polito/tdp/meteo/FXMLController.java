/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    
    Model model;
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    public void setModel( Model model) {
    	this.model = model;
    }
    
    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	
    	Integer m = this.boxMese.getValue();
    	
    	if( m != null) {
    		List<Citta> best = model.trovaSequenza(m);
    		
    		this.txtResult.appendText(String.format("Sequenza ottima per il mese %s\n", Integer.toString(m)));
    		this.txtResult.appendText(best + "\n");
    	}
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	int mese = this.boxMese.getValue();
    	String risultato = this.model.getUmiditaMedia(mese);
    	this.txtResult.setText(risultato);
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        for( int i = 1; i <=12; i++) {
        	this.boxMese.getItems().add(i);
        }
    }
}

