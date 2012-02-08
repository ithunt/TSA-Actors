import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author ian hunt
 * @date 2/8/12
 *
 * When a passenger arrives (representing the passenger’s baggage),
 * the scanner determines whether the baggage passes or fails inspection
 * (scans fail randomly with a probability of 20%).
 * A “report” message is sent to the security station for the line;
 * the message contains the passenger and the result of the scan/inspection.
 * When a “close” message is received, it is sent on to the security station and the baggage scan actor stops itself.
 */
public class BaggageScanner extends UntypedActor{
    
    final int index;
    final ActorRef securityStation;

    public BaggageScanner(int index, ActorRef securityStation) {
        this.index = index;
        this.securityStation = securityStation;
    }
}
