package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;

public class TicketServiceImplBaseTest {

    private final Random random = ThreadLocalRandom.current();
    private TicketServiceImpl impl;

    @BeforeEach
    void setUp() {
        impl = new TicketServiceImpl(mock(PricingService.class),
                mock(SeatingCalculatorService.class),
                mock(TicketPaymentService.class),
                mock(SeatReservationService.class));
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
        var tickets = getTicketRequests(4, () -> generateValidTestableTicketRequestMultipleIssueFixedAmount(5));
        impl.purchaseTickets(generateValidTicketPurchaseRequest(tickets));
    }

    @Test
    void failsOnTwentyOneIndividualTickets() {
        var tickets = getTicketRequests(21, this::generateTestableTicketRequestSingleIssue);
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(generateValidTicketPurchaseRequest(tickets)));
    }

    @Test
    void failsOnTwentyOneCollectionTickets() {
        var tickets = getTicketRequests(3, () -> generateValidTestableTicketRequestMultipleIssueFixedAmount(7));
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(generateValidTicketPurchaseRequest(tickets)));
    }

    @Test
    void failsOnNegativeCollectionTickets() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(generateValidTicketPurchaseRequest(
                        List.of(generateInvalidTestableTicketRequestMultipleIssueRandom()))));
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

    @Test
    void failsOnNullValues() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(null));
    }

    @Test
    void failsOnChildWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(new TicketPurchaseRequest(1,
                        List.of(new TicketRequest(TicketRequest.Type.CHILD, 1)))));
    }

    @Test
    void failsOnInfantWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(new TicketPurchaseRequest(1,
                        List.of(new TicketRequest(TicketRequest.Type.CHILD, 1)))));
    }

    @Test
    void failsOnInfantAndChildWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(new TicketPurchaseRequest(1,
                        List.of(new TicketRequest(TicketRequest.Type.CHILD, 1),
                                new TicketRequest(TicketRequest.Type.INFANT, 1)))));
    }

    @Test
    void failsOnMultipleInfantAndChildWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(new TicketPurchaseRequest(1,
                        List.of(new TicketRequest(TicketRequest.Type.CHILD, 2),
                                new TicketRequest(TicketRequest.Type.INFANT, 3)))));
    }

    @Test
    void failsOnThreeChildrenInATrenchCoatWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(new TicketPurchaseRequest(1,
                        List.of(new TicketRequest(TicketRequest.Type.CHILD, 3)))));
    }

    private TicketPurchaseRequest generateValidTicketPurchaseRequest(List<TicketRequest> ticketRequest) {
        return new TicketPurchaseRequest(random.nextLong(0, Long.MAX_VALUE), ticketRequest);
    }

    private TicketPurchaseRequest generateInvalidTicketPurchaseRequest(List<TicketRequest> ticketRequest) {
        return new TicketPurchaseRequest(random.nextLong(Long.MIN_VALUE, 0), ticketRequest);
    }

    private TicketRequest generateTestableTicketRequestSingleIssue() {
        return new TicketRequest(TicketRequest.Type.ADULT, 1);
    }

    private TicketRequest generateValidTestableTicketRequestMultipleIssueFixedAmount(int noOfTicketsFixed) {
        return new TicketRequest(TicketRequest.Type.ADULT, noOfTicketsFixed);
    }

    private TicketRequest generateInvalidTestableTicketRequestMultipleIssueRandom() {
        return new TicketRequest(TicketRequest.Type.ADULT, random.nextInt(Integer.MIN_VALUE, 0));
    }

    private ArrayList<TicketRequest> getTicketRequests(int listSize, Supplier<TicketRequest> callable) {
        var tickets = new ArrayList<TicketRequest>(listSize);
        for (int i = 0; i < listSize; i++) {
            tickets.add(callable.get());
        }
        return tickets;
    }

}
