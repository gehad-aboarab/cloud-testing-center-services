package TestingCenter;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseHelper {
	private static MongoClient mongoClient;
	private static MongoDatabase database;
	
	private static String TIMINGS_TABLE = "testing-center-timings";
	
	public DatabaseHelper(String databaseName) {
		mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://Gehad:Aboarab97@cloud-computing-zqxty.mongodb.net/test?retryWrites=true"));
		database = mongoClient.getDatabase(databaseName);
	}
	
	// Gets all available timings from the database
	public static JSONObject getTimings(String center) {
		MongoCollection<Document> collection = database.getCollection(TIMINGS_TABLE);
		FindIterable<Document> documents = collection.find();
		
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		try {
			for(Document document : documents) {
				if((document.get("company").equals(center) || center.equals("all")) && document.getBoolean("booked") == false) {
					document.remove("_id");
					array.put(document);
				}
			}
			result.put("result", array);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// Gets the customer's booked timing
	public static JSONObject getBookedTiming(String center, String customer_no) {
		MongoCollection<Document> collection = database.getCollection(TIMINGS_TABLE);
		FindIterable<Document> documents = collection.find();

		try {
			for(Document document : documents) {
				if(document.get("company").equals(center) && document.getString("customer-no").equals(customer_no)) {
					document.remove("_id");
					return new JSONObject(document);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new JSONObject();
	}
	
	// Books a timing and returns the customer number generated
	public static JSONObject bookTiming(String center, String day, String month, String year, String time) {
		MongoCollection<Document> collection = database.getCollection(TIMINGS_TABLE);
		FindIterable<Document> documents = collection.find();
		
		String date = day + "/" + month + "/" + year;
		String newTime = new StringBuilder().append(time.charAt(0)).append(time.charAt(1)).append(":").append(time.charAt(2)).append(time.charAt(3)).toString();
		String temp_id;
		try {
			for(Document document : documents) {
				if(document.get("company").equals(center) 
						&& document.getString("date").equals(date) 
						&& document.getString("time").equals(newTime)) {
					temp_id = document.getString("timing-id");

					Bson filter = new Document("timing-id", temp_id);
					Bson book = new Document("booked", true);
					Bson customer_no = new Document("customer-no",temp_id+"a"); 
					
					Bson updateBooking = new Document("$set", book);
					collection.updateOne(filter, updateBooking);

					Bson updateCustomerId = new Document("$set", customer_no);
					collection.updateOne(filter, updateCustomerId);
				}
			}
			
			for(Document document : documents) {
				if(document.get("company").equals(center) 
						&& document.getString("date").equals(date) 
						&& document.getString("time").equals(newTime)) {
					document.remove("_id");
					return new JSONObject(document);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new JSONObject();
	}
	
	// For testing
	public static void main(String[] args) {
		mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://Gehad:Aboarab97@cloud-computing-zqxty.mongodb.net/test?retryWrites=true"));
		database = mongoClient.getDatabase("cloud-computing");
		
//		System.out.println(getTimings("shamil"));
		System.out.println(getBookedTiming("shamil","5cb73a6444c9ab54e4f67f5aa"));
//		System.out.println(bookTiming("17","4","2019","0600"));
	}
}