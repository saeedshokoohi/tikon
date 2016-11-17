package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ServiceCategoryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ServiceCategory and its DTO ServiceCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {ServantMapper.class,SettingInfoMapper.class })
public interface ServiceCategoryMapper {

    @Mapping(source = "setting.id", target = "settingId")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "images.id", target = "imagesId")
    ServiceCategoryDTO serviceCategoryToServiceCategoryDTO(ServiceCategory serviceCategory);

    List<ServiceCategoryDTO> serviceCategoriesToServiceCategoryDTOs(List<ServiceCategory> serviceCategories);

//    @Mapping(source = "settingId", target = "setting")
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "parentId", target = "parent")
    @Mapping(source = "imagesId", target = "images")
    ServiceCategory serviceCategoryDTOToServiceCategory(ServiceCategoryDTO serviceCategoryDTO);

    List<ServiceCategory> serviceCategoryDTOsToServiceCategories(List<ServiceCategoryDTO> serviceCategoryDTOs);

    default SettingInfo settingInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        SettingInfo settingInfo = new SettingInfo();
        settingInfo.setId(id);
        return settingInfo;
    }

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default ServiceCategory serviceCategoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setId(id);
        return serviceCategory;
    }

    default Servant servantFromId(Long id) {
        if (id == null) {
            return null;
        }
        Servant servant = new Servant();
        servant.setId(id);
        return servant;
    }

    default AlbumInfo albumInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AlbumInfo albumInfo = new AlbumInfo();
        albumInfo.setId(id);
        return albumInfo;
    }
}
