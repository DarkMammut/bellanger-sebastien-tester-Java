package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private static final int MILLISECONDS_TO_HOUR = 1000 * 60 * 60;

    public void calculateFare(Ticket ticket, boolean discount) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double duration = (outHour - inHour) / MILLISECONDS_TO_HOUR;

        if (duration < 0.5) {
            ticket.setPrice(0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    if (discount) {
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * Fare.DISCOUNT_RATE);
                    } else {
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    }
                    break;
                }
                case BIKE: {
                    if (discount) {
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * Fare.DISCOUNT_RATE);
                    } else {
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}