package hosp.db.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

 //the name is optional, is going to create a table by default with the name of the class
@Entity
@Table(name = "nurses")

public class Nurse implements Serializable {
	
	
	private static final long serialVersionUID = 9046867226460663272L;
	
	@Id //indicate the id is going to be the primary key
	@GeneratedValue(generator="nurses")
	@TableGenerator(name="nurses", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="nurses")
	
	
	@XmlAttribute //id is not going to appear on a xml; also for photos
	private Integer id;
	@XmlElement
	private String name;
	@XmlElement
	private String surname;
	@XmlElement	
	@ManyToMany
	@JoinTable(name="operations_nurses")

	private List<Operation> operations; //The nurse has a list of operations, many to many relationship

	
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	//CONSTRUCTOR WITH ALL
		public Nurse(Integer id, String name, String surname, List<Operation> operations) {
			super();
			this.id = id;
			this.name = name;
			this.surname = surname;
			this.operations = operations;
		}

	// CONSTRUCTOR WITHOUT ID
		public Nurse(String name, String surname, List<Operation> operations) {
			super();
			this.name = name;
			this.surname = surname;
			this.operations = operations;
		}
		//constructor needed
		public Nurse(Integer id,String name, String surname) {
			super();
			this.id=id;
			this.name = name;
			this.surname = surname;
			this.operations=new ArrayList<Operation>(); //to initialize the list
		}
		
		//constructor for the menu
		public Nurse(String name, String surname) {
			super();
			this.name = name;
			this.surname = surname;
			this.operations=new ArrayList<Operation>(); //to initialize the list
		}
		public Nurse() {
			super();
			this.operations=new ArrayList<Operation>();
		}
		
	
	// HASHCODE AND EQUALS METHODS
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
		Nurse other = (Nurse) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// TO STRING METHOD: the nurse is going to print the operations
	// the operation is not going to print the nurse to avoid errrors
	@Override
	public String toString() {
		return "Nurse [id=" + id + ", name=" + name + ", surname=" + surname +  "]";
	}

	
	
	
	
	
	
	

}
