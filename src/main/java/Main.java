import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import java.util.ArrayList;
import java.util.List;

import static akka.actor.Actors.actorOf;

/**
 * @author ian hunt
 * @date 2/8/12
 */
public class Main {
    
    public static final int NUM_LINES = 3;
    public static final int NUM_PASSENGERS = 5;
    
    public static void main(final String[] args) 
	{
        final List<Line> lines = new ArrayList<Line>();
        final List<ActorRef> queues = new ArrayList<ActorRef>();

		//create the Jail
		final ActorRef theJail = actorOf(
			new UntypedActorFactory()
			{
				public UntypedActor create()
				{
					return new Jail(NUM_LINES);
				}
			}
		);
		
		for(int i = 0; i < NUM_LINES; i++)
		{
            final Line l = new Line(i);
            
			l.security = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new SecurityStation(l.index, theJail);
					}
				}
			);
			l.baggageScanner = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new BaggageScanner(l.index, l.security);
					}
				}
			);
			l.bodyScanner = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new BodyScanner(l.index, l.security, l.queue);
					}
				}
			);
			l.queue = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new Queue(l.index, l.baggageScanner, l.bodyScanner);
					}
				}
			);
			lines.add(l);
            queues.add(l.queue);
		}
		
		final ActorRef theDocumentChecker = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new DocumentChecker(queues);
					}
				}
			);

        //Creation complete - go time
        theDocumentChecker.start();
        theJail.start();
        for(Line l : lines ) {
            l.start();
        }
        
        for(int i=0; i<NUM_PASSENGERS; i++) {
            final Passenger p = new Passenger("Passenger " + i);
            theDocumentChecker.tell(p);
        }
        theDocumentChecker.tell(new Close());
    }
}
