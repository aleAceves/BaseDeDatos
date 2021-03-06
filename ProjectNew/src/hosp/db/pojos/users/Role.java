package hosp.db.pojos.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="roles")
public class Role implements Serializable{

//hi this is a change for commiting

	private static final long serialVersionUID = 247947661084291846L;
	
	
	@Id
	@GeneratedValue(generator="roles")
	@TableGenerator(name="roles", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="roles")
	
	private Integer id;
	private String name;
	@OneToMany(mappedBy="role") //a user just can have one role
	private List<User> users;
	
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
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	
	// HASHCODE METHOD AND EQUALS
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
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	//EMPTY CONSTRUCTOR
	public Role() {
		super();
		this.users=new ArrayList<User>();
	}
	
	public Role(String name) {
		super();
		
		this.name=name;
		
	}
	
	public Role(int id2) {
		// TODO Auto-generated constructor stub
	}
	// to string without the users
	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}
	
	
	
	
}
