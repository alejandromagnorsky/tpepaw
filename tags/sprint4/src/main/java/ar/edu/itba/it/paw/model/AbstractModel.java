package ar.edu.itba.it.paw.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
