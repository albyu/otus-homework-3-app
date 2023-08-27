package ru.boldyrev.otus.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class VersionResponse {
    public VersionResponse(String versionTag){
        this.versionTag = versionTag;
    }
    String versionTag;
}
