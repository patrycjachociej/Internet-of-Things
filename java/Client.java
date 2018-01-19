/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
import com.thingworx.types.collections.ValueCollection;
import thing.Room;
import thing.RoomSmartOffice;

public class Client extends ConnectedThingClient{

	
	private static final Logger LOG = LoggerFactory.getLogger(Client.class);
	
	public Client(ClientConfigurator config) throws Exception {
		super(config);
	}
	
	public static void startConnection(){
		ClientConfigurator config = new ClientConfigurator();
		
		LOG.info("START");
		config.setUri("ws://127.0.0.1:8888/Thingworx/WS"); //CO TUTAJ DAĆ?
		config.setAppKey("58da657a-970b-496f-936e-42e14201988d");
		config.ignoreSSLErrors(true);
		try {
			Client client = new Client(config);
			
			
			client.start();
			
			while(!client.getEndpoint().isConnected()) {
				Thread.sleep(1000);
				LOG.info("WAIT");
			}
                        
                        for (Room room : Room.values()){
                            ValueCollection params = new ValueCollection();
                            params.SetStringValue("RoomName", room.name);
                            client.invokeService(ThingworxEntityTypes.Things, "SmartCreator2", "CreateRoom", params, 500);
                        }
                        
                        for (Room room : Room.values()){
                            RoomSmartOffice thing1 = new RoomSmartOffice(room.name, "", client);
                            client.bindThing(thing1);
                        }
			
			while(!client.isShutdown()) {
				// Loop over all the Virtual Things and process them
				if(client.isConnected()) {
					LOG.info("SEND");
					for(VirtualThing thing : client.getThings().values()) {
						try {
							thing.processScanRequest();
						}
						catch(Exception eProcessing) {
							System.out.println("Error Processing Scan Request for [" + thing.getName() + "] : " + eProcessing.getMessage());
						}
					}
					LOG.info("SLEEP");
					Thread.sleep(5000);
				}
			}
			LOG.info("END");
			
			
		} catch (Exception e) {
			LOG.info("ERROR");
			e.printStackTrace();
		}
		
	}
	

}
