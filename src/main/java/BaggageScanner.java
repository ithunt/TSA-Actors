import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author ian hunt
 * @date 2/8/12
 *
 * When a passenger arrives (representing the passengerâ€™s baggage),
 * the scanner determines whether the baggage passes or fails inspection
 * (scans fail randomly with a probability of 20%).
 * A â€œreportâ€� message is sent to the security station for the line;
 * the message contains the passenger and the result of the scan/inspection.
 * When a â€œcloseâ€� message is received, it is sent on to the security station and the baggage scan actor stops itself.
 */
public class BaggageScanner extends Scanner{
    public BaggageScanner(int index, ActorRef securityStation) {
        super(index, securityStation);
    }

    @Override
    String getPrintableMessage() {
        return "    Baggage Scan " + index + ": ";
    }

    @Override
    String getPrintableMessage(Passenger p) {
        return getPrintableMessage() + p.name + " baggage";
    }

	@Override
	protected void performPostScan() {
		//nothing for baggage
	}

}
