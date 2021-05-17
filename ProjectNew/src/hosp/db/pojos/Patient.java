package hosp.db.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "patients")
public class Patient implements Serializable{
	
	
	private static final long serialVersionUID = 8206022291779704964L;
	@Id
	@GeneratedValue(generator="patients")
	@TableGenerator(name="patients", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="patients")
	
	private Integer id;
	private String name;
	private String surname;
	
	@OneToMany(mappedBy="patient") // patient is the one, and operations is the many
	private List<Operation> operations; //indicate that patient has a one to may relationship with operations
	
	@OneToOne(mappedBy="patient")
	private WaitingRoom room;
	
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
	public WaitingRoom getWaitingRoom() {
		return room;
	}
	public void setWaitingRoom(WaitingRoom room) {
		this.room = room;
	}
	
	
	//CONSTRUCTORS
	
	// empty constructor
		public Patient() {
			super();
			this.operations=new ArrayList<Operation>();
		}
		
	// constructor without ID
	public Patient(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
		this.operations=new ArrayList<Operation>();
	}
	
	// with the id
	public Patient(Integer id, String name, String surname) {
		super();
		this.id=id;
		this.name = name;
		this.surname = surname;
		this.operations=new ArrayList<Operation>();
	}
	
	public Patient(Integer id, String name, String surname, List<Operation> operations) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.operations = operations;
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
		Patient other = (Patient) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	//TO STRING METHOD
	// the patient is going to print the operations
	// one to many relationship
	@Override
	public String toString() {
		return "Patient [id=" + id + ", name=" + name + ", surname=" + surname + ", room=" +room+"]";
	}

	
	
	
	
	
	
	
	
	
	

}
