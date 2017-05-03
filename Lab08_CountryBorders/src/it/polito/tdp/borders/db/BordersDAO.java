package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries(int anno) {

		String sql = "SELECT ccode,StateAbb,StateNme " 
				   + "FROM country, contiguity " 
				   + "WHERE country.StateAbb=contiguity.state1ab "
				   + "AND year<=?";

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			List<Country> c = new ArrayList<Country>();
			
			while (rs.next()) {
				
				Country x = new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				c.add(x);
			}

			conn.close();
			return c;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database Error -- loadAllCountries");
			throw new RuntimeException("Database Error");
		}
	}

	public List<Border> getCountryPairs(int anno) {

		String sql = "SELECT contiguity.state1ab, contiguity.state2ab "+
					 "FROM country, contiguity "+
					 "WHERE country.StateAbb=contiguity.state1ab "+
					 "AND contiguity.conttype = 1 "+
					 "AND contiguity.year <= ?";
		
		List<Border> b = new ArrayList<Border>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
		
			while (rs.next()) {
				Country c1 = new Country(rs.getString(1));
				Country c2 = new Country(rs.getString(2));
				b.add(new Border(c1, c2));
			}
		
			conn.close();
			return b;
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database Error -- getCountryPairs");
			throw new RuntimeException("Database Error");
		}
		
	}
}
