package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PaymentTable;
import com.mycompany.myapp.repository.PaymentTableRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PaymentTable}.
 */
@RestController
@RequestMapping("/api/payment-tables")
@Transactional
public class PaymentTableResource {

    private final Logger log = LoggerFactory.getLogger(PaymentTableResource.class);

    private static final String ENTITY_NAME = "paymentTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentTableRepository paymentTableRepository;

    public PaymentTableResource(PaymentTableRepository paymentTableRepository) {
        this.paymentTableRepository = paymentTableRepository;
    }

    /**
     * {@code POST  /payment-tables} : Create a new paymentTable.
     *
     * @param paymentTable the paymentTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentTable, or with status {@code 400 (Bad Request)} if the paymentTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentTable> createPaymentTable(@RequestBody PaymentTable paymentTable) throws URISyntaxException {
        log.debug("REST request to save PaymentTable : {}", paymentTable);
        if (paymentTable.getId() != null) {
            throw new BadRequestAlertException("A new paymentTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentTable result = paymentTableRepository.save(paymentTable);
        return ResponseEntity
            .created(new URI("/api/payment-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-tables/:id} : Updates an existing paymentTable.
     *
     * @param id the id of the paymentTable to save.
     * @param paymentTable the paymentTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentTable,
     * or with status {@code 400 (Bad Request)} if the paymentTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentTable> updatePaymentTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentTable paymentTable
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentTable : {}, {}", id, paymentTable);
        if (paymentTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentTable result = paymentTableRepository.save(paymentTable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentTable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-tables/:id} : Partial updates given fields of an existing paymentTable, field will ignore if it is null
     *
     * @param id the id of the paymentTable to save.
     * @param paymentTable the paymentTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentTable,
     * or with status {@code 400 (Bad Request)} if the paymentTable is not valid,
     * or with status {@code 404 (Not Found)} if the paymentTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentTable> partialUpdatePaymentTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentTable paymentTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentTable partially : {}, {}", id, paymentTable);
        if (paymentTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentTable> result = paymentTableRepository
            .findById(paymentTable.getId())
            .map(existingPaymentTable -> {
                if (paymentTable.getOrderId() != null) {
                    existingPaymentTable.setOrderId(paymentTable.getOrderId());
                }
                if (paymentTable.getUserId() != null) {
                    existingPaymentTable.setUserId(paymentTable.getUserId());
                }
                if (paymentTable.getPaymentDate() != null) {
                    existingPaymentTable.setPaymentDate(paymentTable.getPaymentDate());
                }
                if (paymentTable.getPaymentMethod() != null) {
                    existingPaymentTable.setPaymentMethod(paymentTable.getPaymentMethod());
                }
                if (paymentTable.getTransactionAmount() != null) {
                    existingPaymentTable.setTransactionAmount(paymentTable.getTransactionAmount());
                }
                if (paymentTable.getPaymentStatus() != null) {
                    existingPaymentTable.setPaymentStatus(paymentTable.getPaymentStatus());
                }
                if (paymentTable.getBillingAddress() != null) {
                    existingPaymentTable.setBillingAddress(paymentTable.getBillingAddress());
                }

                return existingPaymentTable;
            })
            .map(paymentTableRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentTable.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-tables} : get all the paymentTables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentTables in body.
     */
    @GetMapping("")
    public List<PaymentTable> getAllPaymentTables() {
        log.debug("REST request to get all PaymentTables");
        return paymentTableRepository.findAll();
    }

    /**
     * {@code GET  /payment-tables/:id} : get the "id" paymentTable.
     *
     * @param id the id of the paymentTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentTable> getPaymentTable(@PathVariable("id") Long id) {
        log.debug("REST request to get PaymentTable : {}", id);
        Optional<PaymentTable> paymentTable = paymentTableRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paymentTable);
    }

    /**
     * {@code DELETE  /payment-tables/:id} : delete the "id" paymentTable.
     *
     * @param id the id of the paymentTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentTable(@PathVariable("id") Long id) {
        log.debug("REST request to delete PaymentTable : {}", id);
        paymentTableRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
