/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thing;

/**
 *
 * @author Muszka
 */
import org.slf4j.LoggerFactory;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import org.slf4j.Logger;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;

@ThingworxPropertyDefinitions(properties = {
    @ThingworxPropertyDefinition(name = "temperature", description = "", baseType = "NUMBER",
            aspects = {"dataChangeType:ALWAYS",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:FALSE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:0"})
    ,
		@ThingworxPropertyDefinition(name = "humidity", description = "", baseType = "STRING",
            aspects = {"dataChangeType:ALWAYS",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:FALSE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:0"})
    ,
		@ThingworxPropertyDefinition(name = "heating", description = "", baseType = "STRING",
            aspects = {"dataChangeType:ALWAYS",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:FALSE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:0"})
    ,
		@ThingworxPropertyDefinition(name = "airconditioning", description = "", baseType = "STRING",
            aspects = {"dataChangeType:ALWAYS",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:FALSE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:0"})
    ,
		@ThingworxPropertyDefinition(name = "busy", description = "", baseType = "STRING",
            aspects = {"dataChangeType:ALWAYS",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:FALSE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:wow"})
})

public class RoomSmartOffice extends VirtualThing implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RoomSmartOffice.class);

    private String thingName = null;
    private Double temperature;
    private Double humidity;
    private Boolean heating;
    private Boolean airconditioning;
    private Boolean busy;
    private String TEMPERATURE_FIELD = "temperature";
    private String HUMIDITY_FIELD = "humidity";
    private String HEATING_FIELD = "heating";
    private String AIRCONDITIONING_FIELD = "airconditioning";
    private String BUSY_FIELD = "busy";

    public RoomSmartOffice(String name, String description, ConnectedThingClient client) {
        super(name, description, client);
        thingName = name;
        // Populate the thing shape with the properties, services, and events that are annotated in this code
        super.initializeFromAnnotations();
        this.init();
    }

    // From the VirtualThing class
    // This method will get called when a connect or reconnect happens
    // Need to send the values when this happens
    // This is more important for a solution that does not send its properties on a regular basis
    public void synchronizeState() {
        // Be sure to call the base class
        super.synchronizeState();
        // Send the property values to ThingWorx when a synchronization is required
        super.syncProperties();
    }

    private void init() {
        temperature = new Double(20);
        humidity = new Double(20);
        heating = false;
        airconditioning = false;
        busy = false;
    }

    @Override
    public void run() {
        try {
            // Delay for a period to verify that the Shutdown service will return
            Thread.sleep(1000);
            // Shutdown the client
            this.getClient().shutdown();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }

    }

    @Override
    public void processScanRequest() throws Exception {
       temperature = temperature + (Math.random()*20 - 10)/20;
       humidity = humidity + (Math.random()*20 - 10)/20;
       if (temperature < 10){
           temperature = new Double(15);
       }
       else if (temperature > 30){
           temperature = new Double(30);
       }
       if (humidity < 1){
           humidity = new Double(1);
       }
       else if (humidity > 99){
           humidity = new Double(99);
       }
       setTemperature();
       setHumidity();
       heating = Math.random() < 0.5;
       airconditioning = Math.random() < 0.5;
       busy = Math.random() < 0.5;
       setHeating();
       setAirConditioning();
       setBusy();
       this.updateSubscribedProperties(5000);
    }

    public void setTemperature() throws Exception{
        setProperty(TEMPERATURE_FIELD, this.temperature);
    }
    public void setHumidity() throws Exception{
        setProperty(HUMIDITY_FIELD, this.humidity);
    }
    public void setHeating() throws Exception{
        setProperty(HEATING_FIELD, this.heating);
    }
    public void setAirConditioning() throws Exception{
        setProperty(AIRCONDITIONING_FIELD, this.airconditioning);
    }
    public void setBusy() throws Exception{
        setProperty(BUSY_FIELD, this.busy);
    }

}
