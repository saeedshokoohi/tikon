package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ImageDataDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ImageData and its DTO ImageDataDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ImageDataMapper {

    @Mapping(source = "albumInfo.id", target = "albumInfoId")
    ImageDataDTO imageDataToImageDataDTO(ImageData imageData);

    List<ImageDataDTO> imageDataToImageDataDTOs(List<ImageData> imageData);

    @Mapping(source = "albumInfoId", target = "albumInfo")
    ImageData imageDataDTOToImageData(ImageDataDTO imageDataDTO);

    List<ImageData> imageDataDTOsToImageData(List<ImageDataDTO> imageDataDTOs);

    default AlbumInfo albumInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AlbumInfo albumInfo = new AlbumInfo();
        albumInfo.setId(id);
        return albumInfo;
    }
}
