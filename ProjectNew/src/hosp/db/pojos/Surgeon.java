package hosp.db.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "surgeons")

public class Surgeon implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4442487532035713640L;
	
	@Id
	@GeneratedValue(generator="surgeons")
	@TableGenerator(name="surgeons", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="surgeons")
	@XmlAttribute
	private Integer id;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String surname;
	@XmlAttribute
	private String speciality;
	
	@ManyToMany
	@XmlElement
	@JoinTable(name="operations_surgeons")
	private List<Operation> operations;
	
	/* ,
	joinColumns={@JoinColumn(name="surgeon_id", referencedColumnName="id")},
    inverseJoinColumns={@JoinColumn(name="operation_id", referencedColumnName="id")})
	@ManyToMany(mappedBy="surgeons") */

	
	// GETTERS AND SETTERS
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
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public List<Operation> getOperations() {
		return operations;
	}
	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	// HASHHCODE AND EQUALS METHOD
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
		Surgeon other = (Surgeon) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	// CONSTRUCTOR
	public Surgeon(Integer id, String name, String surname, String speciality, List<Operation> operations) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.speciality = speciality;
		this.operations = operations;
	}
	
	//CONSTRUCTOR WITHOUT THE ID
	public Surgeon(String name, String surname, String speciality, List<Operation> operations) {
		super();
		this.name = name;
		this.surname = surname;
		this.speciality = speciality;
		this.operations = operations;
	}
	
	//constructor without parameter
	public Surgeon() {
		super();
		this.operations=new ArrayList<Operation>();
	}
	public Surgeon(String name, String surname, String speciality) {
		super();
		this.name = name;
		this.surname=surname;
		this.speciality=speciality;
		this.operations=new ArrayList<Operation>();
	}
	
	
	// optional constructor
	public Surgeon(Integer id, String name, String surname, String speciality) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.speciality = speciality;
		this.operations=new ArrayList<Operation>(); //to initialize the list
	}
	// TO STRING METHOD
	// since is a many to many relationship, the surgeons shows all the operations but the ToString of operation do not show the surgeon
	
	@Override
	public String toString() {
		return "Surgeon [id=" + id + ", name=" + name + ", surname=" + surname + ", speciality=" + speciality
				+ "]\n";
	}
	
	//Un to string sin que imprima las operations: eso hace el!!!!! 
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
}
