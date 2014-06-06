package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class User {

	@Id
	private ObjectId _id; 
	
	private int id;
	private String facebookId;
	private String lastName;
	private String firstName;
	private String nickName;
	private String email;
	private Date birthday;
	private List<String> languages = new ArrayList<String>();
	private int type;
	private String gender;
	private String avatarUrl;
	private String familyId;
	private Date createdDate;
	private List<Item> items = new ArrayList<Item>();
	
	public User() {
		_id = new ObjectId();
	}
	
	public void set_id(String _id) {
		this._id = ObjectId.massageToObjectId(_id);
	}

	public String get_id() {
		return _id.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		return formatter.format(birthday);
		//return "1980/1/1";
	}

	public void setBirthday(String birthday) {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		Date birthdayDate = new Date();
		try {	 
			birthdayDate = formatter.parse(birthday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.birthday = birthdayDate; 
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages (ArrayList<String> myLanguage) {	
		languages = myLanguage;	
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getCreatedDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		return formatter.format(createdDate);
	}

	public void setCreatedDate(String createdDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		try {	 
			Date mycreatedDate = formatter.parse(createdDate);
			this.createdDate = mycreatedDate; 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	
}
