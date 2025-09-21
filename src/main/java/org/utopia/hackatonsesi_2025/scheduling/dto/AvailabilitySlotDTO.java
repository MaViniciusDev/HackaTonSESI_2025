package org.utopia.hackatonsesi_2025.scheduling.dto;

import java.time.LocalDateTime;

public record AvailabilitySlotDTO(
        LocalDateTime start,
        LocalDateTime end
) {}

