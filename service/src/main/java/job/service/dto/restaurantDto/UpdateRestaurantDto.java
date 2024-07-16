package job.service.dto.restaurantDto;

import java.util.UUID;

public record UpdateRestaurantDto(UUID id, int tables) {
}
