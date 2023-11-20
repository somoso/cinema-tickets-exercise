package uk.gov.dwp.uc.pairtest.domain;

import java.util.Objects;

/**
 * Should be an Immutable Object
 */
public final class TicketRequest {

    private final int noOfTickets;
    private final Type type;

    public TicketRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketRequest that = (TicketRequest) o;
        return noOfTickets == that.noOfTickets && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(noOfTickets, type);
    }

    public enum Type {
        ADULT, CHILD , INFANT
    }

}
