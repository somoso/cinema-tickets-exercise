package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketRequest;

public class SeatingCalculatorServiceImpl implements SeatingCalculatorService {
    @Override
    public int getSeatReservationCount(TicketRequest.Type requestType) {
        if (requestType == TicketRequest.Type.INFANT) {
            return 0;
        }
        return 1;
    }
}
