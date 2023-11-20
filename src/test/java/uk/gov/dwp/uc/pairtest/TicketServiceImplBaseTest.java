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

import static org.mockito.Mockito.mock;
import static uk.gov.dwp.uc.pairtest.TicketServiceHelper.*;

public class TicketServiceImplBaseTest {

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
        impl.purchaseTickets(makeValidRequest(tickets));
    }

    @Test
    void passesOnTenIndividualTickets() {
        var tickets = makeCustomRequest(10, TicketServiceHelper::makeOneAdultTicket);
        impl.purchaseTickets(makeValidRequest(tickets));
    }

    @Test
    void passesOnTwentyIndividualTickets() {
        var tickets = makeCustomRequest(20, TicketServiceHelper::makeOneAdultTicket);
        impl.purchaseTickets(makeValidRequest(tickets));
    }

    @Test
    void passesOnTwentyCollectionTickets() {
        var tickets = makeCustomRequest(4,
                () -> makeMultiAdultTicket(5));
        impl.purchaseTickets(makeValidRequest(tickets));
    }

    @Test
    void failsOnTwentyOneIndividualTickets() {
        var tickets = makeCustomRequest(21, TicketServiceHelper::makeOneAdultTicket);
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeValidRequest(tickets)));
    }

    @Test
    void failsOnTwentyOneCollectionTickets() {
        var tickets = makeCustomRequest(3,
                () -> makeMultiAdultTicket(7));
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeValidRequest(tickets)));
    }

    @Test
    void failsOnNegativeCollectionTickets() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeValidRequest(
                        List.of(makeOneInvalidAdultTicket()))));
    }

    @Test
    void failsOnNegativeAccountId() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeInvalidRequest(
                        List.of(makeOneAdultTicket()))));
    }

    @Test
    void failsOnAccountIdEqualsZero() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeRequestWithId(0,
                        List.of(makeOneAdultTicket()))));
    }

    @Test
    void failsOnNullValues() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(null));
    }

    @Test
    void failsOnChildWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeRequestWithId(1,
                        List.of(makeOneChildTicket()))));
    }

    @Test
    void failsOnInfantWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeRequestWithId(1,
                        List.of(makeOneChildTicket()))));
    }

    @Test
    void failsOnInfantAndChildWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeRequestWithId(1,
                        List.of(makeOneChildTicket(),
                                makeOneInfantTicket()))));
    }

    @Test
    void failsOnMultipleInfantAndChildWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeRequestWithId(1,
                        List.of(makeMultiChildTicket(2),
                                makeMultiInfantTicket(3)))));
    }

    @Test
    void failsOnThreeChildrenInATrenchCoatWithoutAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> impl.purchaseTickets(makeRequestWithId(1,
                        List.of(makeMultiChildTicket(3)))));
    }

}
