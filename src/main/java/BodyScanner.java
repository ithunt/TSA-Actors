import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author ian hunt
 * @date 2/8/12
 * When a passenger arrives, the scanner determines whether the passenger passes or fails inspection
 * (scans fail randomly with a probability of 20%).
 * A â€œreportâ€� message is sent to the security station for the line;
 * the message contains the passenger and the result of the scan/inspection.
 * When a â€œcloseâ€� message is received, it is sent on to the security station and the body scan actor stops itself.
 */
public class BodyScanner extends Scanner {
	protected ActorRef parentQueue;
    public BodyScanner(int index, ActorRef securityStation, ActorRef parentQueue) {
        super(index, securityStation);
        this.parentQueue = parentQueue;
    }

    @Override
    String getPrintableMessage() {
        return null;
    }

	@Override
	protected void performPostScan() {
		parentQueue.tell(new String("Done"));
	}


}
