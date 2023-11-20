package uk.gov.dwp.uc.pairtest.domain;

import java.util.Objects;

/**
 * Should be an Immutable Object
 */
public final class TicketRequest {

    private final int noOfTickets;
    private final Type type;
    private final String discountCode;

    public TicketRequest(Type type, int noOfTickets, String discountCode) {
        this.type = type;
        this.noOfTickets = noOfTickets;
        this.discountCode = discountCode;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketRequest that = (TicketRequest) o;
        return noOfTickets == that.noOfTickets && type == that.type && Objects.equals(discountCode, that.discountCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noOfTickets, type, discountCode);
    }

    public enum Type {
        ADULT, CHILD , INFANT
    }

}
