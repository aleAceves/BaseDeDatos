package hosp.db.pojos;

import java.io.Serializable;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//ANOTATIONS FOR JPA
@Entity
@Table(name = "waiting_room")

//ANNOTATIONS FOR JAXB
@XmlAccessorType(XmlAccessType.FIELD) //Put annotations in the "fits" of the class
//activates the annotations for XML

@XmlRootElement(name = "WaitingRoom") //WaitingRoom can be the group element of an XML document
@XmlType(propOrder = { "id", "name" })
//Indicates the order in which all the attributes, elements, objects are in the XML 


public class WaitingRoom implements Serializable {
	
	private static final long serialVersionUID = 7452405030872022152L;
	@Id
	@GeneratedValue(generator="waiting_room")
	@TableGenerator(name="waiting_room", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="waiting_room")
	 
	@XmlAttribute  //We could use @XmlTransient that makes the id to not appear in the XML document
	private Integer id;
	@XmlAttribute 
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
	public WaitingRoom() {
		super();
	
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
