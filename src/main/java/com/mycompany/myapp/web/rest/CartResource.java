package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Cart;
import com.mycompany.myapp.repository.CartRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Cart}.
 */
@RestController
@RequestMapping("/api/carts")
@Transactional
public class CartResource {

    private final Logger log = LoggerFactory.getLogger(CartResource.class);

    private static final String ENTITY_NAME = "cart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartRepository cartRepository;

    public CartResource(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /**
     * {@code POST  /carts} : Create a new cart.
     *
     * @param cart the cart to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cart, or with status {@code 400 (Bad Request)} if the cart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) throws URISyntaxException {
        log.debug("REST request to save Cart : {}", cart);
        if (cart.getId() != null) {
            throw new BadRequestAlertException("A new cart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cart result = cartRepository.save(cart);
        return ResponseEntity
            .created(new URI("/api/carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /carts/:id} : Updates an existing cart.
     *
     * @param id the id of the cart to save.
     * @param cart the cart to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cart,
     * or with status {@code 400 (Bad Request)} if the cart is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cart couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cart cart)
        throws URISyntaxException {
        log.debug("REST request to update Cart : {}, {}", id, cart);
        if (cart.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cart.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cart result = cartRepository.save(cart);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cart.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /carts/:id} : Partial updates given fields of an existing cart, field will ignore if it is null
     *
     * @param id the id of the cart to save.
     * @param cart the cart to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cart,
     * or with status {@code 400 (Bad Request)} if the cart is not valid,
     * or with status {@code 404 (Not Found)} if the cart is not found,
     * or with status {@code 500 (Internal Server Error)} if the cart couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cart> partialUpdateCart(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cart cart)
        throws URISyntaxException {
        log.debug("REST request to partial update Cart partially : {}, {}", id, cart);
        if (cart.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cart.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cart> result = cartRepository
            .findById(cart.getId())
            .map(existingCart -> {
                if (cart.getUserId() != null) {
                    existingCart.setUserId(cart.getUserId());
                }
                if (cart.getProductId() != null) {
                    existingCart.setProductId(cart.getProductId());
                }
                if (cart.getAddedToCartDate() != null) {
                    existingCart.setAddedToCartDate(cart.getAddedToCartDate());
                }
                if (cart.getPriority() != null) {
                    existingCart.setPriority(cart.getPriority());
                }
                if (cart.getQuantityDesired() != null) {
                    existingCart.setQuantityDesired(cart.getQuantityDesired());
                }
                if (cart.getLastModified() != null) {
                    existingCart.setLastModified(cart.getLastModified());
                }

                return existingCart;
            })
            .map(cartRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cart.getId().toString())
        );
    }

    /**
     * {@code GET  /carts} : get all the carts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carts in body.
     */
    @GetMapping("")
    public List<Cart> getAllCarts() {
        log.debug("REST request to get all Carts");
        return cartRepository.findAll();
    }

    /**
     * {@code GET  /carts/:id} : get the "id" cart.
     *
     * @param id the id of the cart to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cart, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable("id") Long id) {
        log.debug("REST request to get Cart : {}", id);
        Optional<Cart> cart = cartRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cart);
    }

    /**
     * {@code DELETE  /carts/:id} : delete the "id" cart.
     *
     * @param id the id of the cart to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id") Long id) {
        log.debug("REST request to delete Cart : {}", id);
        cartRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
