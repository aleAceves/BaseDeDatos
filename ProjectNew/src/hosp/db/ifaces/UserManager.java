package hosp.db.ifaces;


import java.util.List;

import hosp.db.pojos.OperatingRoom;
import hosp.db.pojos.Patient;
import hosp.db.pojos.users.Role;
import hosp.db.pojos.users.User;

public interface UserManager {
	
	public void connect();
	public void disconnect();
	public void newUser(User u);
	public void newRole(Role r);
	public Role getRole(int id);
	public List<Role> getRoles();
	public User checkPassword(String email, String password); //important method
	
	
	
	
	void deleteOperation(Integer id);
	
	void updateOperatingRoom(Integer or, String name);
	List<OperatingRoom> showOperationRooms();
	
	
	
	

}
