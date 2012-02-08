import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author ian hunt
 * @date 2/8/12
 * Each queue knows the index of the line it is in (for printing).
 * When passengers arrive at the queue, they (or their baggage) are immediately sent to the baggage scanner.
 * Passengers can go to the body scanner only when it is ready (its initial state).
 * If ready when a passenger arrives, the passenger is sent to the body scanner.
 * Otherwise the passenger is placed in a FIFO data structure within the queue actor.
 * When the body scanner replies with a “next” message:
 * The body scanner is marked ready if no passengers are waiting.
 * Otherwise, the first passenger is sent into the scanner.
 * A “close” message is sent to the baggage scanner immediately, and to the body scanner if it is in the ready state.
 * If the body scanner is still processing a passenger, and/or if passengers are waiting in the queue,
 * the “close” message must be deferred until all passengers have been through the body scanner.
 * Once a “close” message has been sent to both scanners, the queue actor stops itself.
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
