package org.app.courier.repository;

import org.app.courier.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    boolean existsByCourierNumber(String courierNumber);
}
