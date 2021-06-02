package hosp.db.jpa;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import hosp.db.ifaces.UserManager;
import hosp.db.pojos.Nurse;
import hosp.db.pojos.Operation;
import hosp.db.pojos.Patient;
import hosp.db.pojos.Surgeon;
import hosp.db.pojos.users.Role;
import hosp.db.pojos.users.User;





public class JPAUserManager implements UserManager {
	
	
	
 
	private static EntityManager em;

	
	public void connect() {
		em = Persistence.createEntityManagerFactory("user-hospital").createEntityManager();
		
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();
		List<Role> existingRoles = this.getRoles();
		if (existingRoles.size()<2) {
			this.newRole(new Role("admin"));
			//this.newRole(new Role("user"));
			//create the roles needed for our database
			this.newRole(new Role("surgeon"));
			this.newRole(new Role("nurse"));
			this.newRole(new Role("patient"));
		}

	}

	@Override
	public void disconnect() {
		em.close();

	}
	

	// delete surgeon using JDBC
	@Override
	
	public void deleteSurgeon(Integer surgeonId) {
		int s_id = surgeonId; 
		Query q2 = em.createNativeQuery("SELECT * FROM surgeons WHERE id = ?", Surgeon.class);
		q2.setParameter(1, s_id);
		Surgeon s = (Surgeon) q2.getSingleResult();
		em.getTransaction().begin();
		System.out.println(s);
		em.remove(s);
		em.getTransaction().commit();

		
	}
	
	//delete nurse using JDBC
	@Override
	public void deleteNurse(Integer nurseId) {
		
	
		int nu_id = nurseId;
		Query q2 = em.createNativeQuery("SELECT * FROM nurses WHERE id = ?", Patient.class);
		q2.setParameter(1, nu_id);
		Nurse p = (Nurse) q2.getSingleResult();
		em.getTransaction().begin();
		em.remove(p);
		em.getTransaction().commit();    
		
		
	}
	
	//delete patient using JPA
	@Override
	
	public void deletePatient(Integer id) {

		int patient_id = id;
		Query q2 = em.createNativeQuery("SELECT * FROM patients WHERE id = ?", Patient.class);
		q2.setParameter(1, patient_id);
		Patient p = (Patient) q2.getSingleResult();
		em.getTransaction().begin();
		em.remove(p);
		em.getTransaction().commit();      
	}
	
	
	@Override
	public List<Patient> selectPatients() {
		Query q1 = em.createNativeQuery("SELECT * FROM patients", Patient.class);
		List <Patient> patients= (List<Patient>) q1.getResultList();
		return patients;
	}

	//delete operation using JPA
	@Override
	public void deleteOperation(Integer id) {

	
		Query q2 = em.createNativeQuery("SELECT * FROM operation WHERE id = ?", Operation.class);
		q2.setParameter(1, id);
		Operation o = (Operation) q2.getSingleResult();

		em.getTransaction().begin();
		em.remove(o);
		em.getTransaction().commit();

	}
	
	
	//update patient using JPA
	@Override
	public void updatePatient(Patient p) {
		// Begin transaction
	
		em.getTransaction().begin();
		EntityTransaction  t = em.getTransaction(); 
		t.begin();
		t.commit();
		// Make changes
		em.flush();
		// End transaction
		em.getTransaction().commit();

	}

	@Override
	public void newUser(User u) {
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();

	}

	@Override
	public void newRole(Role r) {
		em.getTransaction().begin();
		em.persist(r);
		em.getTransaction().commit();

	}

	@Override
	public Role getRole(int id) {
		Query q = em.createNativeQuery("SELECT * FROM roles WHERE id = ?", Role.class);
		q.setParameter(1, id);
		return (Role) q.getSingleResult();
		
	}

	@Override
	public List<Role> getRoles() {
		Query q = em.createNativeQuery("SELECT * FROM roles", Role.class);
		return (List<Role>)q.getResultList(); //return a list of objects, that we are casting
		
	}

	@Override
	public User checkPassword(String email, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5"); //what we are going to use for the hash function
			md.update(password.getBytes());
			byte[] hash = md.digest();
			Query q = em.createNativeQuery("SELECT * FROM users WHERE email = ? AND password = ?", User.class);
			q.setParameter(1, email);
			q.setParameter(2, hash);
			return (User) q.getSingleResult();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoResultException nre) {
			return null;
		}
		return null;
	
		
	}

}
