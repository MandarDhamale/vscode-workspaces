package com.mandar.SpringRestApi.payload.album;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhotoViewDTO {

    private long id;
    private String name;
    private String description;

}
