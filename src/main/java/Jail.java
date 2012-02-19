import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ian hunt
 * @date 2/8/12
 * 
 * The jail knows how many security stations feed it passengers as prisoners.
 * As each passenger message arrives the passenger is added to the set of inmates in the jail.
 * When “close” messages are received from all feeder security stations, the jail stops itself.
 */
public class Jail extends UntypedActor{
    
    private final static String name = "        Jail: ";
    
    final int securityStations;
    final List<Passenger> inmates = new ArrayList<Passenger>();
    
    private int closeReceived = 0;

    public Jail(int securityStations) {
        this.securityStations = securityStations;
    }

    public void onReceive(final Object message) {
        if(message instanceof Passenger) {
            System.out.println(name + ((Passenger) message).name + " jailed.");
            inmates.add((Passenger)message);
        }
        if(message instanceof Close) {
            closeReceived++;
            System.out.println(name + "Close received (" + closeReceived + " of " + securityStations + " lines)");
            if(closeReceived == securityStations) {
                System.out.println(name + "Incarcerated Passengers");
                for(Passenger p : inmates)
                    System.out.println("              " + p.name);
                System.out.println(name + "Closed");
                getContext().stop();
            }
                
        }
    }
}
