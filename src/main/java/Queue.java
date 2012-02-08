import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author ian hunt
 * @date 2/8/12
 */
public class Queue extends UntypedActor {
    
    final int index;
    final ActorRef baggageScanner;
    final ActorRef bodyScanner;

    public Queue(int index, ActorRef baggageScanner, ActorRef bodyScanner) {
        this.index = index;
        this.baggageScanner = baggageScanner;
        this.bodyScanner = bodyScanner;
    }

    public void onReceive(final Object message) {

    }
}
