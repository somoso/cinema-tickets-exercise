package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;


public class TicketServiceImpl implements TicketService {

    private static final int MAX_TICKET_AMOUNT = 20;

    private final PricingService pricingService;
    private final SeatingCalculatorService seatingCalculatorService;
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    public TicketServiceImpl(PricingService pricingService, SeatingCalculatorService seatingCalculatorService,
                             TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {
        this.pricingService = pricingService;
        this.seatingCalculatorService = seatingCalculatorService;
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException {
        rejectInvalidRequests(ticketPurchaseRequest);

        restrictTicketSales(ticketPurchaseRequest);

        prohibitChildrenAndInfantsWithoutAdult(ticketPurchaseRequest);

        takePayment(ticketPurchaseRequest);

        reserveSeats(ticketPurchaseRequest);
    }

    private void rejectInvalidRequests(TicketPurchaseRequest ticketPurchaseRequest) {
        if (ticketPurchaseRequest == null) {
            throw new InvalidPurchaseException();
        }

        if (ticketPurchaseRequest.getAccountId() <= 0) {
            throw new InvalidPurchaseException();
        }
    }

    private void restrictTicketSales(TicketPurchaseRequest ticketPurchaseRequest) {
        if (ticketPurchaseRequest.getTicketTypeRequests().size() > MAX_TICKET_AMOUNT) {
            throw new InvalidPurchaseException();
        }

        var sum = ticketPurchaseRequest.getTicketTypeRequests().stream().mapToInt(TicketRequest::getNoOfTickets).sum();
        if (sum < 0) {
            throw new InvalidPurchaseException();
        }
        if (sum > MAX_TICKET_AMOUNT) {
            throw new InvalidPurchaseException();
        }
    }

    private void prohibitChildrenAndInfantsWithoutAdult(TicketPurchaseRequest ticketPurchaseRequest) {
        var foundUnderage = ticketPurchaseRequest.getTicketTypeRequests().stream()
                .anyMatch(t -> t.getTicketType() == TicketRequest.Type.CHILD
                        || t.getTicketType() == TicketRequest.Type.INFANT);
        if (foundUnderage) {
            var cannotFindAdult = ticketPurchaseRequest.getTicketTypeRequests()
                    .stream()
                    .noneMatch(t -> t.getTicketType() == TicketRequest.Type.ADULT);
            if (cannotFindAdult) {
                throw new InvalidPurchaseException();
            }
        }
    }

    private void takePayment(TicketPurchaseRequest ticketPurchaseRequest) {
        var totalAmount = ticketPurchaseRequest.getTicketTypeRequests()
                .stream()
                .mapToInt(t -> pricingService.getPrice(t.getTicketType()) * t.getNoOfTickets())
                .sum();

        ticketPaymentService.makePayment(ticketPurchaseRequest.getAccountId(), totalAmount);
    }

    private void reserveSeats(TicketPurchaseRequest ticketPurchaseRequest) {
        var totalSeats = ticketPurchaseRequest.getTicketTypeRequests()
                .stream()
                .mapToInt(t -> seatingCalculatorService.getSeatReservationCount(t.getTicketType()) * t.getNoOfTickets())
                .sum();

        seatReservationService.reserveSeat(ticketPurchaseRequest.getAccountId(), totalSeats);
    }
}
