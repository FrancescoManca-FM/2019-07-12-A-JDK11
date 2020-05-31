package it.polito.tdp.food.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		m.creaGrafo(5);
		System.out.println("GRAFO CREATO!\n NUMERO VERTICI: "+m.getVertici()+"\n NUMERO ARCHI: "+m.getArchi()+"\n");
		/*Food f = m.getVertice(24107010);
		List<Adiacenza> vicini  = m.calorie(f.getFood_code());
		System.out.println("CIBO SELEZIONATO: "+f.toString()+"\n");
		for(Adiacenza a : vicini) {
			System.out.println(a.getF1()+" - "+a.getF2()+" CALORIE CONGIUNTE: "+ a.getPeso()+"\n");
		}*/
	}

}
