package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PaymentTableTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentTable.class);
        PaymentTable paymentTable1 = getPaymentTableSample1();
        PaymentTable paymentTable2 = new PaymentTable();
        assertThat(paymentTable1).isNotEqualTo(paymentTable2);

        paymentTable2.setId(paymentTable1.getId());
        assertThat(paymentTable1).isEqualTo(paymentTable2);

        paymentTable2 = getPaymentTableSample2();
        assertThat(paymentTable1).isNotEqualTo(paymentTable2);
    }
}
