package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PaymentTable;
import com.mycompany.myapp.repository.PaymentTableRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PaymentTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentTableResourceIT {

    private static final Integer DEFAULT_ORDER_ID = 1;
    private static final Integer UPDATED_ORDER_ID = 2;

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_PAYMENT_METHOD = 1;
    private static final Integer UPDATED_PAYMENT_METHOD = 2;

    private static final Double DEFAULT_TRANSACTION_AMOUNT = 1D;
    private static final Double UPDATED_TRANSACTION_AMOUNT = 2D;

    private static final String DEFAULT_PAYMENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_BILLING_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_BILLING_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentTableRepository paymentTableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentTableMockMvc;

    private PaymentTable paymentTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTable createEntity(EntityManager em) {
        PaymentTable paymentTable = new PaymentTable()
            .orderId(DEFAULT_ORDER_ID)
            .userId(DEFAULT_USER_ID)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .transactionAmount(DEFAULT_TRANSACTION_AMOUNT)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .billingAddress(DEFAULT_BILLING_ADDRESS);
        return paymentTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTable createUpdatedEntity(EntityManager em) {
        PaymentTable paymentTable = new PaymentTable()
            .orderId(UPDATED_ORDER_ID)
            .userId(UPDATED_USER_ID)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .billingAddress(UPDATED_BILLING_ADDRESS);
        return paymentTable;
    }

    @BeforeEach
    public void initTest() {
        paymentTable = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentTable() throws Exception {
        int databaseSizeBeforeCreate = paymentTableRepository.findAll().size();
        // Create the PaymentTable
        restPaymentTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentTable)))
            .andExpect(status().isCreated());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentTable testPaymentTable = paymentTableList.get(paymentTableList.size() - 1);
        assertThat(testPaymentTable.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testPaymentTable.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPaymentTable.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testPaymentTable.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPaymentTable.getTransactionAmount()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT);
        assertThat(testPaymentTable.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPaymentTable.getBillingAddress()).isEqualTo(DEFAULT_BILLING_ADDRESS);
    }

    @Test
    @Transactional
    void createPaymentTableWithExistingId() throws Exception {
        // Create the PaymentTable with an existing ID
        paymentTable.setId(1L);

        int databaseSizeBeforeCreate = paymentTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentTable)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaymentTables() throws Exception {
        // Initialize the database
        paymentTableRepository.saveAndFlush(paymentTable);

        // Get all the paymentTableList
        restPaymentTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS)))
            .andExpect(jsonPath("$.[*].billingAddress").value(hasItem(DEFAULT_BILLING_ADDRESS)));
    }

    @Test
    @Transactional
    void getPaymentTable() throws Exception {
        // Initialize the database
        paymentTableRepository.saveAndFlush(paymentTable);

        // Get the paymentTable
        restPaymentTableMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentTable.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD))
            .andExpect(jsonPath("$.transactionAmount").value(DEFAULT_TRANSACTION_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS))
            .andExpect(jsonPath("$.billingAddress").value(DEFAULT_BILLING_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingPaymentTable() throws Exception {
        // Get the paymentTable
        restPaymentTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentTable() throws Exception {
        // Initialize the database
        paymentTableRepository.saveAndFlush(paymentTable);

        int databaseSizeBeforeUpdate = paymentTableRepository.findAll().size();

        // Update the paymentTable
        PaymentTable updatedPaymentTable = paymentTableRepository.findById(paymentTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentTable are not directly saved in db
        em.detach(updatedPaymentTable);
        updatedPaymentTable
            .orderId(UPDATED_ORDER_ID)
            .userId(UPDATED_USER_ID)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .billingAddress(UPDATED_BILLING_ADDRESS);

        restPaymentTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaymentTable))
            )
            .andExpect(status().isOk());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeUpdate);
        PaymentTable testPaymentTable = paymentTableList.get(paymentTableList.size() - 1);
        assertThat(testPaymentTable.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testPaymentTable.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPaymentTable.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPaymentTable.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPaymentTable.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
        assertThat(testPaymentTable.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPaymentTable.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingPaymentTable() throws Exception {
        int databaseSizeBeforeUpdate = paymentTableRepository.findAll().size();
        paymentTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentTable() throws Exception {
        int databaseSizeBeforeUpdate = paymentTableRepository.findAll().size();
        paymentTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentTable() throws Exception {
        int databaseSizeBeforeUpdate = paymentTableRepository.findAll().size();
        paymentTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentTableWithPatch() throws Exception {
        // Initialize the database
        paymentTableRepository.saveAndFlush(paymentTable);

        int databaseSizeBeforeUpdate = paymentTableRepository.findAll().size();

        // Update the paymentTable using partial update
        PaymentTable partialUpdatedPaymentTable = new PaymentTable();
        partialUpdatedPaymentTable.setId(paymentTable.getId());

        partialUpdatedPaymentTable.transactionAmount(UPDATED_TRANSACTION_AMOUNT).billingAddress(UPDATED_BILLING_ADDRESS);

        restPaymentTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentTable))
            )
            .andExpect(status().isOk());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeUpdate);
        PaymentTable testPaymentTable = paymentTableList.get(paymentTableList.size() - 1);
        assertThat(testPaymentTable.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testPaymentTable.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPaymentTable.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testPaymentTable.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPaymentTable.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
        assertThat(testPaymentTable.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPaymentTable.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdatePaymentTableWithPatch() throws Exception {
        // Initialize the database
        paymentTableRepository.saveAndFlush(paymentTable);

        int databaseSizeBeforeUpdate = paymentTableRepository.findAll().size();

        // Update the paymentTable using partial update
        PaymentTable partialUpdatedPaymentTable = new PaymentTable();
        partialUpdatedPaymentTable.setId(paymentTable.getId());

        partialUpdatedPaymentTable
            .orderId(UPDATED_ORDER_ID)
            .userId(UPDATED_USER_ID)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .billingAddress(UPDATED_BILLING_ADDRESS);

        restPaymentTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentTable))
            )
            .andExpect(status().isOk());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeUpdate);
        PaymentTable testPaymentTable = paymentTableList.get(paymentTableList.size() - 1);
        assertThat(testPaymentTable.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testPaymentTable.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPaymentTable.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPaymentTable.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPaymentTable.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
        assertThat(testPaymentTable.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPaymentTable.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentTable() throws Exception {
        int databaseSizeBeforeUpdate = paymentTableRepository.findAll().size();
        paymentTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentTable() throws Exception {
        int databaseSizeBeforeUpdate = paymentTableRepository.findAll().size();
        paymentTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentTable() throws Exception {
        int databaseSizeBeforeUpdate = paymentTableRepository.findAll().size();
        paymentTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTableMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paymentTable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentTable in the database
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentTable() throws Exception {
        // Initialize the database
        paymentTableRepository.saveAndFlush(paymentTable);

        int databaseSizeBeforeDelete = paymentTableRepository.findAll().size();

        // Delete the paymentTable
        restPaymentTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentTable> paymentTableList = paymentTableRepository.findAll();
        assertThat(paymentTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
