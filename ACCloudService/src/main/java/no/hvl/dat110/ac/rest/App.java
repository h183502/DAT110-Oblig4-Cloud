package no.hvl.dat110.ac.rest;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service

		get("/accessdevice/log/", ((request, response) -> accesslog.toJson()));;

		post("/accessdevice/log/", ((request, response) -> {

			Gson gson = new Gson();
			AccessMessage msg = gson.fromJson(request.body(), AccessMessage.class);
			accesslog.add(msg.getMessage());
			return gson.toJson(accesslog.log);
		}));

		get("/accessdevice/log/{id}", ((request, response) -> {
			Gson gson = new Gson();
			return gson.toJson(accesslog.get(Integer.parseInt(request.params(":id"))));

		}));

		put("/accessdevice/code", ((request, response) -> {
			Gson gson = new Gson();
			accesscode = gson.fromJson(request.body(), AccessCode.class);
			return gson.toJson(accesscode.getAccesscode());

		}));

		get("/accessdevice/code", ((request, response) -> {
			Gson gson = new Gson();
			return gson.toJson(accesscode.getAccesscode());
		}));

		delete("/accessdevice/log", ((request, response) -> {
			accesslog.clear();
			Gson gson = new Gson();
			return gson.toJson("Log has been cleared");
		}));
		
    }
    
}
