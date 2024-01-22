package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.OrderlistTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderlistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orderlist.class);
        Orderlist orderlist1 = getOrderlistSample1();
        Orderlist orderlist2 = new Orderlist();
        assertThat(orderlist1).isNotEqualTo(orderlist2);

        orderlist2.setId(orderlist1.getId());
        assertThat(orderlist1).isEqualTo(orderlist2);

        orderlist2 = getOrderlistSample2();
        assertThat(orderlist1).isNotEqualTo(orderlist2);
    }
}
