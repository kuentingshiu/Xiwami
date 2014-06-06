package Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import Model.User;

@Service
public class UserRepositoryImpl implements UserRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;

//  REST API: /user/id
//	HTTP verb: GET
	@Override
	public User GetUserById(int id)throws Exception {
		
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(myQuery, User.class);
	}

/*	@Override
	public void AddUser(int familyId, User newUser) {
		// Save this user into the MongoDB collection "User"
		mongoTemplate.save(newUser,"User");
		
		// Retrieve the family from MongoDb based on family_id
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("id").is(familyId));
		Family thisFamily = mongoTemplate.findOne(myQuery, Family.class, "Family");
		
		// Retrieve and update the family members
		List<User> familyMembers = new ArrayList<User>();
		familyMembers = thisFamily.getMember();
		familyMembers.add(newUser);
		thisFamily.setMember(familyMembers);
		
		// Save the updated family into the MongoDB collection "Family"
		mongoTemplate.save(thisFamily,"Family");
	}*/
}
