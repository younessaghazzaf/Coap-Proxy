package com.javahash.jersey;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
 import javax.ws.rs.QueryParam;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.interceptors.MessageTracer;
/**
 * Root resource (exposed at "helloworld" path)
 */
@Path("helloworld")
public class HelloWorldREST {
 

CoapClient client;

 CoapResponse response;
/**
 * Method handling HTTP GET requests. The returned object will be sent to
 * the client as "text/plain" media type.
 *
 * @return String that will be returned as a text/plain response.
 */
 @GET
 @Produces(MediaType.TEXT_PLAIN)
 public String ping(@QueryParam("ipv6") String to,
					@QueryParam("Params") String param,
					@QueryParam("Value") int val) {
 	int id;
 	String uri = "aaaa::c30c:0:0:"+to;

 	
 	// Config used for plugtest
		NetworkConfig.getStandard()
			.setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE, 64)
			.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, 64);
 	id=Get_Id_Param(param);
 	switch(id){
 		case 1 : System.out.println("set t");
 				client = new CoapClient("coap://["+uri+"]:5683/chenillard/setv1?t="+val);
 				client.post("non-empty", MediaTypeRegistry.TEXT_PLAIN);
 				 break;
 		case 2: System.out.println("set n");
 					break;
 		case 3: System.out.println("Reset");
 					break;
 		case 4: System.out.println("Activate (0/1)");
 				client = new CoapClient("coap://["+uri+"]:5683/chenillard/setv1?activate="+val);
 				client.post("non-empty", MediaTypeRegistry.TEXT_PLAIN);
 					break;
 	}
 	return "Hey, This is Jersey JAX-RS !";
 }

 /*
@Pre : Param name
@Post : Id of Param ==>
{t : 1  , n : 2 , Reset : 3 , Activate : 4}

 */
 private int Get_Id_Param(String param){
 		if(param.compareTo("t")==0) return 1;
 		if(param.compareTo("n")==0) return 2;
 		if(param.compareTo("reset")==0) return 3;
 		if(param.compareTo("activate")==0) return 4;
 		return 0;
 }
}