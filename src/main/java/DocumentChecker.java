import akka.actor.UntypedActor;

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

    public DocumentChecker(List<Queue> lineQueues) {
        this.lineQueues = lineQueues;
    }
}
