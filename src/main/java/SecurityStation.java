import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author ian hunt
 * @date 2/8/12
 * 
 * Each security station knows the index of the line it is in (for printing).
 * The security station must be prepared to have either the body scanner or the baggage scanner get ahead of the other 
 *      by an arbitrary amount - that is, it must remember results until both scan reports for a passenger arrive.
 * If a passenger passes both scans, the passenger leaves the system. Otherwise, 
 *  the passenger is passed on as a message to the “jail.”
 * The security station cannot close until both of the scanners attached have sent a “close” message. 
 *  When this condition is met, the station sends a “close” message to the jail and stops itself.
 */
public class SecurityStation extends UntypedActor {
    
    final int index;
    final ActorRef jail;

    public SecurityStation(int index, ActorRef jail) {
        this.index = index;
        this.jail = jail;
    }
}
