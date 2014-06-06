package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;

public class Activity {
	
	@Id
	private String _id;
	
	private int id;
	private String creatorId;
	private String name;
	private String description;
	private Date fromDate = new Date(0);
	private Date toDate = new Date(0);
	private String fromTime;
	private String toTime;
	private String location;
	private String type;
	private String orginalLink;
	private String imageUrl;
	private Date createdDate = new Date(0);
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrginalLink() {
		return orginalLink;
	}

	public void setOrginalLink(String orginalLink) {
		this.orginalLink = orginalLink;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getFromDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		return formatter.format(fromDate);
	}
	
	public void setFromDate(String fromDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		try {	 
			Date myFromDate = formatter.parse(fromDate);
			this.fromDate = myFromDate; 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String getToDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		return formatter.format(toDate);
	}
	
	public void setToDate(String toDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd"); 
		try {	 
			Date myToDate = formatter.parse(toDate);
			this.toDate = myToDate; 
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
	
	
	
	
}
