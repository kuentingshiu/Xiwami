package Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import Model.Activity;
import Model.Family;
import Model.Item;
import Model.Status;
import Model.Transaction;
import Model.User;
import Service.FamilyRepositoryCustom;

import com.mongodb.WriteResult;

@Controller
public class FamilyController {

	@Autowired
	private FamilyRepositoryCustom myFamily;

//	REST API: /families
//	HTTP verb: GET
	@RequestMapping(value = "/families", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String,List<Family>> FindFamilies() {
		
		try {			
			Map<String,List<Family>> responseBody = new HashMap<String,List<Family>>();
			responseBody.put("Families", myFamily.GetFamilies());
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Families.");
			return null;
		}
	}

//	REST API: /items
//	HTTP verb: GET
	@RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String,List<Item>> FindItems() {		
		try {
			Map<String,List<Item>> responseBody = new HashMap<String,List<Item>>();
			responseBody.put("Items", myFamily.GetItems());
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Families.");
			return null;
		}
	}	
	
//	REST API: /activities
//	HTTP verb: GET
	@RequestMapping(value = "/activities", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String,List<Activity>> FindActivities() {
		
		try {
			Map<String,List<Activity>> responseBody = new HashMap<String,List<Activity>>();
			responseBody.put("Activities", myFamily.GetActivities());
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Families.");
			return null;
		}
	}
	
	
//	REST API: /family/id
//	HTTP verb: GET
	@RequestMapping(value = "/families/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String,Family> FindByFamilyId(@PathVariable("id") int id) {
		
		try {
			//return "Family:{" + myFamily.GetFamilyById(id) + "}";
			Map<String,Family> responseBody = new HashMap<String,Family>();
			responseBody.put("Family", myFamily.GetFamilyById(id));
			return responseBody;
			//return myFamily.GetFamilyById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query Family.");
			return null;
		}
	}
	
//	REST API: /addfamily
//	HTTP verb: PUT
	@RequestMapping(value = "/addFamily", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public Status AddFamily(@RequestBody Family newFamily)
	{
		try {
			myFamily.AddFamily(newFamily);
			return new Status("Success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");
			return new Status("Unable to add Family");
		}
		
	}
	
//	REST API: /family/id/edit
//	HTTP verb: GET
	@RequestMapping(value = "/families/{id}/edit", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String,Family> FindByFamilyEditId(@PathVariable("id") int id) {
		
		try {
			Map<String,Family> responseBody = new HashMap<String,Family>();
			responseBody.put("Family", myFamily.GetFamilyByIdEdit(id));
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to view Family.");
			return null;
		}
	}
	
//	REST API: /family/id/edit
//	HTTP verb: POST	
	@RequestMapping(value = "/families/{id}/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Status UpdateFamily(@PathVariable("id") int id, @RequestBody Family newFamily) {
		
		try {
			myFamily.UpdateFamily(id, newFamily);
			return new Status("Success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to add Family.");
			return new Status("Unable to edit Family");
		}

	}
	
//	REST API: /family/id/addperson
//	HTTP verb: PUT
	@RequestMapping(value = "/families/{familyId}/addPerson", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public Status AddPerson(@PathVariable("familyId") int familyId, @RequestBody User newUser) {
		try {
			myFamily.AddFamilyPerson(familyId, newUser);
			return new Status("Success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to add User.");
			return new Status("Unable to add User");
		}
	}
	
	
//	REST API: /family/id/person/id/edit
//	HTTP verb: GET	
	@RequestMapping (value = "/families/{familyId}/person/{userId}/edit", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public User GetUserByFamilyIdAndPersonId(@PathVariable("familyId") int familyId, @PathVariable("userId") int userId) {
		
		try {
			return myFamily.FindPersonByFamilyIdAndUserId(familyId, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to view User.");
			return null;
		}		
	}
	
//	REST API: /family/id/person/id/edit
//	HTTP verb: POST		
	@RequestMapping (value = "/families/{familyId}/person/{userId}/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Status UpdateUserByFamilyIdAndPersonId(@PathVariable ("familyId") int familyId, 
												  @PathVariable ("userId") int userId, 
												  @RequestBody User updateUser) {
		
		WriteResult wr = myFamily.UpdatePersonByFamilyIdAndUserId(familyId, userId, updateUser);
		
		if (wr.getError() == null)
			return new Status("Success");
		else
			return new Status("Unable to update User");
/*		try {
			WriteResult wr = myFamily.UpdatePersonByFamilyIdAndUserId(familyId, userId, updateUser);
			return new Status("Success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to add User.");
			return new Status("Unable to update User");
		}*/

	}
	
//	REST API: family/id/person/id/delete
//	HTTP verb: DELETE	
	@RequestMapping (value = "/families/{familyId}/person/{userId}/delete", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public Status DeleteUserByFamilyIdAndPersonId(@PathVariable("familyId") int familyId, @PathVariable("userId")int userId) {
		
		WriteResult wr = myFamily.DeletePersonByFamilyIdAndUserId(familyId, userId);
		
		if (wr.getError() == null)
			return new Status("Success");
		else
			return new Status("Unable to update User");

	}
	
// REST API : /family/searchfamily/languages=chinese&fromAge=2&toAge=5
// HTTP verb: GET		
	@RequestMapping (value = "/families/searchfamily", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String,List<Family>> SearchFamily(@RequestParam(value = "languages") String languages, 
								     @RequestParam(value = "fromAge") int fromAge, 
								     @RequestParam(value = "toAge") int toAge) {
		
		try {
			Map<String,List<Family>> responseBody = new HashMap<String,List<Family>>();
			responseBody.put("Families", myFamily.SearchFamily(languages,fromAge, toAge));
			return responseBody;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to view Families.");
			return null;
		}		
	}
	
	
	
// 	REST API: /user/id/additem
// 	HTTP verb: PUT
	@RequestMapping(value = "/user/{userFacebookId}/additem", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public Status AddItem (@PathVariable("userFacebookId") String userFacebookId, @RequestBody Item newItem) {
		
		WriteResult wr = myFamily.AddItem(userFacebookId, newItem);
		
		if (wr.getError() == null)
			return new Status("201");
		else
			return new Status("Unable to add item");
	}			
	
//	REST API: /user/userFacebookId/edititem
//	HTTP verb: POST
	@RequestMapping(value = "/user/{userFacebookId}/edititem", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Status EditItem (@PathVariable("userFacebookId") String userFacebookId, @RequestBody Item newItem) {
		
		WriteResult wr = myFamily.EditItem(userFacebookId, newItem);
		
		if (wr.getError() == null)
			return new Status("200");
		else
			return new Status("Unable to edit item");
	}	
	
//	REST API: /user/id/items?status=xx&type=yy
//	HTTP verb: GET
	@RequestMapping(value="/user/{userFacebookId}/items", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Map<String,List<Item>> getItems(@PathVariable("userFacebookId") String userFacebookId,
//	public @ResponseBody Map<String,Family> getItems(@PathVariable("userFacebookId") String userFacebookId,	
											 @RequestParam (value="status", required=false) String status,
											 @RequestParam (value="type", required=false) String type
											) {
		Map<String,List<Item>> responseBody = new HashMap<String,List<Item>>();
//		Map<String,Family> responseBody = new HashMap<String,Family>();
		responseBody.put("Items", myFamily.getItem(userFacebookId, status, type));
		return responseBody;
	}
	
//	REST API: /user/id/deleteitem
//	HTTP verb: DELETE	
	@RequestMapping(value = "/user/{userFacebookId}/deleteitem", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody	
	public Status DeleteItem(@PathVariable("userFacebookId") String userFacebookId, @RequestBody Item deleteItem) {
		
		WriteResult wr = myFamily.DeleteItem(userFacebookId, deleteItem);
		if (wr.getError() == null)
			return new Status("200");
		else
			return new Status("Unable to delete item");
	}
										
//	REST API: /transaction
//	HTTP verb: PUT
	@RequestMapping(value = "/transaction", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public Status userTransaction(@RequestBody Transaction transaction)
	{
		try {
			myFamily.userTransaction(transaction);
			return new Status("201");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to save the transaction.");
			return new Status("Unable to save the transaction");
		}
		
	}
	
//	REST API: /user/id/transaction
//	HTTP verb: GET	
	@RequestMapping(value = "/user/{userId}/transaction", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Transaction> getTransaction(@PathVariable("userId") String userId) {
		
		try {
			return myFamily.getTransaction(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query transaction.");
			return null;
		}
	}
	
	//REST API: /activity
	//HTTP verb: PUT
	@RequestMapping(value = "/activities", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public Status AddActivity(@RequestBody Activity activity)
	{
		try {
			myFamily.AddActivity(activity);
			return new Status("201");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to add the activity.");
			return new Status("Unable to add the activity.");
		}
		
	}	
	
	//	REST API: /activity
	//	HTTP verb: POST
	@RequestMapping(value = "/activities", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Status EditActivity (@RequestBody Activity updatedActivity) {
		
		WriteResult wr = myFamily.EditActivity(updatedActivity);
		
		if (wr.getError() == null)
			return new Status("200");
		else
			return new Status("Unable to edit item");
	}
	
	//	REST API: /activity
	//	HTTP verb: DELETE
	@RequestMapping(value = "/activities", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public Status DeleteActivity (@RequestBody Activity deletedActivity) {
		
		try {
			myFamily.DeleteActivity(deletedActivity);
			return new Status("200");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to delete the activity.");
			return new Status("Unable to delete the activity.");
		}
	}	
	
	//	REST API: /activity?location=48105&type=2&fromdate=2014-1-1&todate=2014-1-3
	//	HTTP verb: GET
	
	
}

