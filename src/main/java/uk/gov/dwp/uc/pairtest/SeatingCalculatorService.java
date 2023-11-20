package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketRequest;

public interface SeatingCalculatorService {

    int getSeatReservationCount(TicketRequest.Type requestType);
}
