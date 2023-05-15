package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	MeteoDAO meteoDAO;
	private List<Citta> leCitta;
	private List<Citta> best;
	
	public Model() {
		meteoDAO = new MeteoDAO();
		this.leCitta = meteoDAO.getCitta();
		
	}

	public String getUmiditaMedia(int mese) {
		List<Citta> citta = this.meteoDAO.getCitta();
		String medie = "";
		
		if( citta.size() != 0) {
			for( Citta city : citta) {
				List<Rilevamento> rilevamenti = this.meteoDAO.getAllRilevamentiLocalitaMese(mese, city);
				int sommaRiv = 0;
				// ora sommo tutte le umidita dei rilevamenti e le divido per la grandezza della lista ottenendo una media
				for( Rilevamento r : rilevamenti) {
					sommaRiv = sommaRiv + r.getUmidita();
				}
				sommaRiv = sommaRiv / rilevamenti.size();
				medie = medie + "L'umidità media a " + city + " è del " + sommaRiv + " %. \n";
						
			}
		}
		return medie;
	}
	

	public List<Citta> trovaSequenza(int mese) {
		List<Citta> parziale = new ArrayList<>();
		this.best = null;
		
		MeteoDAO dao = new MeteoDAO();
		
		//carico per ciascuna citta i relativi rilevamenti
		for(Citta c : this.leCitta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c));
		}
		
		cerca(parziale, 0);
		
		return best;
	}

	private void cerca(List<Citta> parziale, int livello) {
		
		if( livello== this.NUMERO_GIORNI_TOTALI) {
			//caso terminale
			Double costo = calcolaCosto(parziale);
			if( best == null || costo < calcolaCosto(best)) {
				best = new ArrayList<>(parziale);
			}
		}else {
			//caso intermedio
			for( Citta prova : leCitta) {
				if( aggiuntaValida(prova, parziale)) {
					parziale.add(prova);
					cerca( parziale, livello +1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}

	
	
	/**
	 * @param prova, citta che sto cercando di aggiungere
	 * @param parziale, la sequenza di città già aggiunte 
	 * @return true se la città è lecita, false se viola i vincoli
	 */
	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		
		//verifica se ha raggiunto il max dei giorni
		int  conta = 0;
		for( Citta precedente : parziale) {
			if( precedente.equals(prova)) {
				conta++;
			}
		}
		
		if( conta >= this.NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		if( parziale.size()==0)
			return true;
		if( parziale.size()==1 || parziale.size()==2) {
			//secondo o terzo giorno, aggiungo se la città è la stessa
			return parziale.get(parziale.size()-1).equals(prova);
		}
		
		//giorni successivi a 3
		if( parziale.get(parziale.size()-1).equals(prova))
			return true;
		
		if (parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) 
				&& parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
					return true;
		
		
		return false;
	}

	
	
	/**
	 * 
	 * @param lista parziale di tutte le citta
	 * @return il costo, che tiene conto dell'umidità nei 15 giorni e del costo fisso di 100 ogni volta che si cambia città
	 */
	private Double calcolaCosto(List<Citta> parziale) {
		double costo = 0.0;
		for( int giorno = 1; giorno <= this.NUMERO_GIORNI_TOTALI; giorno++) {
			//citta dove mi trovo
			Citta c = parziale.get(giorno-1);
			// umidità che ho in quel giorno in quella citta
			double umid = c.getRilevamenti().get(giorno-1).getUmidita();
			costo +=umid;
		}
		
		//sommo 100 * num di volte in cui cambio città
		for(int giorno = 2; giorno<= this.NUMERO_GIORNI_TOTALI; giorno++) {
			if( !parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo+=this.COST;
			}
		}
		
		return costo;
	}
	


}
