package se.apegroup.pizzaapp.infrastructure.jdbc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.apegroup.pizzaapp.infrastructure.jdbc.entity.OrderDetails;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findByOrderId(Long orderId);
}
