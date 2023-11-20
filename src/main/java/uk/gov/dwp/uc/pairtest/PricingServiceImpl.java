package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketRequest;

public class PricingServiceImpl implements PricingService {
    @Override
    public int getPrice(TicketRequest.Type ticketType) {
        return switch (ticketType) {
            case ADULT -> 20;
            case CHILD -> 10;
            case INFANT -> 0;
        };
    }
}
