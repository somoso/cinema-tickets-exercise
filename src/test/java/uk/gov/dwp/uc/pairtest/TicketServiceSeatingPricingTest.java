package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static uk.gov.dwp.uc.pairtest.TicketServiceHelper.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceSeatingPricingTest {

    private static final int ACCOUNT_ID = 1234;

    @Mock
    private PricingServiceImpl pricingService;

    @Mock
    private SeatingCalculatorServiceImpl seatingCalculatorService;

    @Mock
    private TicketPaymentService ticketPaymentService;

    @Mock
    private SeatReservationService seatReservationService;

    @InjectMocks
    private TicketServiceImpl impl;

    @Test
    void passesOnCalculatingSingleAdultPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneAdultTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 20);
    }

    @Test
    void passesOnCalculatingTwoAdultsPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID,
                List.of(makeMultiAdultTicket(2))));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 40);
    }

    @Test
    void passesOnCalculatingThreeAdultsPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID,
                List.of(makeMultiAdultTicket(3))));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 60);
    }

    @Test
    void passesOnCalculatingTwoSeparateAdultsPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneAdultTicket(),
                makeOneAdultTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 40);
    }

    @Test
    void passesOnCalculatingThreeSeparateAdultsPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneAdultTicket(),
                makeOneAdultTicket(), makeOneAdultTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 60);
    }

    @Test
    void passesOnCalculatingSingleChildAdultPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneChildTicket(),
                makeOneAdultTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 30);
    }

    @Test
    void passesOnCalculatingSingleInfantAdultPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneInfantTicket(),
                makeOneAdultTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 20);
    }

    @Test
    void passesOnCalculatingSingleAdultChildInfantPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneAdultTicket(),
                makeOneChildTicket(), makeOneInfantTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 30);
    }

    @Test
    void passesOnCalculatingMultiAdultChildInfantPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID,
                List.of(makeMultiAdultTicket(2), makeMultiChildTicket(2),
                        makeMultiInfantTicket(2))));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 60);
    }

    @Test
    void passesOnCalculatingSingleAdultSeat() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneAdultTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 1);
    }

    @Test
    void passesOnCalculatingTwoAdultsSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID,
                List.of(makeMultiAdultTicket(2))));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 2);
    }

    @Test
    void passesOnCalculatingThreeAdultsSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID,
                List.of(makeMultiAdultTicket(3))));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 3);
    }

    @Test
    void passesOnCalculatingTwoSeparateAdultsSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneAdultTicket(),
                makeOneAdultTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 2);
    }

    @Test
    void passesOnCalculatingThreeSeparateAdultsSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneAdultTicket(),
                makeOneAdultTicket(), makeOneAdultTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 3);
    }

    @Test
    void passesOnCalculatingSingleChildSeatsWithAdult() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneChildTicket(),
                makeOneAdultTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 2);
    }

    @Test
    void passesOnCalculatingSingleInfantSeatsWithAdult() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneInfantTicket(),
                makeOneAdultTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 1);
    }

    @Test
    void passesOnCalculatingSingleAdultChildInfantSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, List.of(makeOneAdultTicket(),
                makeOneChildTicket(), makeOneInfantTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 2);
    }

    @Test
    void passesOnCalculatingMultiAdultChildInfantSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID,
                List.of(makeMultiAdultTicket(2), makeMultiChildTicket(2),
                        makeMultiInfantTicket(2))));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 4);
    }

}
