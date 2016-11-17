package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.SocialNetworkType;

/**
 * A DTO for the SocialNetworkInfo entity.
 */
public class SocialNetworkInfoDTO implements Serializable {

    private Long id;

    private SocialNetworkType socialNetworkType;

    private String title;

    private String url;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public SocialNetworkType getSocialNetworkType() {
        return socialNetworkType;
    }

    public void setSocialNetworkType(SocialNetworkType socialNetworkType) {
        this.socialNetworkType = socialNetworkType;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SocialNetworkInfoDTO socialNetworkInfoDTO = (SocialNetworkInfoDTO) o;

        if ( ! Objects.equals(id, socialNetworkInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SocialNetworkInfoDTO{" +
            "id=" + id +
            ", socialNetworkType='" + socialNetworkType + "'" +
            ", title='" + title + "'" +
            ", url='" + url + "'" +
            '}';
    }
}
