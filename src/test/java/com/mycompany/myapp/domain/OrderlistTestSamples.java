package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrderlistTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Orderlist getOrderlistSample1() {
        return new Orderlist().id(1L).orderId(1).productId(1).quantity("quantity1").subtotal(1);
    }

    public static Orderlist getOrderlistSample2() {
        return new Orderlist().id(2L).orderId(2).productId(2).quantity("quantity2").subtotal(2);
    }

    public static Orderlist getOrderlistRandomSampleGenerator() {
        return new Orderlist()
            .id(longCount.incrementAndGet())
            .orderId(intCount.incrementAndGet())
            .productId(intCount.incrementAndGet())
            .quantity(UUID.randomUUID().toString())
            .subtotal(intCount.incrementAndGet());
    }
}
