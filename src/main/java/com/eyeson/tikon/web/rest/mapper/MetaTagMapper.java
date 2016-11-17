package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.MetaTagDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity MetaTag and its DTO MetaTagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MetaTagMapper {

    MetaTagDTO metaTagToMetaTagDTO(MetaTag metaTag);

    List<MetaTagDTO> metaTagsToMetaTagDTOs(List<MetaTag> metaTags);

    @Mapping(target = "companies", ignore = true)
    @Mapping(target = "serviceItems", ignore = true)
    MetaTag metaTagDTOToMetaTag(MetaTagDTO metaTagDTO);

    List<MetaTag> metaTagDTOsToMetaTags(List<MetaTagDTO> metaTagDTOs);
}
