import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author ian hunt
 * @date 2/8/12
 */
public abstract class Scanner extends UntypedActor {
    final int index;
    final ActorRef securityStation;
    public final static double FAIL_PROBABILITY = 0.2;

    public Scanner(int index, ActorRef securityStation) {
        this.index = index;
        this.securityStation = securityStation;
    }
    
    public void onReceive(final Object message) {

        if(message instanceof Passenger) {
            final Passenger p = (Passenger)message;
            System.out.println(getPrintableMessage(p) + " enters");
            if(Math.random() >= FAIL_PROBABILITY) {

                System.out.println(getPrintableMessage(p) + " passes");
                securityStation.tell(
                        new Report((Passenger)message, true));
            } else {

                System.out.println(getPrintableMessage(p) + " fails");
                securityStation.tell(
                        new Report((Passenger)message, false));
            }
			performPostScan();
        }
        if(message instanceof Close) {
            System.out.println(getPrintableMessage() + "Close received");
            securityStation.tell(message);
            System.out.println(getPrintableMessage() + "Close sent to security");
            getContext().stop();
            System.out.println(getPrintableMessage() + "Closed");
            
        }
    }
    abstract String getPrintableMessage();
    abstract String getPrintableMessage(Passenger p);
    protected abstract void performPostScan();

}
