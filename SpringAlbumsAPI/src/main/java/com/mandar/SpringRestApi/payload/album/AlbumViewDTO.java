package com.mandar.SpringRestApi.payload.album;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumViewDTO {

    private long id;

    @NotBlank
    @Schema(description = "Album name", example = "Travel", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description = "Album description", example = "England tour photos", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    private List<PhotoDTO> photos;


}
