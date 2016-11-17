package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.SelectorDataTypeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SelectorDataType and its DTO SelectorDataTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SelectorDataTypeMapper {

    SelectorDataTypeDTO selectorDataTypeToSelectorDataTypeDTO(SelectorDataType selectorDataType);

    List<SelectorDataTypeDTO> selectorDataTypesToSelectorDataTypeDTOs(List<SelectorDataType> selectorDataTypes);

    SelectorDataType selectorDataTypeDTOToSelectorDataType(SelectorDataTypeDTO selectorDataTypeDTO);

    List<SelectorDataType> selectorDataTypeDTOsToSelectorDataTypes(List<SelectorDataTypeDTO> selectorDataTypeDTOs);
}
