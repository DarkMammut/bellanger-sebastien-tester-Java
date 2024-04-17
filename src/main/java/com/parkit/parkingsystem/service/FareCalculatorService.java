package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.Ticket;

import static com.parkit.parkingsystem.constants.Fare.*;

public class FareCalculatorService {

    private static final int MILLISECONDS_TO_HOUR = 1000 * 60 * 60;

    public static void calculateFare(Ticket ticket, boolean discount) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double discountCar = 0.0;
        double discountBike = 0.0;

        double duration = (outHour - inHour) / MILLISECONDS_TO_HOUR;

        if (discount) {
            discountCar = DISCOUNT_RATE * duration * CAR_RATE_PER_HOUR;
            discountBike = DISCOUNT_RATE * duration * BIKE_RATE_PER_HOUR;
        }

        if (duration < 0.5) {
            ticket.setPrice(0.0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * CAR_RATE_PER_HOUR - discountCar);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * BIKE_RATE_PER_HOUR - discountBike);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}