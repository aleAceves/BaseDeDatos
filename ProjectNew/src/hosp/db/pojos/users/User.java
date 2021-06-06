package hosp.db.pojos.users;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="users")
public class User implements Serializable{
	
	
	

	private static final long serialVersionUID = 6444797276759676017L;
	@Id
	@GeneratedValue(generator="users")
	@TableGenerator(name="useers", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="users")
	private Integer id;
	private String email;
	
	@Lob
	private byte[] password;
	
	//a role can have several users
	@ManyToOne(fetch = FetchType.EAGER) //i want the role of the user every time
	@JoinColumn(name="role_ide")//put the join column in the side of the many
	private Role role;
	private Integer Ref_id;
	//GEETTERS AND SETTERS
	public Integer getRef_id() {
		return Ref_id;
	}
	public void setRef_id(Integer r) {
		Ref_id = r;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public byte[] getPassword() {
		return password;
	}
	public void setPassword(byte[] password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	//HASHCODE METHOD AND EQUALS
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	//empty constructor
	public User() {
		super();
	}
	
	//constructor including the role
	public User(Integer id, String email, byte[] password, Role role) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	public User(String email, byte[] password, Role role,int Ref_id) {
		super();
		this.email = email;
		this.password = password;
		this.role = role;
		this.Ref_id = Ref_id;
	}
	
	
	
	
	
	
	

}
