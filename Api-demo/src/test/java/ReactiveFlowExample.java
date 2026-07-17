import java.util.concurrent.Flow.*;
import java.util.concurrent.SubmissionPublisher;
import java.util.List;

public class ReactiveFlowExample {
    public static void main(String[] args) throws InterruptedException {
        // 1. Create a Publisher
        try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {

            // 2. Create and register a Subscriber
            Subscriber<String> subscriber = new Subscriber<>() {
                private Subscription subscription;

                @Override
                public void onSubscribe(Subscription subscription) {
                    this.subscription = subscription;
                    // Request the first item
                    subscription.request(1);
                }

                @Override
                public void onNext(String item) {
                    System.out.println("Received: " + item);
                    // Request the next item (Backpressure control)
                    subscription.request(1);
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onComplete() {
                    System.out.println("Done!");
                }
            };

            publisher.subscribe(subscriber);

            // 3. Publish items
            System.out.println("Publishing items...");
            List.of("Apple", "Banana", "Cherry").forEach(publisher::submit);

            // Allow time for async processing to finish
            Thread.sleep(1000);
        }
    }
}