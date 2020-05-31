package it.polito.tdp.food.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.model.Event.EventType;

public class Simulator {

	private PriorityQueue<Event> coda;
	
	//PARAMETRI DA INSERIRE
	int K=2; //numero stazioni di lavoro
	Food source;
	SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	LocalDateTime APERTURA_CUCINA;
	Model model;
	
	//STATO DEL SISTEMA 
	List<Food> cibiPreparati;
	int n; //numero di cibi preparati
	int kLibere;
	
	//PARAMETRI IN OUTPUT
	double tempoTotale;
	
	public void init(int k, Food source, SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo, Model model) {
		this.K = k;
		this.source = source;
		this.grafo = grafo;
		this.model = model;
		
		cibiPreparati = new ArrayList<>();
		n = 0;
		kLibere = K;
		tempoTotale = 0.0;
		coda = new PriorityQueue<>();
		
		List<Food> vicini = Graphs.neighborListOf(grafo, source);
		if(vicini.size()<K) {
			for(Food vicino : vicini) {
				DefaultWeightedEdge edge = this.grafo.getEdge(source, vicino);
				Event e = new Event(this.tempoTotale, this.grafo.getEdgeWeight(edge), EventType.INIZIO_PREPARAZIONE, vicino);
				this.coda.add(e);
			}
		}else {
			int i = 0;
			for(Food vicino : vicini) {
				if(i<K) {
					DefaultWeightedEdge edge = this.grafo.getEdge(source, vicino);
					Event e = new Event(this.tempoTotale, this.grafo.getEdgeWeight(edge), EventType.INIZIO_PREPARAZIONE, vicino);
					this.coda.add(e);
					i++;
				}
			}
		}
	}
	
	public List<Food> getCibiPreparati() {
		return cibiPreparati;
	}

	public int getN() {
		return n;
	}

	public double getTempoTotale() {
		return tempoTotale;
	}

	public void setK(int k) {
		K = k;
	}

	public void run() {
		while(!this.coda.isEmpty()) {
			Event e = this.coda.poll();
			System.out.println(e);
			this.processEvent(e);
		}
	}
	
	public void processEvent(Event e) {
		
		switch(e.getTipo()) {
		
		case INIZIO_PREPARAZIONE:
			
			kLibere--;
			n++;
			this.tempoTotale += e.getDurataPreparazione();
			cibiPreparati.add(e.getFood());
			coda.add(new Event(this.tempoTotale, 0, EventType.FINE_PREPARAZIONE, e.getFood()));
			break;
			
		case FINE_PREPARAZIONE:
			kLibere++;
			Adiacenza prossimo = bestVicino(e.getFood());
			if(prossimo!=null) {
				if(e.getFood().equals(prossimo.getF1())) {
					if(!cibiPreparati.contains(prossimo.getF2()) && this.kLibere>0) {
						coda.add(new Event(tempoTotale, prossimo.getPeso(), EventType.INIZIO_PREPARAZIONE, prossimo.getF2()));
					}
				}else {
					if(!cibiPreparati.contains(prossimo.getF1()) && this.kLibere>0) {
						coda.add(new Event(tempoTotale, prossimo.getPeso(), EventType.INIZIO_PREPARAZIONE, prossimo.getF1()));
					}
				}
			}else {
				System.out.println("caz");
			}
			break;
		}
	}
	
	public Adiacenza bestVicino(Food f) {

		List<Adiacenza> lista = this.model.calorie(f);
		
		return lista.get(0);
	}
}
