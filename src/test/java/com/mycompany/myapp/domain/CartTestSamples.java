package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CartTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cart getCartSample1() {
        return new Cart().id(1L).userId(1).productId(1).priority(1).quantityDesired(1);
    }

    public static Cart getCartSample2() {
        return new Cart().id(2L).userId(2).productId(2).priority(2).quantityDesired(2);
    }

    public static Cart getCartRandomSampleGenerator() {
        return new Cart()
            .id(longCount.incrementAndGet())
            .userId(intCount.incrementAndGet())
            .productId(intCount.incrementAndGet())
            .priority(intCount.incrementAndGet())
            .quantityDesired(intCount.incrementAndGet());
    }
}
