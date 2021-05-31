package hosp.db.pojos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "operatingRooms")
public class OperatingRoom implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2486511277771155480L;
	@Id
	@GeneratedValue(generator="operatingRooms")
	@TableGenerator(name="operatingRooms", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="operatingRooms")
	
	private Integer id;
	private String name;
	
	public Integer getId() {
		return id;
	}
	
	// GETTERS AND SETTERS
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//HASH CODE AND EQUALS METHOD
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperatingRoom other = (OperatingRoom) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	//GENERAL CONSTRUCTOR
	public OperatingRoom(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	//constructor without id
	public OperatingRoom( String name) {
		super();
		this.name = name;
	}
	//empty constructor
	public OperatingRoom() {
		super();
	}
	
	
	//TO STRING METHOD
	@Override
	public String toString() {
		return "OperatingRoom [id=" + id + ", name=" + name + "]";
	}
	

	
	
	
	
	
	

}
