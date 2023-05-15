package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data").toLocalDate(), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			rs.close();
			st.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta citta) {
		
		final String sql = "SELECT Localita, Data, Umidita "
				+ "FROM situazione "
				+ "WHERE MONTH(DATA)=? AND Localita = ? "
				+ "ORDER BY data ASC";
		
		List<Rilevamento> rilevamenti = new LinkedList<Rilevamento>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, citta.getNome());
			ResultSet rs = st.executeQuery();
			
			while( rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data").toLocalDate(), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}
			rs.close();
			st.close();
			conn.close();
			return rilevamenti;
				
			}catch (SQLException e) {
				System.out.println("Error in  DAO");
				e.printStackTrace();
				throw new RuntimeException(e);
				
			}
		
	}

	
	//elenco di tutte le citta presenti nel database
	public List<Citta> getCitta(){
		final String sql = "SELECT DISTINCT localita FROM situazione ORDER BY localita";

		List<Citta> result = new ArrayList<Citta>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Citta c = new Citta(rs.getString("localita"));
				result.add(c);
			}

			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	//dato un mese ed una città calcola l'umidità media
	public Double getUmiditaMedia( int mese, Citta citta) {
		
		final String sql = "SELECT AVG(Umidita) AS U FROM situazione " +
				   "WHERE localita=? AND MONTH(data)=? ";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, citta.getNome());
			st.setString(2, Integer.toString(mese));
			
			ResultSet rs = st.executeQuery();
			Double u = rs.getDouble("U");
			
			
			st.close();
			rs.close();
			conn.close();
			return u;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		}
		
		
		
	}
}
