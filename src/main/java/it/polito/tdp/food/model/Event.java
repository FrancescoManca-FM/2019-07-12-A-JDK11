package it.polito.tdp.food.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Event implements Comparable<Event>{

	public enum EventType{
		INIZIO_PREPARAZIONE, FINE_PREPARAZIONE
	}
	private double time;
	private double durataPreparazione;
	private EventType tipo;
	private Food food;
	@Override
	public int compareTo(Event o) {
		return (int)(this.time-o.time);
	}
	public Event(double time, double durataPreparazione, EventType tipo, Food food) {
		super();
		this.food = food;
		this.time = time;
		this.durataPreparazione = durataPreparazione;
		this.tipo = tipo;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getDurataPreparazione() {
		return durataPreparazione;
	}
	public void setDurataPreparazione(double durataPreparazione) {
		this.durataPreparazione = durataPreparazione;
	}
	public EventType getTipo() {
		return tipo;
	}
	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}
	@Override
	public String toString() {
		return "Event [time=" + time + ", durataPreparazione=" + durataPreparazione + ", tipo=" + tipo + "]";
	}
	
	
}
