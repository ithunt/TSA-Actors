import akka.actor.ActorRef;

/**
 * @author ian hunt
 * @date 2/18/12
 * A container to hold the Queue, bodyscanner, baggage scanner, and the Security 
 * Station Actors.  Allows for easy starting of all the actors
 */
public class Line {

    public final int index;
    public ActorRef queue;
    public ActorRef bodyScanner;
    public ActorRef baggageScanner;
    public ActorRef security;

    public Line(int index) {
        this.index = index;
    }

    public void start() {
        queue.start();
        bodyScanner.start();
        baggageScanner.start();
        security.start();
    }
}
