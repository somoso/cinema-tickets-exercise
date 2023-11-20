package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketRequest;

public interface PricingService {

    int getPrice(TicketRequest.Type ticketType);
}
