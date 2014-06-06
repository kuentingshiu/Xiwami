package Service;

import java.util.List;

//import org.springframework.stereotype.Service;

import Model.Activity;
import Model.Family;
import Model.Item;
import Model.Transaction;
import Model.User;

import com.mongodb.WriteResult;

//@Service
public interface FamilyRepositoryCustom {

//	REST API: /activities
//	HTTP verb: GET
	public List<Activity> GetActivities() throws Exception;		
	
//	REST API: /items
//	HTTP verb: GET
	public List<Item> GetItems() throws Exception;	
	
//	REST API: /families
//	HTTP verb: GET
	public List<Family> GetFamilies() throws Exception;
	
//	REST API: /family/id
//	HTTP verb: GET
	public Family GetFamilyById(int id) throws Exception;
	
//	REST API: /family/id/edit
//	HTTP verb: GET	
	public Family GetFamilyByIdEdit(int id) throws Exception;	

//	REST API: /addfamily
//	HTTP verb: PUT
	public void AddFamily(Family newFamily) throws Exception;
	
//	REST API: /family/id/edit
//	HTTP verb: POST		
	public void UpdateFamily(int id, Family updateFamily) throws Exception;
	
//	REST API: /family/id/addperson
//	HTTP verb: PUT		
	public void AddFamilyPerson(int id, User newUser) throws Exception;
		
//	REST API: /family/id/person/id/edit
//	HTTP verb: GET	
	public User FindPersonByFamilyIdAndUserId(int familyId, int userId) throws Exception;
	
//	REST API: /family/id/person/id/edit
//	HTTP verb: POST		
	public WriteResult UpdatePersonByFamilyIdAndUserId(int familyId, int userId, User updateUser);
	
//	REST API: family/id/person/id/delete
//	HTTP verb: DELETE
	public WriteResult DeletePersonByFamilyIdAndUserId(int familyId, int userId);
	
// REST API : /family/searchfamily/languages=chinese&fromAge=2&toAge=5
// HTTP verb: GET		
	public List<Family> SearchFamily(String languages, int fromAge, int toAge) throws Exception;
	
// 	REST API: /user/userFacebookId/additem
// 	HTTP verb: PUT
	public WriteResult AddItem (String userFacebookId, Item newItem);
		
//	REST API: /user/userFacebookId/edititem
//	HTTP verb: POST
	public WriteResult EditItem (String userFacebookId, Item newItem);
	
//	REST API: /user/id/items?status=xx&type=yy
//	HTTP verb: GET	
	public List<Item> getItem (String userFacebookId, String status, String type);
	
	
//	REST API: /user/id/deleteitem
//	HTTP verb: DELETE	
	public WriteResult DeleteItem(String userFacebookId, Item deleteItem);
	
//	REST API: /transaction
//	HTTP verb: PUT
	public void userTransaction(Transaction transaction) throws Exception;
	
//	REST API: /user/id/transaction
//	HTTP verb: GET	
	public List<Transaction> getTransaction(String userId) throws Exception;
	
//	REST API: /activity
//	HTTP verb: PUT	
	public void AddActivity(Activity activity) throws Exception;
	
//	REST API: /activity
//	HTTP verb: POST
	public WriteResult EditActivity(Activity updatedActivity);
	
//	REST API: /activity
//	HTTP verb: DELETE
	public void DeleteActivity(Activity deletedActivity) throws Exception;
	
//	REST API: /activity?location=48105&type=2&fromdate=2014-1-1&todate=2014-1-3
//	HTTP verb: GET
	//public List<Activity> getActivity(String type, String fromDate, String toDate);
	
}
