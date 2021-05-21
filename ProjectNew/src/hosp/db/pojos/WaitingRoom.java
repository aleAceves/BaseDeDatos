package hosp.db.pojos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


@Entity
@Table(name = "waitingRooms")
public class WaitingRoom implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7452405030872022152L;
	@Id
	@GeneratedValue(generator="waitingRooms")
	@TableGenerator(name="waitingRooms", table="sqlite_sequence",
	    pkColumnName="waitingRooms", valueColumnName="seq", pkColumnValue="waitingRooms")
	
	private Integer id;
	private String name;
	
	

	//GETTERS AND SETTERS
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	//CONSTRUCTOR WITH ID
	public WaitingRoom(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	//constructor without id
	public WaitingRoom( String name) {
		super();
		this.name = name;
	}

	//HASHCODE AND EQUALS METHOD
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
		WaitingRoom other = (WaitingRoom) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "WaitingRoom [id=" + id + ", name=" + name + "]";
	}
	
	
	
	
	
	

}
