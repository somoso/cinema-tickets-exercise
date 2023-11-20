package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class TicketServiceHelper {

    private TicketServiceHelper() {
        throw new RuntimeException("Cannot instantiate this class");
    }

    public static TicketPurchaseRequest makeValidRequest(List<TicketRequest> ticketRequest) {
        return new TicketPurchaseRequest(ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE), ticketRequest);
    }

    public static TicketPurchaseRequest makeInvalidRequest(List<TicketRequest> ticketRequest) {
        return new TicketPurchaseRequest(ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, 0), ticketRequest);
    }

    public static TicketPurchaseRequest makeRequestWithId(long accountId, List<TicketRequest> ticketRequests) {
        return new TicketPurchaseRequest(accountId, ticketRequests);
    }

    /**
     * Aims to make a ticket request with a negative number of tickets
     * @return  {@link TicketRequest} object with a nonsensical negative number of tickets.
     */
    public static TicketRequest makeOneInvalidAdultTicket() {
        return new TicketRequest(TicketRequest.Type.ADULT, ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, 0));
    }

    /**
     * Given a list size and a lambda function that provides a {@link TicketRequest}, we will populate each entry in the list
     * with whatever the lambda function provides.
     * @param listSize      Size of list
     * @param callable      Some Supplier that provides a type of {@link TicketRequest}
     * @return              Pre-populated custom {@link TicketRequest} of supplied size
     */
    public static List<TicketRequest> makeCustomRequest(int listSize, Supplier<TicketRequest> callable) {
        var tickets = new ArrayList<TicketRequest>(listSize);
        for (int i = 0; i < listSize; i++) {
            tickets.add(callable.get());
        }
        return tickets;
    }

    /**
     * @return  A cinema ticket request for one adult
     */
    public static TicketRequest makeOneAdultTicket() {
        return new TicketRequest(TicketRequest.Type.ADULT, 1);
    }

    /**
     * @return  A cinema ticket request for one child
     */
    public static TicketRequest makeOneChildTicket() {
        return new TicketRequest(TicketRequest.Type.CHILD, 1);
    }

    /**
     * @return  A cinema ticket request for one infant
     */
    public static TicketRequest makeOneInfantTicket() {
        return new TicketRequest(TicketRequest.Type.INFANT, 1);
    }

    /**
     * @return  A cinema ticket request for many adults
     */
    public static TicketRequest makeMultiAdultTicket(int amount) {
        return new TicketRequest(TicketRequest.Type.ADULT, amount);
    }

    /**
     * @return  A cinema ticket request for many children
     */
    public static TicketRequest makeMultiChildTicket(int amount) {
        return new TicketRequest(TicketRequest.Type.CHILD, amount);
    }

    /**
     * @return  A cinema ticket request for many infants
     */
    public static TicketRequest makeMultiInfantTicket(int amount) {
        return new TicketRequest(TicketRequest.Type.INFANT, amount);
    }
}
