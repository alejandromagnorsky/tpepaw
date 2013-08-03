package ar.edu.itba.it.paw.model;

public abstract class AbstractModel {
	
	protected int id;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		if(id < 0)
			throw new IllegalArgumentException();
		this.id = id;
	}	
	
	public boolean isNew(){
		return id == 0;
	}
}
