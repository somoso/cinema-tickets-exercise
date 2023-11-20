package uk.gov.dwp.uc.pairtest.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Should be an Immutable Object
 */
public final class TicketPurchaseRequest {

    private final long accountId;
    private final List<TicketRequest> ticketRequests;

    public TicketPurchaseRequest(long accountId, List<TicketRequest> ticketRequests) {
        this.accountId = accountId;
        this.ticketRequests = new ArrayList<>(ticketRequests);
    }

    public long getAccountId() {
        return accountId;
    }

    public List<TicketRequest> getTicketTypeRequests() {
        return Collections.unmodifiableList(ticketRequests);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketPurchaseRequest that = (TicketPurchaseRequest) o;
        return accountId == that.accountId && Objects.equals(ticketRequests, that.ticketRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, ticketRequests);
    }
}
