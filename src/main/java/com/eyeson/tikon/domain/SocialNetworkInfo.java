package com.eyeson.tikon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.SocialNetworkType;

/**
 * A SocialNetworkInfo.
 */
@Entity
@Table(name = "social_network_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "socialnetworkinfo")
public class SocialNetworkInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_network_type")
    private SocialNetworkType socialNetworkType;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @ManyToMany(mappedBy = "socialNetworkInfos")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersonInfo> personInfos = new HashSet<>();

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

    public Set<PersonInfo> getPersonInfos() {
        return personInfos;
    }

    public void setPersonInfos(Set<PersonInfo> personInfos) {
        this.personInfos = personInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SocialNetworkInfo socialNetworkInfo = (SocialNetworkInfo) o;
        if(socialNetworkInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, socialNetworkInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SocialNetworkInfo{" +
            "id=" + id +
            ", socialNetworkType='" + socialNetworkType + "'" +
            ", title='" + title + "'" +
            ", url='" + url + "'" +
            '}';
    }
}
