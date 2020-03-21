package no.ntnu.sjakkarena.subscriberhandler;

public abstract class SubscriberHandler {

    /**
     * Prints an error message when the user to be notified doesn't subscribe to a service
     * @param serviceDescription A description of the service
     * @param exception
     */
    protected void printNotSubscribingErrorMessage(String serviceDescription, Exception exception){
        System.out.println("User is probably not subscribing to the service providing + " + serviceDescription
                + "\n" + exception);
    }
}
