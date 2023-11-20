package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.discount.Discount;
import thirdparty.discount.DiscountService;
import thirdparty.discount.exception.InvalidDiscountCodeException;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static uk.gov.dwp.uc.pairtest.TicketServiceHelper.makeOneAdultTicketWithDiscount;
import static uk.gov.dwp.uc.pairtest.TicketServiceHelper.makeRequestWithId;

@ExtendWith(MockitoExtension.class)
public class TickerServiceDiscountTest {
    private static final long ACCOUNT_ID = 1234;

    @Mock
    private PricingServiceImpl pricingService;

    @Mock
    private SeatingCalculatorServiceImpl seatingCalculatorService;

    @Mock
    private TicketPaymentService ticketPaymentService;

    @Mock
    private SeatReservationService seatReservationService;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private TicketServiceImpl impl;

    @Test
    void invalidDiscountDoesntFailPayment() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        when(discountService.getDiscountPercentage(anyLong(), any()))
                .thenThrow(new InvalidDiscountCodeException("No discounts here, bud"));
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, makeOneAdultTicketWithDiscount("testing")));

        verify(discountService, only()).getDiscountPercentage(ACCOUNT_ID, "testing");
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 20);
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 1);
    }

    @Test
    void halfPriceDiscount() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        when(discountService.getDiscountPercentage(eq(ACCOUNT_ID), eq("half")))
                .thenReturn(new Discount(0.5f));
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, makeOneAdultTicketWithDiscount("half")));

        verify(discountService, only()).getDiscountPercentage(ACCOUNT_ID, "half");
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 10);
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 1);
    }

    @Test
    void twentyFivePercentOff() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        when(discountService.getDiscountPercentage(eq(ACCOUNT_ID), eq("quarter-time")))
                .thenReturn(new Discount(0.25f));
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, makeOneAdultTicketWithDiscount("quarter-time")));

        verify(discountService, only()).getDiscountPercentage(ACCOUNT_ID, "quarter-time");
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 15);
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 1);
    }

    @Test
    void seventyFivePercentOff() {
        when(pricingService.getPrice(any())).thenCallRealMethod();
        when(seatingCalculatorService.getSeatReservationCount(any())).thenCallRealMethod();
        when(discountService.getDiscountPercentage(eq(ACCOUNT_ID), eq("pacman")))
                .thenReturn(new Discount(0.75f));
        impl.purchaseTickets(makeRequestWithId(ACCOUNT_ID, makeOneAdultTicketWithDiscount("pacman")));

        verify(discountService, only()).getDiscountPercentage(ACCOUNT_ID, "pacman");
        verify(ticketPaymentService, only()).makePayment(ACCOUNT_ID, 5);
        verify(seatReservationService, only()).reserveSeat(ACCOUNT_ID, 1);
    }
}
