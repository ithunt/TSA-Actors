import akka.actor.UntypedActor;
import static java.lang.Math.* ;
import java.util.List;

/**
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

    final List<Queue> lineQueues;

	private int i = 0;
	
    public DocumentChecker(List<Queue> lineQueues) {
        this.lineQueues = lineQueues;
    }
	
	public void onReceive(final Object message)
	{
		if(message instanceof Passenger)
		{
			if((int)(random() * 100) <=20)
			{
				System.out.println("Passenger: " + (Passenger)message.name + "has been sent away");
			}
			else
			{
				System.out.println("Passenger: " + (Passenger)message.name + "has passed Document Check");
				lineQueues[i % lineQueues.size()].tell(message);
				i++;
			}
			
			
		}
		else if(message instanceof Close)
		{
			for(Queue q : lineQueues)
			{
				q.tell(message);
			}
			this.getContext.stop();
		}
	}
}
