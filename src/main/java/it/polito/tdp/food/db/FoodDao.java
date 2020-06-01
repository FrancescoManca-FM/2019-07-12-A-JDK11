package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public void listAllFoods(Map<Integer, Food> idMap){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					if(!idMap.containsKey(res.getInt("food_code"))) {
						
						idMap.put(res.getInt("food_code"), new Food(res.getInt("food_code"), res.getString("display_name")));
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM `portion`" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Food> getVertici(int numeroPorzioni, Map<Integer, Food> idMap){
		String sql = "SELECT food_code, COUNT(*) as porzioni " + 
				"FROM `portion` " + 
				"GROUP BY food_code";
		List<Food> res = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(rs.getInt("porzioni")==numeroPorzioni) {
					res.add(idMap.get(rs.getInt("food_code")));
				}
			}
			
			conn.close();
			return res;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati dal database");
		}
		
		
	}
	
	public List<Adiacenza> getAdiacenze(Map<Integer, Food> idMap){
		String sql = "SELECT fc1.food_code AS f1, fc2.food_code AS f2, AVG(c.condiment_calories) AS media " + 
				"FROM food_condiment AS fc1, food_condiment AS fc2, condiment AS c, food AS f1, food AS f2 " + 
				"WHERE fc1.condiment_code=c.condiment_code AND " + 
				"fc1.food_code!=fc2.food_code AND fc1.condiment_code=fc2.condiment_code AND " + 
				"f1.food_code>f2.food_code AND f1.food_code=fc1.food_code AND f2.food_code=fc2.food_code " + 
				"GROUP BY f1.food_code, f2.food_code";
		List<Adiacenza> res = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st  = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(idMap.containsKey(rs.getInt("f1")) && idMap.containsKey(rs.getInt("f2"))) {
					res.add(new Adiacenza(idMap.get(rs.getInt("f1")), idMap.get(rs.getInt("f2")), rs.getDouble("media")));
				}	
			}
			
			conn.close();
			return res;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati dal database");
		}
	}
}
