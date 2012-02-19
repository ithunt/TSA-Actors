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
    
    private final String name;
    final int index;
    final ActorRef baggageScanner;
    
    final ActorRef bodyScanner;
    protected boolean scannerReady;
    //Using linked list to use remove() method without parameters
    protected LinkedList<Object> waitQueue;
    
    protected boolean keepRunning;

    public Queue(int index, ActorRef baggageScanner, ActorRef bodyScanner) {
        this.index = index;
        this.baggageScanner = baggageScanner;
        this.bodyScanner = bodyScanner;
        scannerReady = true;
        waitQueue = new LinkedList<Object>();
        this.name = "  Queue " + index + ": ";
        
    }

    public void onReceive(final Object message) {
    	if(message instanceof Passenger) {
            System.out.println(name + ((Passenger) message).name + " arrives in line.");
    		baggageScanner.tell(message);
            System.out.println(name + ((Passenger) message).name + " baggage placed on scanner");
    		if(scannerReady){
                System.out.println(name + ((Passenger) message).name + " enters body scanner");
    			bodyScanner.tell(message, getContext());
    			scannerReady=false;
    		}
    		else{
    			waitQueue.add((Passenger)message);
    		}
    	}
    	else if(message instanceof Next){
    		if(!waitQueue.isEmpty()){
                final Object msg = waitQueue.remove();
                if(msg instanceof Close)
                    closeOut(msg);
                else {
                    System.out.println(name + ((Passenger) msg).name + " enters body scanner");
                    bodyScanner.tell(msg, getContext());
                }
    		} else{
    			scannerReady = true;
    		} 
    	}
    	else if(message instanceof Close) {
            System.out.println(name + "Close received");
            baggageScanner.tell(message);
            System.out.println(name + "Close sent to baggage scanner");
            if(waitQueue.isEmpty()) {
                closeOut(message);
            } else {
                waitQueue.add(message);
            }

    	}

    }
    
    private void closeOut(Object message) {
        bodyScanner.tell(message);
        System.out.println(name + "Close sent to body scanner");
        getContext().stop();
        System.out.println(name + "Closed");
    }
}
