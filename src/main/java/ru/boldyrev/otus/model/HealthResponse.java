package ru.boldyrev.otus.model;

import lombok.Data;

@Data
public class HealthResponse {
    public HealthResponse(StatusEnum status){
        this.status = status;
    }
    StatusEnum status;
}
