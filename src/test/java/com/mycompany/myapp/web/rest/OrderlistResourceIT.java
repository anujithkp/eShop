package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Orderlist;
import com.mycompany.myapp.repository.OrderlistRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link OrderlistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderlistResourceIT {

    private static final Integer DEFAULT_ORDER_ID = 1;
    private static final Integer UPDATED_ORDER_ID = 2;

    private static final Integer DEFAULT_PRODUCT_ID = 1;
    private static final Integer UPDATED_PRODUCT_ID = 2;

    private static final String DEFAULT_QUANTITY = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUBTOTAL = 1;
    private static final Integer UPDATED_SUBTOTAL = 2;

    private static final String ENTITY_API_URL = "/api/orderlists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderlistRepository orderlistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderlistMockMvc;

    private Orderlist orderlist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orderlist createEntity(EntityManager em) {
        Orderlist orderlist = new Orderlist()
            .orderId(DEFAULT_ORDER_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .quantity(DEFAULT_QUANTITY)
            .subtotal(DEFAULT_SUBTOTAL);
        return orderlist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orderlist createUpdatedEntity(EntityManager em) {
        Orderlist orderlist = new Orderlist()
            .orderId(UPDATED_ORDER_ID)
            .productId(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .subtotal(UPDATED_SUBTOTAL);
        return orderlist;
    }

    @BeforeEach
    public void initTest() {
        orderlist = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderlist() throws Exception {
        int databaseSizeBeforeCreate = orderlistRepository.findAll().size();
        // Create the Orderlist
        restOrderlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderlist)))
            .andExpect(status().isCreated());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeCreate + 1);
        Orderlist testOrderlist = orderlistList.get(orderlistList.size() - 1);
        assertThat(testOrderlist.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrderlist.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testOrderlist.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderlist.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void createOrderlistWithExistingId() throws Exception {
        // Create the Orderlist with an existing ID
        orderlist.setId(1L);

        int databaseSizeBeforeCreate = orderlistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderlist)))
            .andExpect(status().isBadRequest());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderlists() throws Exception {
        // Initialize the database
        orderlistRepository.saveAndFlush(orderlist);

        // Get all the orderlistList
        restOrderlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL)));
    }

    @Test
    @Transactional
    void getOrderlist() throws Exception {
        // Initialize the database
        orderlistRepository.saveAndFlush(orderlist);

        // Get the orderlist
        restOrderlistMockMvc
            .perform(get(ENTITY_API_URL_ID, orderlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderlist.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL));
    }

    @Test
    @Transactional
    void getNonExistingOrderlist() throws Exception {
        // Get the orderlist
        restOrderlistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderlist() throws Exception {
        // Initialize the database
        orderlistRepository.saveAndFlush(orderlist);

        int databaseSizeBeforeUpdate = orderlistRepository.findAll().size();

        // Update the orderlist
        Orderlist updatedOrderlist = orderlistRepository.findById(orderlist.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderlist are not directly saved in db
        em.detach(updatedOrderlist);
        updatedOrderlist.orderId(UPDATED_ORDER_ID).productId(UPDATED_PRODUCT_ID).quantity(UPDATED_QUANTITY).subtotal(UPDATED_SUBTOTAL);

        restOrderlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderlist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderlist))
            )
            .andExpect(status().isOk());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeUpdate);
        Orderlist testOrderlist = orderlistList.get(orderlistList.size() - 1);
        assertThat(testOrderlist.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderlist.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testOrderlist.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderlist.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void putNonExistingOrderlist() throws Exception {
        int databaseSizeBeforeUpdate = orderlistRepository.findAll().size();
        orderlist.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderlist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderlist() throws Exception {
        int databaseSizeBeforeUpdate = orderlistRepository.findAll().size();
        orderlist.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderlist() throws Exception {
        int databaseSizeBeforeUpdate = orderlistRepository.findAll().size();
        orderlist.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderlistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderlist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderlistWithPatch() throws Exception {
        // Initialize the database
        orderlistRepository.saveAndFlush(orderlist);

        int databaseSizeBeforeUpdate = orderlistRepository.findAll().size();

        // Update the orderlist using partial update
        Orderlist partialUpdatedOrderlist = new Orderlist();
        partialUpdatedOrderlist.setId(orderlist.getId());

        partialUpdatedOrderlist.orderId(UPDATED_ORDER_ID).quantity(UPDATED_QUANTITY).subtotal(UPDATED_SUBTOTAL);

        restOrderlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderlist))
            )
            .andExpect(status().isOk());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeUpdate);
        Orderlist testOrderlist = orderlistList.get(orderlistList.size() - 1);
        assertThat(testOrderlist.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderlist.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testOrderlist.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderlist.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void fullUpdateOrderlistWithPatch() throws Exception {
        // Initialize the database
        orderlistRepository.saveAndFlush(orderlist);

        int databaseSizeBeforeUpdate = orderlistRepository.findAll().size();

        // Update the orderlist using partial update
        Orderlist partialUpdatedOrderlist = new Orderlist();
        partialUpdatedOrderlist.setId(orderlist.getId());

        partialUpdatedOrderlist
            .orderId(UPDATED_ORDER_ID)
            .productId(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .subtotal(UPDATED_SUBTOTAL);

        restOrderlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderlist))
            )
            .andExpect(status().isOk());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeUpdate);
        Orderlist testOrderlist = orderlistList.get(orderlistList.size() - 1);
        assertThat(testOrderlist.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderlist.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testOrderlist.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderlist.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingOrderlist() throws Exception {
        int databaseSizeBeforeUpdate = orderlistRepository.findAll().size();
        orderlist.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderlist() throws Exception {
        int databaseSizeBeforeUpdate = orderlistRepository.findAll().size();
        orderlist.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderlist() throws Exception {
        int databaseSizeBeforeUpdate = orderlistRepository.findAll().size();
        orderlist.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderlistMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderlist))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orderlist in the database
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderlist() throws Exception {
        // Initialize the database
        orderlistRepository.saveAndFlush(orderlist);

        int databaseSizeBeforeDelete = orderlistRepository.findAll().size();

        // Delete the orderlist
        restOrderlistMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderlist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Orderlist> orderlistList = orderlistRepository.findAll();
        assertThat(orderlistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
