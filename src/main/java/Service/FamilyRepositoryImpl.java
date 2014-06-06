package Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.aggregation.Aggregation;
//import org.springframework.data.mongodb.core.aggregation.Field;
//import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import Model.Activity;
import Model.Family;
import Model.Item;
import Model.Transaction;
import Model.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Service
public class FamilyRepositoryImpl implements FamilyRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
//	REST API: /families
//	HTTP verb: GET
	public List<Family> GetFamilies() throws Exception {
		
		return (mongoTemplate.findAll(Family.class, "Family"));
	}	
	
//	REST API: /family/id
//	HTTP verb: GET
	@Override
	public Family GetFamilyById(int id) throws Exception {

		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("id").is(id));
		myQuery.fields().exclude("description");
		myQuery.fields().exclude("members.facebookId");
		myQuery.fields().exclude("members.items");
		
		//String result = mongoTemplate.findOne(myQuery, String.class, "Family");
		//System.out.println("Result = " + result); 
		
		//return (new SingleFamilyWrapper(mongoTemplate.findOne(myQuery, Family.class,"Family")));
		return mongoTemplate.findOne(myQuery, Family.class, "Family");
//		Query myQuery = new Query();
//		myQuery.addCriteria(Criteria.where("id").is(id));
//		myQuery.fields().exclude("description");
//		myQuery.fields().exclude("members.facebookId");
//		Family getFamily = mongoTemplate.findOne(myQuery, Family.class,"Family");
//		DBObject getFamilyDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(getFamily);

//		return (new SingleFamilyWrapper(mongoTemplate.findOne(myQuery, Family.class,"Family")));			
		
	}		

//	REST API: /family/id/edit
//	HTTP verb: GET		
	@Override
	public Family GetFamilyByIdEdit(int id) throws Exception {

		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("id").is(id));
		myQuery.fields().exclude("members");

		return (mongoTemplate.findOne(myQuery, Family.class,"Family"));		
	}		
	
	
//	REST API: /addfamily
//	HTTP verb: PUT	
	@Override
	public void AddFamily(Family newFamily) throws Exception {
		
		mongoTemplate.save(newFamily,"Family");
	}
	
//	REST API: /family/id/edit
//	HTTP verb: POST		
	@Override
	public void UpdateFamily(int id, Family updatedFamily) throws Exception {
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("id").is(id));

		DBObject updatedFamilyDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(updatedFamily);
		updatedFamilyDBObject.removeField("_id");
		updatedFamilyDBObject.removeField("members");
		Update setUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedFamilyDBObject));
		//mongoTemplate.updateFirst(myQuery, setUpdate, Family.class);
		mongoTemplate.updateFirst(myQuery, setUpdate, Family.class, "Family");
	}

//	REST API: /family/id/addperson
//	HTTP verb: PUT		
	@Override
	public void AddFamilyPerson(int id, User newUser) throws Exception {
		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.updateFirst(myQuery, new Update().push("members", newUser), Family.class,"Family");
		mongoTemplate.save(newUser, "User");
	}
	
//	REST API: /family/id/person/id/edit
//	HTTP verb: GET	
	@Override
	public User FindPersonByFamilyIdAndUserId(int familyId, int userId) throws Exception {

		Query myQuery = new Query();
		myQuery.addCriteria(Criteria.where("id").is(familyId).andOperator(Criteria.where("members.id").is(userId)));
		Family getFamily = mongoTemplate.findOne(myQuery, Family.class,"Family");
		User thisUser = null;
		Iterator<User> userIterator = getFamily.getMembers().iterator();
		while (userIterator.hasNext()) {
			thisUser = (User) userIterator.next();
			if (thisUser.getId() == userId)
				break;
		}
		
		return thisUser;
	}

//	REST API: /family/id/person/id/edit
//	HTTP verb: POST		
	@Override
	public WriteResult UpdatePersonByFamilyIdAndUserId(int familyId, int userId, User updateUser) {
		
		// update Collection "User"
		Query QueryInUser = new Query();
		QueryInUser.addCriteria(Criteria.where("id").is(userId));
		DBObject updatedUserDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(updateUser);
		updatedUserDBObject.removeField("_id");
		updatedUserDBObject.removeField("facebookId");
		Update setUserUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedUserDBObject));
		User getUser = mongoTemplate.findAndModify(QueryInUser, setUserUpdate, User.class, "User");
		
		// If not found, return null;
		if (getUser.getId()!= updateUser.getId())
			return null;
		
		// update Collection "Family"
		Query QueryInFamily = new Query();
		QueryInFamily.addCriteria(Criteria.where("id").is(familyId).andOperator(Criteria.where("members.id").is(userId)));
		Update userUpdate = new Update().set("members.$", getUser);
		return mongoTemplate.updateFirst(QueryInFamily, userUpdate, "Family");

	}
	
//	REST API: family/id/person/id/delete
//	HTTP verb: DELETE	
	@Override
	public WriteResult DeletePersonByFamilyIdAndUserId(int familyId, int userId) {

		// Delete user from the collection "User"
		Query queryInUser = new Query();
		queryInUser.addCriteria(Criteria.where("id").is(userId));
		User deleteUser = mongoTemplate.findAndRemove(queryInUser, User.class, "User");
		
		// Return null, if we cannot find this userId in the collectio "User"
		if (deleteUser.getId() != userId) {
			return null;
		}
		
		// Delete user from the collection "Family"
		BasicDBObject chooseFamily = new BasicDBObject("id",familyId);
		BasicDBObject chooseUser = new BasicDBObject("id",userId);
		BasicDBObject choosemembersElement = new BasicDBObject("members",chooseUser);
		BasicDBObject removemembersElement = new BasicDBObject("$pull",choosemembersElement);
		return mongoTemplate.getDb().getCollection("Family").update(chooseFamily, removemembersElement);		
		
	}
	
// REST API : /family/searchfamily/languages=chinese&fromAge=2&toAge=5
// Parameters : fromAge, toAge,languages
// HTTP verb: GET	
	@Override
	public List<Family> SearchFamily(String languages, int fromAge, int toAge) throws Exception{
			
		Calendar cal = Calendar.getInstance();
				
		Date today = new Date();
		cal.setTime(today);
		cal.add(Calendar.YEAR, -fromAge);
		Date fromDate = cal.getTime();
		
		cal.setTime(today);
		cal.add(Calendar.YEAR, -toAge);
		Date toDate = cal.getTime();
		
		Query queryInFamily = new Query();
		Criteria c = new Criteria().andOperator(Criteria.where("members.languages").is(languages),Criteria.where("members.birthday").gte(toDate),Criteria.where("members.birthday").lte(fromDate));
		queryInFamily.addCriteria(c);

		return (mongoTemplate.find(queryInFamily, Family.class, "Family"));
	}
	
// REST API: /user/id/additem
// HTTP verb: PUT
	@Override
	public WriteResult AddItem (String userFacebookId, Item newItem) {
		
		// Insert new item in the Collection "User"
		Query QueryInUser = new Query();
		QueryInUser.addCriteria(Criteria.where("facebookId").is(userFacebookId));
		WriteResult wr1 = mongoTemplate.updateFirst(QueryInUser, new Update().push("items", newItem), User.class,"User");
		
		// Insert new item in the Collection "Family"
		Query QueryInFamily = new Query();
		QueryInFamily.addCriteria(Criteria.where("members.facebookId").is(userFacebookId));	
		WriteResult wr2 = mongoTemplate.updateFirst(QueryInFamily, new Update().push("members.$.items", newItem), Family.class,"Family");
		
		// If either collections failed insertion, return null
		if ( (wr1 ==null) || (wr2 == null))
			return null;
		else 
			return wr2;		
	}
	
//	REST API: /user/userFacebookId/edititem
//	HTTP verb: POST
	@Override
	public WriteResult EditItem (String userFacebookId, Item newItem) {
		
		// Update the item in the Collection "User"
		Query QueryInUser = new Query();
		QueryInUser.addCriteria(Criteria.where("facebookId").is(userFacebookId).andOperator(Criteria.where("items.id").is(newItem.getId())));
		WriteResult wr1 = mongoTemplate.updateFirst(QueryInUser, new Update().set("items.$", newItem), User.class,"User");
		
		// Update the item in the Collection "Family"
		// Note : currently, mongodB is unable to update element in the multi-nested array
		//        MongoDB does not support multiple use of positional operator $
		// (1) find family object from the collection "Family"
		// (2) modify this family object by modifying item
		// (3) save the modified family object into collection "Family"
		Query QueryInFamily = new Query();
		QueryInFamily.addCriteria(Criteria.where("members.facebookId").is(userFacebookId));
		Family getFamily = mongoTemplate.findOne(QueryInFamily, Family.class, "Family");		
		outerloop:
		for (ListIterator<User> userIter = getFamily.getMembers().listIterator(); userIter.hasNext();) {			
			if (userIter.next().getFacebookId().equals(userFacebookId)) {
				userIter.previous();
				for (ListIterator<Item> itemIter = userIter.next().getItems().listIterator(); itemIter.hasNext();) {					
					if (itemIter.next().getId() == newItem.getId()) {
						itemIter.remove();
						itemIter.add(newItem);
						break outerloop;
					}
				}
			}
		}

		DBObject updatedFamilyDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(getFamily);
		updatedFamilyDBObject.removeField("_id"); 		// MongoDB does not allow user to create & store ObjectId
		Update setUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedFamilyDBObject));		
		WriteResult wr2 = mongoTemplate.upsert(QueryInFamily, setUpdate, Family.class, "Family");		
		
		// If either collections failed insertion, return null
		if ( (wr1 ==null) || (wr2 == null))
			return null;
		else 
			return wr2;	
	}
	
	
//	REST API: /user/id/items?status=xx&type=yy
//	HTTP verb: GET	
	@Override
	public List<Item> getItem (String userFacebookId, String status, String type) {

/*		// Retrieve items from the "Family" collection
		Query QueryInFamily = new Query();
		//QueryInFamily.addCriteria(Criteria.where("members.facebookId").is(userFacebookId).elemMatch(Criteria.where("members.items.status").is(status).and("members.items.type").is(type)));
		QueryInFamily.addCriteria(Criteria.where("members.facebookId").is(userFacebookId));
		Family getFamily = mongoTemplate.findOne(QueryInFamily, Family.class, "Family");
		
		List<Item> foundItems = new ArrayList<Item>();
		outerloop:
		for (Iterator<User> userIter = getFamily.getMembers().iterator(); userIter.hasNext();) {
			User thisUser = userIter.next();
			if (thisUser.getFacebookId().equals(userFacebookId)) {
				for (Iterator<Item> itemIter = thisUser.getItems().iterator(); itemIter.hasNext();) {
					Item thisItem = itemIter.next();
					if ((thisItem.getStatus().equals(status) || (status==null)) && (thisItem.getType().equals(type) || (type==null))) {
						foundItems.add(thisItem);
					}															
				}
				
				break outerloop;
			}
		}
		
		return foundItems;	*/		
		
		Query QueryInFamily = new Query();
		Criteria c = new Criteria().where("members.facebookId").is(userFacebookId);
		Criteria d = new Criteria().where("members").elemMatch(Criteria.where("items").elemMatch(Criteria.where("status").is(status)));
		Criteria e = new Criteria().where("members").elemMatch(Criteria.where("items").elemMatch(Criteria.where("type").is(type)));
		Criteria f = c.andOperator(d.andOperator(e));
		QueryInFamily.addCriteria(f);
		Family getFamily = mongoTemplate.findOne(QueryInFamily, Family.class, "Family");
		
		List<Item> foundItems = new ArrayList<Item>();
		outerloop:
		for (Iterator<User> userIter = getFamily.getMembers().iterator(); userIter.hasNext();) {
			User thisUser = userIter.next();
			if (thisUser.getFacebookId().equals(userFacebookId)) {
				for (Iterator<Item> itemIter = thisUser.getItems().iterator(); itemIter.hasNext();) {
					Item thisItem = itemIter.next();
					if ((thisItem.getStatus().equals(status) || (status==null)) && (thisItem.getType().equals(type) || (type==null))) {
						foundItems.add(thisItem);
					}															
				}
				
				break outerloop;
			}
		}
		
		return foundItems;
		
/*		Fields f = new Fields(null);
		
		
		Aggregation agg = Aggregation.newAggregation(
		Aggregation.unwind("members"),
		Aggregation.unwind("members.items"),
		Aggregation.match(Criteria.where("members.items.status").is(status).andOperator(Criteria.where("members.items.type").is(type))),
		Aggregation.project(fields),
		Aggregation.pr
				);*/
		
		
		
	}
	
//	REST API: /user/id/deleteitem
//	HTTP verb: DELETE	
	@Override
	public WriteResult DeleteItem(String userFacebookId, Item deleteItem) {
			
		// delete the item in the Collection "User"
		Query QueryInUser = new Query();
		QueryInUser.addCriteria(Criteria.where("facebookId").is(userFacebookId));
		WriteResult wr1 = mongoTemplate.upsert(QueryInUser, new Update().pull("items", deleteItem), User.class,"User");
	
		// delete the item in the Collection "Family"
//		Query QueryInFamily = new Query();
//		QueryInUser.addCriteria(Criteria.where("members.facebookId").is(userFacebookId));
//		WriteResult wr2 = mongoTemplate.upsert(QueryInFamily, new Update().pull("members.items", deleteItem), Family.class,"Family");		
		
		// delete the item in the Collection "Family"
		// Note : currently, mongodB is unable to delete element from the multi-nested array
		//        MongoDB does not support multiple use of positional operator $
		// (1) find family object from the collection "Family"
		// (2) modify this family object by modifying item
		// (3) save the modified family object into collection "Family"
		Query QueryInFamily = new Query();
		QueryInFamily.addCriteria(Criteria.where("members.facebookId").is(userFacebookId));
		Family getFamily = mongoTemplate.findOne(QueryInFamily, Family.class, "Family");		
		outerloop:
		for (ListIterator<User> userIter = getFamily.getMembers().listIterator(); userIter.hasNext();) {			
			if (userIter.next().getFacebookId().equals(userFacebookId)) {
				userIter.previous();
				for (ListIterator<Item> itemIter = userIter.next().getItems().listIterator(); itemIter.hasNext();) {					
					if (itemIter.next().getId() == deleteItem.getId()) {
						itemIter.remove();
						break outerloop;
					}
				}
			}
		}

		DBObject updatedFamilyDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(getFamily);
		updatedFamilyDBObject.removeField("_id"); 		// MongoDB does not allow user to create & store ObjectId
		Update setUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedFamilyDBObject));		
		WriteResult wr2 = mongoTemplate.upsert(QueryInFamily, setUpdate, Family.class, "Family");		
		
		// If either collections failed insertion, return null
		if ( (wr1 ==null) || (wr2 == null))
			return null;
		else 
			return wr2;			
		
	}

//	REST API: /items
//	HTTP verb: GET
	public List<Item> GetItems() throws Exception {

		List<Item> foundItems = new ArrayList<Item>();
		List<Family> families = new ArrayList<Family>();
		families = mongoTemplate.findAll(Family.class, "Family");	

		for (Iterator<Family> familyIter = families.iterator(); familyIter.hasNext();) {			
			Family thisFamily = familyIter.next();
			for (Iterator<User> userIter = thisFamily.getMembers().iterator(); userIter.hasNext();) {				
				User thisUser = userIter.next();
				for (Iterator<Item> itemIter = thisUser.getItems().iterator(); itemIter.hasNext();) {
						//Item thisItem = itemIter.next();
						foundItems.add(itemIter.next());	
				}
			}
		}
		
		return foundItems;					
	}		
	
//	REST API: /transaction
//	HTTP verb: PUT
	@Override
	public void userTransaction(Transaction transaction) {

		mongoTemplate.save(transaction,"Transaction");
	}
	
//	REST API: /user/id/transaction
//	HTTP verb: GET	
	@Override
	public List<Transaction> getTransaction(String userId) throws Exception {
		
		Query queryByUser = new Query();
		Criteria c = new Criteria().orOperator(Criteria.where("fromUserId").is(userId), Criteria.where("toUserId").is(userId));
		queryByUser.addCriteria(c);
//		queryByUser.addCriteria(Criteria.where("fromUserId").is(userId).orOperator(Criteria.where("toUserId").is(userId)));	
		return mongoTemplate.find(queryByUser, Transaction.class, "Transaction");				
		
	}
	
//	REST API: /activity
//	HTTP verb: PUT	
	@Override
	public void AddActivity(Activity activity) throws Exception {
		
		mongoTemplate.save(activity,"Activity");
	}
	
//	REST API: /activity
//	HTTP verb: POST
	@Override
	public WriteResult EditActivity(Activity updatedActivity) {
	
		Query queryInActivity = new Query();
		queryInActivity.addCriteria(Criteria.where("id").is(updatedActivity.getId()));
		DBObject updatedActivityDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(updatedActivity);
		updatedActivityDBObject.removeField("_id");
		Update setActivityUpdate = Update.fromDBObject(new BasicDBObject("$set",updatedActivityDBObject));
		
		return mongoTemplate.updateFirst(queryInActivity, setActivityUpdate, Activity.class,"Activity");
				
	}
	
//	REST API: /activity
//	HTTP verb: DELETE
	@Override
	public void DeleteActivity(Activity deletedActivity) {
		
		Query queryInActivity = new Query();
		queryInActivity.addCriteria(Criteria.where("id").is(deletedActivity.getId()));
		mongoTemplate.remove(queryInActivity, Activity.class, "Activity");	
		
	}	
	
//	REST API: /activity?location=48105&type=2&fromdate=2014-1-1&todate=2014-1-3
//	HTTP verb: GET
	//@Override
	//public List<Activity> getActivity(String type, String fromDate, String toDate) {
		
//		Calendar cal = Calendar.getInstance();
//		
//		Date today = new Date();
//		cal.setTime(today);
//		cal.add(Calendar.YEAR, -fromage);
//		Date fromDate = cal.getTime();
//		
//		cal.setTime(today);
//		cal.add(Calendar.YEAR, -toage);
//		Date toDate = cal.getTime();
//		
//		Query myQuery = new Query();
//		myQuery.addCriteria(Criteria.where("Kids.birthday").gte(toDate).andOperator(Criteria.where("Kids.birthday").lte(fromDate)));
//		return mongoTemplate.find(myQuery, User.class);
		
	//}

//	REST API: /activities
//	HTTP verb: GET
	public List<Activity> GetActivities() throws Exception{
		
		return  (mongoTemplate.findAll(Activity.class, "Activity"));
	}	
	
}