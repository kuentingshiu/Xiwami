package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Family")
public class Family {

	@Id
	private String _id;

	private int id;
	private String zipcode;
	private String familyName;
	private Date createdDate;
	private String description;
	private List<User> members = new ArrayList<User>();

//	public void set_id(String _id) {
//		this._id = _id;
//	}

	public String get_id() {
		return _id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getCreatedDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		return formatter.format(createdDate);
	}

	public void setCreatedDate(String createdDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");	
		Date myCreatedDate = new Date();
		try {
			myCreatedDate = formatter.parse(createdDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.createdDate = myCreatedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> member) {
		this.members = member;
	}


}