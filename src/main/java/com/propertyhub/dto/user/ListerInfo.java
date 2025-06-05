package com.propertyhub.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListerInfo {
    private UUID id;
    private String fullName;
    // Potentially other non-sensitive contact info if needed
}
