package org.demu.models;

import lombok.*;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Item {
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private String picLink;
    private Date dateAdded = Date.from(Instant.now());
    @NonNull
    private double price;
    private boolean available = true;
}
