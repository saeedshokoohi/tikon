package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.AlbumInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AlbumInfo and its DTO AlbumInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlbumInfoMapper {

    AlbumInfoDTO albumInfoToAlbumInfoDTO(AlbumInfo albumInfo);

    List<AlbumInfoDTO> albumInfosToAlbumInfoDTOs(List<AlbumInfo> albumInfos);

    AlbumInfo albumInfoDTOToAlbumInfo(AlbumInfoDTO albumInfoDTO);

    List<AlbumInfo> albumInfoDTOsToAlbumInfos(List<AlbumInfoDTO> albumInfoDTOs);
}
