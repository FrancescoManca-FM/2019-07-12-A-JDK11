package it.polito.tdp.food.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private FoodDao dao;
	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	private Simulator sim;
	
	public Model() {
		this.dao = new FoodDao();
		this.idMap = new HashMap<Integer, Food>();
		this.dao.listAllFoods(idMap);
		sim = new Simulator();
	}
	
	public void creaGrafo(int numeroPorzioni) {
		
		List<Food> vertici = this.dao.getVertici(numeroPorzioni, this.idMap);
		this.grafo = new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, vertici);
		List<Adiacenza> archi = this.dao.getAdiacenze(idMap);
		for(Adiacenza a : archi) {
			if(!this.grafo.containsEdge(a.getF1(), a.getF2()) && this.grafo.containsVertex(a.getF1()) && this.grafo.containsVertex(a.getF2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getF1(), a.getF2(), a.getPeso());
			}
		}
		
	}
	
	public int getVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public List<Food> getListaVertici(int porzioni){
		List<Food> ordinata = this.dao.getVertici(porzioni, idMap);
		Collections.sort(ordinata);
		return ordinata;
	}
	
	public int getArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getAdiacenze(){
		return this.dao.getAdiacenze(idMap);
	}
	
	public List<Adiacenza> calorie(Food cibo){
		List<Adiacenza> res = new ArrayList<>();
		for(Food f : Graphs.neighborListOf(this.grafo, cibo)) {
			DefaultWeightedEdge e = this.grafo.getEdge(f, cibo);
			res.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e),this.grafo.getEdgeWeight(e)));
		}
		Collections.sort(res);
		return res;
	}

	public Food getVertice(int code) {
		return this.idMap.get(code);
	}
	
	public void initSim(int k, Food source) {
		this.sim.init(k, source, grafo, this);
	}
	
	public void runSim() {
		this.sim.run();
	}
	
	public int getCibiPreparati() {
		return this.sim.getN();
	}
	
	public double getTempo() {
		return this.sim.getTempoTotale();
	}
}
