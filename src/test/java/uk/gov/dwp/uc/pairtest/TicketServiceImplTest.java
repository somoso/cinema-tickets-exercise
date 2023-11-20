package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TicketServiceImplTest {

    private final Random random = ThreadLocalRandom.current();
    private TicketServiceImpl impl;

    private static final TicketRequest.Type[] TYPES = TicketRequest.Type.values();
    private static final int TYPES_LENGTH = TYPES.length;

    @BeforeEach
    void setUp() {
        impl = new TicketServiceImpl();
    }

    @Test
    void passesOnZeroIndividualTickets() {
        var tickets = new ArrayList<TicketRequest>(0);
        impl.purchaseTickets(generateValidTicketPurchaseRequest(tickets));
    }

    @Test
    void passesOnTenIndividualTickets() {
        var tickets = getTicketRequests(10, this::generateTestableTicketRequestSingleIssue);
        impl.purchaseTickets(generateValidTicketPurchaseRequest(tickets));
    }

    @Test
    void passesOnTwentyIndividualTickets() {
        var tickets = getTicketRequests(20, this::generateTestableTicketRequestSingleIssue);
        impl.purchaseTickets(generateValidTicketPurchaseRequest(tickets));
    }

    @Test
    void passesOnTwentyCollectionTickets() {
        var tickets = getTicketRequests(4, () -> generateValidTestableTicketRequestMultipleIssue(5));
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(generateValidTicketPurchaseRequest(tickets)));
    }

    @Test
    void failsOnTwentyOneIndividualTickets() {
        var tickets = getTicketRequests(21, this::generateTestableTicketRequestSingleIssue);
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(generateValidTicketPurchaseRequest(tickets)));
    }

    @Test
    void failsOnTwentyOneCollectionTickets() {
        var tickets = getTicketRequests(3, () -> generateValidTestableTicketRequestMultipleIssue(7));
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(generateValidTicketPurchaseRequest(tickets)));
    }

    @Test
    void failsOnNegativeCollectionTickets() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(generateValidTicketPurchaseRequest(
                        List.of(generateInvalidTestableTicketRequestMultipleIssue()))));
    }

    @Test
    void failsOnNegativeAccountId() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(generateInvalidTicketPurchaseRequest(
                        List.of(generateTestableTicketRequestSingleIssue()))));
    }

    @Test
    void failsOnAccountIdEqualsZero() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(new TicketPurchaseRequest(0,
                        List.of(generateTestableTicketRequestSingleIssue()))));
    }

    private TicketPurchaseRequest generateValidTicketPurchaseRequest(List<TicketRequest> ticketRequest) {
        return new TicketPurchaseRequest(random.nextLong(0, Long.MAX_VALUE), ticketRequest);
    }

    private TicketPurchaseRequest generateInvalidTicketPurchaseRequest(List<TicketRequest> ticketRequest) {
        return new TicketPurchaseRequest(random.nextLong(Long.MIN_VALUE, 0), ticketRequest);
    }

    private TicketRequest generateTestableTicketRequestSingleIssue() {
        return new TicketRequest(TYPES[random.nextInt(TYPES_LENGTH)], 1);
    }

    private TicketRequest generateValidTestableTicketRequestMultipleIssue(int noOfTicketsMaxAmount) {
        return new TicketRequest(TYPES[random.nextInt(TYPES_LENGTH)], random.nextInt(0, noOfTicketsMaxAmount));
    }

    private TicketRequest generateInvalidTestableTicketRequestMultipleIssue() {
        return new TicketRequest(TYPES[random.nextInt(TYPES_LENGTH)], random.nextInt(Integer.MIN_VALUE, 0));
    }

    private ArrayList<TicketRequest> getTicketRequests(int listSize, ICallable<TicketRequest> callable) {
        // Initialising ArrayList with n + 1 so that we do not waste CPU resources growing the ArrayList past the
        // initial limit of 10
        var tickets = new ArrayList<TicketRequest>(listSize + 1);
        for (int i = 0; i < listSize; i++) {
            tickets.add(callable.call());
        }
        return tickets;
    }

    private interface ICallable<T> {
        T call();
    }

}
