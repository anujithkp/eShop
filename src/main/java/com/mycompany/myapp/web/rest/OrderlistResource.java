package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Orderlist;
import com.mycompany.myapp.repository.OrderlistRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Orderlist}.
 */
@RestController
@RequestMapping("/api/orderlists")
@Transactional
public class OrderlistResource {

    private final Logger log = LoggerFactory.getLogger(OrderlistResource.class);

    private static final String ENTITY_NAME = "orderlist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderlistRepository orderlistRepository;

    public OrderlistResource(OrderlistRepository orderlistRepository) {
        this.orderlistRepository = orderlistRepository;
    }

    /**
     * {@code POST  /orderlists} : Create a new orderlist.
     *
     * @param orderlist the orderlist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderlist, or with status {@code 400 (Bad Request)} if the orderlist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Orderlist> createOrderlist(@RequestBody Orderlist orderlist) throws URISyntaxException {
        log.debug("REST request to save Orderlist : {}", orderlist);
        if (orderlist.getId() != null) {
            throw new BadRequestAlertException("A new orderlist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Orderlist result = orderlistRepository.save(orderlist);
        return ResponseEntity
            .created(new URI("/api/orderlists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orderlists/:id} : Updates an existing orderlist.
     *
     * @param id the id of the orderlist to save.
     * @param orderlist the orderlist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderlist,
     * or with status {@code 400 (Bad Request)} if the orderlist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderlist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Orderlist> updateOrderlist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Orderlist orderlist
    ) throws URISyntaxException {
        log.debug("REST request to update Orderlist : {}, {}", id, orderlist);
        if (orderlist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderlist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderlistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Orderlist result = orderlistRepository.save(orderlist);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderlist.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orderlists/:id} : Partial updates given fields of an existing orderlist, field will ignore if it is null
     *
     * @param id the id of the orderlist to save.
     * @param orderlist the orderlist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderlist,
     * or with status {@code 400 (Bad Request)} if the orderlist is not valid,
     * or with status {@code 404 (Not Found)} if the orderlist is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderlist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Orderlist> partialUpdateOrderlist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Orderlist orderlist
    ) throws URISyntaxException {
        log.debug("REST request to partial update Orderlist partially : {}, {}", id, orderlist);
        if (orderlist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderlist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderlistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Orderlist> result = orderlistRepository
            .findById(orderlist.getId())
            .map(existingOrderlist -> {
                if (orderlist.getOrderId() != null) {
                    existingOrderlist.setOrderId(orderlist.getOrderId());
                }
                if (orderlist.getProductId() != null) {
                    existingOrderlist.setProductId(orderlist.getProductId());
                }
                if (orderlist.getQuantity() != null) {
                    existingOrderlist.setQuantity(orderlist.getQuantity());
                }
                if (orderlist.getSubtotal() != null) {
                    existingOrderlist.setSubtotal(orderlist.getSubtotal());
                }

                return existingOrderlist;
            })
            .map(orderlistRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderlist.getId().toString())
        );
    }

    /**
     * {@code GET  /orderlists} : get all the orderlists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderlists in body.
     */
    @GetMapping("")
    public List<Orderlist> getAllOrderlists() {
        log.debug("REST request to get all Orderlists");
        return orderlistRepository.findAll();
    }

    /**
     * {@code GET  /orderlists/:id} : get the "id" orderlist.
     *
     * @param id the id of the orderlist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderlist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Orderlist> getOrderlist(@PathVariable("id") Long id) {
        log.debug("REST request to get Orderlist : {}", id);
        Optional<Orderlist> orderlist = orderlistRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderlist);
    }

    /**
     * {@code DELETE  /orderlists/:id} : delete the "id" orderlist.
     *
     * @param id the id of the orderlist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderlist(@PathVariable("id") Long id) {
        log.debug("REST request to delete Orderlist : {}", id);
        orderlistRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
