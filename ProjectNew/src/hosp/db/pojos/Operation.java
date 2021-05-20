package hosp.db.pojos;

import java.io.Serializable; //to be able to create a file with the information of the class
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import hosp.xml.utils.SQLDateAdapter;


@Entity
@Table(name = "operations")

//XML
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Operation")
@XmlType(propOrder= {"type", "startdate", "duration"}) // the proper order of things, the order is important


public class Operation implements Serializable {
	
	
	
	private static final long serialVersionUID = -4212586232702635067L;
	@Id
	@GeneratedValue(generator="operations")
	@TableGenerator(name="operations", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="operations")
	
	//XML
	@XmlTransient
	private Integer id;
	@XmlElement
	private String type;
	@XmlElement
	@XmlJavaTypeAdapter(SQLDateAdapter.class) // to convert the string into a readable thing for xml file
	private Date startdate; //import from java.sql
	@XmlElement
	private Integer duration;
	
	
	@XmlElement(name="operation_surgeons") 
	@XmlElementWrapper(name="surgeons")
	
	@ManyToMany(mappedBy="operation")
	@JoinTable(name="operations_surgeons",
	joinColumns={@JoinColumn(name="operation_id", referencedColumnName="id")},
    inverseJoinColumns={@JoinColumn(name="surgeon_ID", referencedColumnName="id")})
	private List<Surgeon> surgeons; //list of surgeons that have this operation
	
	@XmlElement(name="operation_nurses") 
	@XmlElementWrapper(name="nurses")
	
	@ManyToMany(mappedBy="operation")
	@JoinTable(name="operations_nurses",
	joinColumns={@JoinColumn(name="operation_id", referencedColumnName="id")},
    inverseJoinColumns={@JoinColumn(name="nurse_ID", referencedColumnName="id")})
	private List<Nurse> nurses; // list of nurses on the operation
	
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY) //to save time, we do not need to access all time to it.
	@JoinColumn(name = "patient_id") //use to indicate in the side of the many we are going to have a foreign key, and we specify the name of the column
	private Patient patient;
	
	
	@XmlTransient
	@OneToOne(mappedBy="operation")
	private OperatingRoom room;
	
	//CREATION OF GETTERS AND SETTERS
	
	public String getType() {
		return type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public List<Surgeon> getSurgeons() {
		return surgeons;
	}
	public void setSurgeons(List<Surgeon> surgeons) {
		this.surgeons = surgeons;
	}
	public List<Nurse> getNurses() {
		return nurses;
	}
	public void setNurses(List<Nurse> nurses) {
		this.nurses = nurses;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient p) {
		this.patient = p;
	}
	public OperatingRoom getOperatingRoom() {
		return room;
	}
	public void setOperatingRoom(OperatingRoom room) {
		this.room = room;
	}
	
	
	
	
	// method useful to add: for the surgeons
	public void addSurgeon(Surgeon surgeon) {
		// if surgeons does not contain this surgeons, is going to add it
		if(!surgeons.contains(surgeon)) {
			surgeons.add(surgeon);
		}
		
	}
	
	public void removeSurgeon(Surgeon surgeon) {
		if(!surgeons.contains(surgeon)) {
			surgeons.remove(surgeon);
		}
	}
	
	// useful method: for the nurses
	public void addNurse(Nurse nurse) {
		// if nurses does not contain this nurse, is going to add it
		if(!nurses.contains(nurse)) {
			nurses.add(nurse);
		}
	}	
	
	
	public void removeNurse(Nurse nurse) {
		if(!nurses.contains(nurse)) {
			nurses.remove(nurse);
		}
	}
	
	//useful method: for the patient
	public void addPatient(Patient patient) {
		if(!patient.equals(patient)) {
				patient.setId(patient.getId());
			}
	}
	
	/*
	public void removePatient(Patient patient) {
		if(patient.equals(patient)) {
			////////
		}
	}
	*/
			
		
	
	
	// EMPTY CONSTRUCTOR
	public Operation() {
		this.surgeons = new ArrayList<Surgeon>();
		this.nurses = new ArrayList<Nurse>();
		this.patient= new Patient();
		this.room = new OperatingRoom();
	}
	

	//GENERATE CONSTRUCTOR
	public Operation(Integer id, String type, Date startdate, Integer duration2) {
		super();
		this.id=id;
		this.type = type;
		this.startdate = startdate;
		this.duration = duration2;
		this.surgeons = new ArrayList<Surgeon>();
		this.nurses = new ArrayList<Nurse>();
		this.patient= new Patient();
		this.room = new OperatingRoom();
	}
	
	public Operation(Integer id, String type, Date startdate, Integer duration2, Patient patient) {
		super();
		this.id=id;
		this.type = type;
		this.startdate = startdate;
		this.duration = duration2;
		this.patient= patient;
		this.surgeons = new ArrayList<Surgeon>();
		this.nurses = new ArrayList<Nurse>();
		this.room = new OperatingRoom();
	}
	
	
	// constructor needed in the menu
	public Operation(String type, Date startdate, Integer duration2) {
		super();
		this.type = type;
		this.startdate = startdate;
		this.duration = duration2;
		this.surgeons = new ArrayList<Surgeon>();
		this.nurses = new ArrayList<Nurse>();
		this.patient= new Patient();
		this.room = new OperatingRoom();
	}
	public Operation(String type, Date startdate, Integer duration2, Patient patient) {
		super();
		this.type = type;
		this.startdate = startdate;
		this.duration = duration2;
		this.patient= patient;
		this.room = new OperatingRoom();
		this.surgeons = new ArrayList<Surgeon>();
		this.nurses = new ArrayList<Nurse>();
	}
	
	//CONSTRUCTOR WITH THE ID AND THE LIST
	public Operation(Integer id, String type, Date startdate, Integer duration, List<Surgeon> surgeons, List<Nurse> nurses) {
		super();
		this.id = id;
		this.type = type;
		this.startdate = startdate;
		this.duration = duration;
		this.surgeons = surgeons;
		this.nurses = nurses;
	}
	
	
	
	
	
	//GENERATE THE HASHCODE AND EQUALS
	// Equals: determines if two objects are the same
	// we just use the id for them
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		//is going to check if both objects have the same reference
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		//do a casting
		Operation other = (Operation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	//TO STRING METHOD with everything
	@Override
	public String toString() {
		return "Operation [id=" + id + ", type=" + type + ", startdate=" + startdate + ", duration=" + duration
				+ ", surgeons=" + surgeons + ", nurses=" + nurses + ", patient=" + this.getPatient().getName() + ", room=" + room + "]";
	}
	
	




	
}
