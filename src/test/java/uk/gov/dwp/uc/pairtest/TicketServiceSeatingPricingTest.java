package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleAdultTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 20);
    }

    @Test
    void passesOnCalculatingTwoAdultsPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateMultipleAdultTicket(2))));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 40);
    }

    @Test
    void passesOnCalculatingThreeAdultsPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateMultipleAdultTicket(3))));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 60);
    }

    @Test
    void passesOnCalculatingTwoSeparateAdultsPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleAdultTicket(), generateSingleAdultTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 40);
    }

    @Test
    void passesOnCalculatingThreeSeparateAdultsPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleAdultTicket(), generateSingleAdultTicket(),
                generateSingleAdultTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 60);
    }

    @Test
    void passesOnCalculatingSingleChildPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleChildTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 10);
    }

    @Test
    void passesOnCalculatingSingleInfantPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleInfantTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 0);
    }

    @Test
    void passesOnCalculatingSingleAdultChildInfantPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleAdultTicket(),
                generateSingleChildTicket(), generateSingleInfantTicket())));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 30);
    }

    @Test
    void passesOnCalculatingMultiAdultChildInfantPrice() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateMultipleAdultTicket(2),
                generateMultipleChildTicket(2), generateMultipleInfantTicket(2))));
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 60);
    }

    @Test
    void passesOnCalculatingSingleAdultSeat() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleAdultTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 1);
    }

    @Test
    void passesOnCalculatingTwoAdultsSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateMultipleAdultTicket(2))));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 2);
    }

    @Test
    void passesOnCalculatingThreeAdultsSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateMultipleAdultTicket(3))));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 3);
    }

    @Test
    void passesOnCalculatingTwoSeparateAdultsSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleAdultTicket(), generateSingleAdultTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 2);
    }

    @Test
    void passesOnCalculatingThreeSeparateAdultsSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleAdultTicket(), generateSingleAdultTicket(),
                generateSingleAdultTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 3);
    }

    @Test
    void passesOnCalculatingSingleChildSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleChildTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 1);
    }

    @Test
    void passesOnCalculatingSingleInfantSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleInfantTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 0);
    }

    @Test
    void passesOnCalculatingSingleAdultChildInfantSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateSingleAdultTicket(),
                generateSingleChildTicket(), generateSingleInfantTicket())));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 2);
    }

    @Test
    void passesOnCalculatingMultiAdultChildInfantSeats() {
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        impl.purchaseTickets(generatePurchaseRequest(List.of(generateMultipleAdultTicket(2),
                generateMultipleChildTicket(2), generateMultipleInfantTicket(2))));
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 4);
    }

    private TicketPurchaseRequest generatePurchaseRequest(List<TicketRequest> ticketRequests) {
        return new TicketPurchaseRequest(ACCOUNT_ID, ticketRequests);
    }

    private TicketRequest generateSingleAdultTicket() {
        return new TicketRequest(TicketRequest.Type.ADULT, 1);
    }

    private TicketRequest generateSingleChildTicket() {
        return new TicketRequest(TicketRequest.Type.CHILD, 1);
    }

    private TicketRequest generateSingleInfantTicket() {
        return new TicketRequest(TicketRequest.Type.INFANT, 1);
    }

    private TicketRequest generateMultipleAdultTicket(int amount) {
        return new TicketRequest(TicketRequest.Type.ADULT, amount);
    }

    private TicketRequest generateMultipleChildTicket(int amount) {
        return new TicketRequest(TicketRequest.Type.CHILD, amount);
    }

    private TicketRequest generateMultipleInfantTicket(int amount) {
        return new TicketRequest(TicketRequest.Type.INFANT, amount);
    }
}
