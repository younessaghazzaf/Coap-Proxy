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
System.out.println("coap://["+uri+"]:5683/chenillard/setv1?activate="+val);
 	switch(id){
 		case 1 : System.out.println("set t");
 			envoie("coap://["+uri+"]:5683/chenillard/setv1?t="+val);
 			break;
 		case 2: System.out.println("set n");
			envoie("coap://["+uri+"]:5683/chenillard/setv1?n="+val);
 			break;
 		case 3: System.out.println("Reset");
			envoie("coap://["+uri+"]:5683/chenillard/setv1?reset="+val);
 			break;
 		case 4: System.out.println("Activate (0/1)");
			envoie("coap://["+uri+"]:5683/chenillard/setv1?active="+val);
 			break;
		case 5: System.out.println("current id");
			envoie("coap://["+uri+"]:5683/chenillard/setv1?current="+val);
 			break;
 	}

 	return "POST request sent to "+uri;
 }

 /*
@Pre : Param name
@Post : Id of Param ==>
{t : 1  , n : 2 , Reset : 3 , Activate : 4 , current : 5}

 */
 private int Get_Id_Param(String param){
 		if(param.compareTo("t")==0) return 1;
 		if(param.compareTo("n")==0) return 2;
 		if(param.compareTo("reset")==0) return 3;
 		if(param.compareTo("activate")==0) return 4;
		if(param.compareTo("current")==0) return 5;
 		return 0;
 }

public static void envoie(String uri) {

		// re-usable response object
		CoapResponse response;
		
		CoapClient client = new CoapClient(uri + "/test");
		
		System.out.println("===============\nCC01+10");
		System.out.println("---------------\nGET /test\n---------------");
		response = client.get();
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		System.out.println("===============\nCC02");
		System.out.println("---------------\nDELETE /test\n---------------");
		response = client.delete();
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		System.out.println("===============\nCC03");
		System.out.println("---------------\nPUT /test\n---------------");
		response = client.put("", MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		System.out.println("===============\nCC04");
		System.out.println("---------------\nPOST /test\n---------------");
		response = client.post("non-empty", MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		client.useNONs();
		
		System.out.println("===============\nCC05");
		System.out.println("---------------\nNON-GET /test\n---------------");
		response = client.get();
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		System.out.println("===============\nCC06");
		System.out.println("---------------\nNON-DELETE /test\n---------------");
		response = client.delete();
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		System.out.println("===============\nCC07");
		System.out.println("---------------\nNON-PUT /test\n---------------");
		response = client.put("", MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		System.out.println("===============\nCC08");
		System.out.println("---------------\nNON-POST /test\n---------------");
		response = client.post("non-empty", MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());

		client.setURI(uri + "/separate");
		client.useCONs();

		System.out.println("===============\nCC09+11");
		System.out.println("---------------\nGET /separate\n---------------");
		response = client.get();
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());

		client.setURI(uri + "/test");

		System.out.println("===============\nCC12");
		System.out.println("---------------\nGET /test w/o Token\n---------------");
		Request req12 = Request.newGet(); // never re-use a Request object
		req12.setToken(new byte[0]);
		response = client.advanced(req12);
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		client.setURI(uri + "/seg1/seg2/seg3");

		System.out.println("===============\nCC13");
		System.out.println("---------------\nGET /seg1/seg2/seg3\n---------------");
		response = client.get();
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		client.setURI(uri + "/query?first=1&second=2");

		System.out.println("===============\nCC14");
		System.out.println("---------------\nGET /query?first=1&second=2\n---------------");
		response = client.get();
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());
		
		client.setURI(uri + "/separate");
		client.setTimeout(10000);
		client.useNONs();
		
		System.out.println("===============\nCC17");
		System.out.println("---------------\nNON-GET /separate\n---------------");
		response = client.get();
		System.out.println(response.advanced().getType() + "-" + response.getCode());
		System.out.println(response.getResponseText());

		client.setURI(uri + "/test");
		client.setTimeout(0);
		client.useCONs();
		
		System.out.println("===============\nCC18");
		System.out.println("---------------\nPOST /test for Location-Path\n---------------");
		response = client.post("TD_COAP_CORE_18", MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(response.getCode() + "-" + response.getOptions().getLocationString());
		System.out.println(response.getResponseText());

		client.setURI(uri + "/location-query");
		
		System.out.println("===============\nCC19");
		System.out.println("---------------\nGET /location-query\n---------------");
		response = client.post("query", MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(response.getCode() + "-" + response.getOptions().getLocationString());
		System.out.println(response.getResponseText());
		
		client.setURI(uri + "/multi-format");
		
		System.out.println("===============\nCC20");
		System.out.println("---------------\nGET /multi-format text/plain\n---------------");
		response = client.get(MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(response.getCode() + "-" + MediaTypeRegistry.toString(response.getOptions().getContentFormat()));
		System.out.println(response.getResponseText());
		System.out.println("---------------\nGET /multi-format application/xml\n---------------");
		response = client.get(MediaTypeRegistry.APPLICATION_XML);
		System.out.println(response.getCode() + "-" + MediaTypeRegistry.toString(response.getOptions().getContentFormat()));
		System.out.println(response.getResponseText());

		client.setURI(uri + "/validate");
		byte[] etag;
		
		System.out.println("===============\nCC21");
		System.out.println("---------------\nGET /validate\n---------------");
		response = client.get();
		if (response.getOptions().getETagCount()==1) {
			etag = response.getOptions().getETags().get(0);
			System.out.println(response.getCode() + " - ETag [" + Utils.toHexString(etag) + "]");
			System.out.println(response.getResponseText());

			System.out.println("---------------\nGET /validate with ETag\n---------------");
			response = client.validate(etag);
			etag = response.getOptions().getETags().get(0);
			System.out.println(response.getCode() + " - ETag [" + Utils.toHexString(etag) + "]");
			System.out.println(response.getResponseText());

			System.out.println("---------------\nPUT /validate stimulus\n---------------");
			CoapClient clientStimulus = new CoapClient(uri + "/validate");
			response = clientStimulus.put("CC21 at " + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()), MediaTypeRegistry.TEXT_PLAIN);
			System.out.println(response.getCode());

			System.out.println("---------------\nGET /validate with ETag\n---------------");
			response = client.validate(etag);
			etag = response.getOptions().getETags().get(0);
			System.out.println(response.getCode() + " - ETag [" + Utils.toHexString(etag) + "]");
			System.out.println(response.getResponseText());
			
		} else {
			System.out.println("Error - no ETag");
		}
		
		System.out.println("===============\nCC22");
		System.out.println("---------------\nGET /validate with If-Match\n---------------");
		response = client.get();
		if (response.getOptions().getETagCount()==1) {
			etag = response.getOptions().getETags().get(0);
			System.out.println(response.getCode() + " - ETag [" + Utils.toHexString(etag) + "]");
			System.out.println(response.getResponseText());

			System.out.println("---------------\nPUT /validate If-Match [" + Utils.toHexString(etag) + "]\n---------------");
			response = client.putIfMatch("CC22 at " + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()),
					MediaTypeRegistry.TEXT_PLAIN, etag);
			System.out.println(response.getCode());

			System.out.println("---------------\nGET /validate\n---------------");
			response = client.get();
			etag = response.getOptions().getETags().get(0);
			System.out.println(response.getCode() + " - ETag [" + Utils.toHexString(etag) + "]");
			System.out.println(response.getResponseText());
			
			System.out.println("---------------\nPUT /validate stimulus\n---------------");
			CoapClient clientStimulus = new CoapClient(uri + "/validate");
			response = clientStimulus.put("CC22 at " + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()), MediaTypeRegistry.TEXT_PLAIN);
			System.out.println(response.getCode());
			
			System.out.println("---------------\nPUT /validate If-Match [" + Utils.toHexString(etag) + "]\n---------------");
			response = client.putIfMatch("CC22 at " + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()),
					MediaTypeRegistry.TEXT_PLAIN, etag);
			System.out.println(response.getCode());
			
		} else {
			System.out.println("Error - no ETag");
		}

		client.setURI(uri + "/create1");
		
		System.out.println("===============\nCC23");
		System.out.println("---------------\nPUT /create1 with If-None-Match\n---------------");
		response = client.putIfNoneMatch("CC23 at " + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()),
				MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(response.getCode());
		System.out.println("---------------\nPUT /create1 with If-None-Match\n---------------");
		response = client.putIfNoneMatch("CC23 at " + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()),
				MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(response.getCode());
	}

}
