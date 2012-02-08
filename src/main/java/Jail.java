import akka.actor.UntypedActor;

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

    public Jail(int securityStations) {
        this.securityStations = securityStations;
    }
}
