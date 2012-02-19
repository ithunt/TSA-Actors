import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import static java.lang.Math.* ;
import java.util.List;

/**
 * @author Zach Masiello
 * @author ian hunt
 * @date 2/8/12
 *
 * Passengers are passed in from the main driver program as messages.
 * Passengers are randomly turned away for document problems at a probability of 20%.
 * Passengers not turned away are passed on to the queue for one of the lines in a cyclic fashion.
 * A close message is passed on to each of the line queues,
 * after which the document check actor terminates [with getContext().stop()].
 */
public class DocumentChecker extends UntypedActor {
	
    private static final String name = "Document Check: ";
    
    final List<ActorRef> lineQueues;
	
	private int i = 0;
	
    public DocumentChecker(List<ActorRef> lineQueues) {
        this.lineQueues = lineQueues;
    }
	
	public void onReceive(final Object message)
	{
		if(message instanceof Passenger)
		{
            System.out.println(name + ((Passenger) message).name + " arrives");
			if((int)(random() * 100) <=20) //20% chance to be sent away
				System.out.println(name + ((Passenger) message).name + " turned away");
			else
			{
                final int line = i % lineQueues.size();
				System.out.println(name + ((Passenger) message).name + " sent to line " + line);
				lineQueues.get(line).tell(message);
				i++;
			}
		}
		else if(message instanceof Close)
		{
			for(ActorRef q : lineQueues)
			{
				q.tell(message);
			}
            System.out.println(name + "Close sent");
            getContext().stop();
            System.out.println(name + "Closed");
		}
	}
}
