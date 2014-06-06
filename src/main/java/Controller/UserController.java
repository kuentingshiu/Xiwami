package Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Model.User;
import Service.UserRepositoryCustom;

@Controller
public class UserController {
	
	@Autowired
	private UserRepositoryCustom thisUser;

//	REST API: /user/id
//	HTTP verb: GET
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody 
	public User FindByUserId(@PathVariable("id") int id) {
		
		try {
			return thisUser.GetUserById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error : unable to query User.");
			return null;
		}
	}
	
/*	@RequestMapping(value = "/family/{id}/addPerson", method = RequestMethod.PUT, produces = "application/json")
	public void AddUser(@PathVariable("id") int FamilyId, @RequestBody User newUser) {
		thisUser.AddUser(FamilyId, newUser);
	}*/
	
	
	
	
}
