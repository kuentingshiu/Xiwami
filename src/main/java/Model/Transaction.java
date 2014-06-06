package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;

public class Transaction {

	@Id
	private String _id;
	
	private int id;
	private String fromUserId;
	private String toUserId;
	private String itemId;
	private Date createdDate;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
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
