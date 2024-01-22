package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PaymentTable getPaymentTableSample1() {
        return new PaymentTable()
            .id(1L)
            .orderId(1)
            .userId(1)
            .paymentMethod(1)
            .paymentStatus("paymentStatus1")
            .billingAddress("billingAddress1");
    }

    public static PaymentTable getPaymentTableSample2() {
        return new PaymentTable()
            .id(2L)
            .orderId(2)
            .userId(2)
            .paymentMethod(2)
            .paymentStatus("paymentStatus2")
            .billingAddress("billingAddress2");
    }

    public static PaymentTable getPaymentTableRandomSampleGenerator() {
        return new PaymentTable()
            .id(longCount.incrementAndGet())
            .orderId(intCount.incrementAndGet())
            .userId(intCount.incrementAndGet())
            .paymentMethod(intCount.incrementAndGet())
            .paymentStatus(UUID.randomUUID().toString())
            .billingAddress(UUID.randomUUID().toString());
    }
}
