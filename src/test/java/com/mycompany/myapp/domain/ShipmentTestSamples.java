package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Shipment getShipmentSample1() {
        return new Shipment().id(1L).orderID(1).address("address1").status("status1");
    }

    public static Shipment getShipmentSample2() {
        return new Shipment().id(2L).orderID(2).address("address2").status("status2");
    }

    public static Shipment getShipmentRandomSampleGenerator() {
        return new Shipment()
            .id(longCount.incrementAndGet())
            .orderID(intCount.incrementAndGet())
            .address(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
