package com.javahash.jersey;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
 
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
 
 String uri="";
CoapClient client = new CoapClient(uri);

 CoapResponse response;
/**
 * Method handling HTTP GET requests. The returned object will be sent to
 * the client as "text/plain" media type.
 *
 * @return String that will be returned as a text/plain response.
 */
 @GET
 @Produces(MediaType.TEXT_PLAIN)
 public String ping() {
 	client.post("query", MediaTypeRegistry.TEXT_PLAIN);
 return "Hey, This is Jersey JAX-RS !";
 }
 
}