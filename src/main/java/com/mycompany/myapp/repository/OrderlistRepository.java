package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Orderlist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Orderlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderlistRepository extends JpaRepository<Orderlist, Long> {}
