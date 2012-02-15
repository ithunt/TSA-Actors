/**
 * @author ian hunt
 * @date 2/8/12
 */
public class Main {
    
    public static void main(final String[] args) 
	{
		List<Queue> queues = new List<Queue>(); 
		
		//create the Jail
		theJail = actorOf(
			new UntypedActorFactory()
			{
				public UntypedActor create()
				{
					return new Jail(3);
				}
			}
		);
		
		for(int i = 0; i < 3; i++)
		{
			aSecurityStation = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new SecurityStation(i, theJail);
					}
				}
			);
			aBaggageScanner = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new BaggageScanner(i, aSecurityStation);
					}
				}
			);
			aBodyScanner = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new BodyScanner(i, aSecurityStation, /*ummm */);
					}
				}
			);
			aQueue = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new Queue(i, aBaggageScanner, aBodyScanner);
					}
				}
			);
			queues.add(aQueue);
		}
		
		theDocumentChecker = actorOf(
				new UntypedActorFactory()
				{
					public UntypedActor create()
					{
						return new DocumentScanner(queues);
					}
				}
			);
		
		
		
		
    }
}
