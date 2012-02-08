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
    
    final int securityStations;
    final List<Passenger> inmates = new ArrayList<Passenger>();
    
    private int closeReceived = 0;

    public Jail(int securityStations) {
        this.securityStations = securityStations;
    }

    public void onReceive(final Object message) {
        if(message instanceof Passenger)
            inmates.add((Passenger)message);
        if(message instanceof Close) {
            closeReceived++;
            if(closeReceived == securityStations)
                getContext().stop();

        }
    }
}
