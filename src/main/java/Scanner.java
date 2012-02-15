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
        //print i recieved something
        if(message instanceof Passenger) {
            if(Math.random() >= FAIL_PROBABILITY) {
                //print i passed it
                securityStation.tell(
                        new Report((Passenger)message, true));
            } else {
                //print i failed it
                securityStation.tell(
                        new Report((Passenger)message, false));
            }
			this.performPostScan();
        }
        if(message instanceof Close) {
            securityStation.tell(message);
            getContext().stop();
        }
    }
    
    abstract String getPrintableMessage();
    protected abstract void performPostScan();

}
