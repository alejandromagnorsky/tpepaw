package ar.edu.itba.it.paw.model;


public class Time {

	private int minutes;

	public Time(int minutes){
		if(minutes < 0)
			throw new IllegalArgumentException();
		this.minutes = minutes;
	}

	public int getMinuteOfHour() {
		return minutes%60;
	}

	public int getHourOfDay() {
		return (minutes/60)%8;
	}

	public int getDays() {
		return minutes/60/8;
	}
	
	public int getMinutes(){
		return minutes;
	}
	
	public void add(Time another){
		if(another != null)
			this.minutes += another.minutes;
	}
}
