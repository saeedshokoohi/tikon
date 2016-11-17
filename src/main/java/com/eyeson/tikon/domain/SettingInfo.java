package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SettingInfo.
 */
@Entity
@Table(name = "setting_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "settinginfo")
public class SettingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "setting_name")
    private String settingName;

    @ManyToOne(cascade = {CascadeType.ALL})
    private ThemeSettingInfo themeSettingInfo;

    @ManyToOne(cascade = {CascadeType.ALL})
    private NotificationSetting notificationSetting;

    @ManyToOne(cascade = {CascadeType.ALL})
    private FinancialSetting financialSetting;

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

    public ThemeSettingInfo getThemeSettingInfo() {

        return themeSettingInfo;
    }

    public void setThemeSettingInfo(ThemeSettingInfo themeSettingInfo) {
        this.themeSettingInfo = themeSettingInfo;
    }

    public NotificationSetting getNotificationSetting() {
        return notificationSetting;
    }

    public void setNotificationSetting(NotificationSetting notificationSetting) {
        this.notificationSetting = notificationSetting;
    }

    public FinancialSetting getFinancialSetting() {
        return financialSetting;
    }

    public void setFinancialSetting(FinancialSetting financialSetting) {
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
        SettingInfo settingInfo = (SettingInfo) o;
        if(settingInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, settingInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SettingInfo{" +
            "id=" + id +
            ", settingName='" + settingName + "'" +
            '}';
    }
}
