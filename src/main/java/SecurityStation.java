import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.Map;

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
    final Map<Passenger, Report> reports = new HashMap<Passenger, Report>();
    boolean keepRunning = true;

    public SecurityStation(int index, ActorRef jail) {
        this.index = index;
        this.jail = jail;
    }

    public void onReceive(final Object message) {
        if(message instanceof Report) {
            final Report r = (Report)message;

            System.out.println("      Security " + index + ": " + r.p.name + " report received " +
                    ( r.b ? "(pass)" : "(fail)" ));

            if(reports.containsKey(r.p) && ((Report)message).b && reports.get(r.p).b) {
                System.out.println("      Security " + index + ": " + r.p + " (pass/pass) released to airport");
                reports.remove(r.p);
            } else if (!reports.containsKey(r.p)) {
                reports.put(r.p, r);                
            } else {
                System.out.println("      Security " + index + ": " + r.p.name + "sent to jail");
                jail.tell(r.p);
            }
        } else if (message instanceof Close) {
            keepRunning = false;
        }

        if(!keepRunning && reports.isEmpty()) getContext().stop();
    }
}
