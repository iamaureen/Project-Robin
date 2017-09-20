import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.simple.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;



//setup ref: http://java2db.com/web-services/call-php-restful-web-service-in-java-client

public class RobinPHPService {

	public void getResponse(){
		
		 Client client = Client.create();
		 WebResource webResource = client.resource("http://localhost/api/product/read.php");
		 
		 ClientResponse response = webResource.accept("").get(ClientResponse.class);
		 if (response.getStatus() != 200) {
		 throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		 }
		 String output2 = response.getEntity(String.class);
		 System.out.println("\n============Plain Text Response============");
		 System.out.println(output2);
		 
	}
	
	public void postResponse(int module, String username, String type, String msg){
		
		Client client = Client.create();
		WebResource webResource = client.resource("http://localhost/api/product/create.php");
		
		//build the json
		//https://stackoverflow.com/questions/20117148/how-to-create-json-object-using-string
		JSONObject json = new JSONObject();
		json.put("module", module);
		json.put("username", username);
		json.put("type", type);
		json.put("msg", msg);
		//String jsonInString = "{\"module\":1,\"username\":\"hailey\",\"type\":\"2\",\"msg\":\"Try harder!\"}";
		String jsonInString = json.toJSONString();
		// POST method
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, jsonInString);

        // check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
		
		
	}
}
