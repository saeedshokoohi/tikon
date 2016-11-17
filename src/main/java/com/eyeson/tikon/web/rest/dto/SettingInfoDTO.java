package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the SettingInfo entity.
 */
public class SettingInfoDTO implements Serializable {

    private Long id;

    private String settingName;

    private Long themeSettingInfoId;

    private Long notificationSettingId;

    private Long financialSettingId;

    private ThemeSettingInfoDTO themeSettingInfo ;

    private NotificationSettingDTO notificationSetting;

    private FinancialSettingDTO financialSetting;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public Long getThemeSettingInfoId() {
        return themeSettingInfoId;
    }

    public void setThemeSettingInfoId(Long themeSettingInfoId) {
        this.themeSettingInfoId = themeSettingInfoId;
    }

    public Long getNotificationSettingId() {
        return notificationSettingId;
    }

    public void setNotificationSettingId(Long notificationSettingId) {
        this.notificationSettingId = notificationSettingId;
    }

    public Long getFinancialSettingId() {
        return financialSettingId;
    }

    public void setFinancialSettingId(Long financialSettingId) {
        this.financialSettingId = financialSettingId;
    }


    public ThemeSettingInfoDTO getThemeSettingInfo() {
        if(themeSettingInfo==null)themeSettingInfo=new ThemeSettingInfoDTO();

        return themeSettingInfo;
    }

    public void setThemeSettingInfo(ThemeSettingInfoDTO themeSettingInfo) {
        this.themeSettingInfo = themeSettingInfo;
    }

    public NotificationSettingDTO getNotificationSetting()
    {
        if(notificationSetting==null)notificationSetting=new NotificationSettingDTO();
        return notificationSetting;
    }

    public void setNotificationSetting(NotificationSettingDTO notificationSetting) {
        this.notificationSetting = notificationSetting;
    }

    public FinancialSettingDTO getFinancialSetting() {
        if(financialSetting==null)financialSetting=new FinancialSettingDTO();
        return financialSetting;
    }

    public void setFinancialSetting(FinancialSettingDTO financialSetting) {
        this.financialSetting = financialSetting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SettingInfoDTO settingInfoDTO = (SettingInfoDTO) o;

        if ( ! Objects.equals(id, settingInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SettingInfoDTO{" +
            "id=" + id +
            ", settingName='" + settingName + "'" +
            '}';
    }
}
