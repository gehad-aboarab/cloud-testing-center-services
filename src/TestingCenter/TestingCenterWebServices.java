package TestingCenter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Path("/testing-center/{center}")
//the user enters the car registration as a parameter
public class TestingCenterWebServices {
private static final DatabaseHelper database = new DatabaseHelper("cloud-computing");

	@GET
	@Path("get-timings")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTimings(@PathParam("center") String center) {
		return database.getTimings(center).toString();
	}
	
	@GET
	@Path("booked-timing/{customer-no}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getBookedTiming(@PathParam("center") String center,
			@PathParam("customer-no") String customer_no) {
		return database.getBookedTiming(center, customer_no).toString();
	}
	
	@GET
	@Path("book-timing/{day}/{month}/{year}/{time}")
	@Produces(MediaType.TEXT_PLAIN)
	public String bookTiming(@PathParam("center") String center,
			@PathParam("day") String day,
			@PathParam("month") String month,
			@PathParam("year") String year,
			@PathParam("time") String time) {
		return database.bookTiming(center, day, month, year, time).toString();
	}
}