package org.projectx.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class Measurement {

    @Id
    private String id;

    private String deviceId;

    private LocalDateTime timestamp;

    //private MeasurementType type;

    private String value;
}
