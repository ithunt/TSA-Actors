import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ian hunt
 * @author Stephen Yingling
 * @date 2/8/12
 * Each queue knows the index of the line it is in (for printing).
 * When passengers arrive at the queue, they (or their baggage) are immediately sent to the baggage scanner.
 * Passengers can go to the body scanner only when it is ready (its initial state).
 * If ready when a passenger arrives, the passenger is sent to the body scanner.
 * Otherwise the passenger is placed in a FIFO data structure within the queue actor.
 * When the body scanner replies with a â€œnextâ€� message:
 * The body scanner is marked ready if no passengers are waiting.
 * Otherwise, the first passenger is sent into the scanner.
 * A â€œcloseâ€� message is sent to the baggage scanner immediately, and to the body scanner if it is in the ready state.
 * If the body scanner is still processing a passenger, and/or if passengers are waiting in the queue,
 * the â€œcloseâ€� message must be deferred until all passengers have been through the body scanner.
 * Once a â€œcloseâ€� message has been sent to both scanners, the queue actor stops itself.
 */
public class Queue extends UntypedActor {
    
    final int index;
    final ActorRef baggageScanner;
    
    ActorRef bodyScanner;
    protected boolean scannerReady;
    //Using linked list to use remove() method without parameters
    protected LinkedList<Object> waitQueue;
    
    protected boolean keepRunning;

    public Queue(int index, ActorRef baggageScanner, ActorRef bodyScanner) {
        this.index = index;
        this.baggageScanner = baggageScanner;
        this.bodyScanner = bodyScanner;
        scannerReady = true;
        waitQueue = new LinkedList<Passenger>();
    }

    public void onReceive(final Object message) {
    	if(message instanceof Passenger){
    		baggageScanner.tell(message);
    		if(scannerReady){
    			bodyScanner.tell(message);
    			scannerReady=false;
    		}
    		else{
    			waitQueue.add((Passenger)message);
    		}
    	}
    	else if(message instanceof Next){
            final Object msg = waitQueue.remove();
    		if(!waitQueue.isEmpty()){
    			bodyScanner.tell(msg);
                if(msg instanceof Close) getContext().stop();
    		}
    		else{
    			scannerReady = true;
    		} 
    	}
    	else if(message instanceof Close) {
            baggageScanner.tell(message);
            if(waitQueue.isEmpty()) {
                bodyScanner.tell(message);
                getContext().stop();
            } else {
                waitQueue.add(message);
            }

    	}

    }

    /**
     * For setting the body scanner (cyclic dep). Only works if null
     * @param bodyScanner the body scanner
     */
    public void setBodyScanner(ActorRef bodyScanner) {
       if(this.bodyScanner == null)
            this.bodyScanner = bodyScanner;
    }
}
