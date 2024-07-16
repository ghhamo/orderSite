package job.persistence;

import job.persistence.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findByName(String name);

    Optional<Restaurant> findByAddress(String address);
}

