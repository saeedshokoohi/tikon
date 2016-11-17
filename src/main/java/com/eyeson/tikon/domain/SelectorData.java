package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SelectorData.
 */
@Entity
@Table(name = "selector_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "selectordata")
public class SelectorData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "key")
    private String key;

    @Column(name = "text")
    private String text;

    @Column(name = "order_no")
    private Integer orderNo;

    @ManyToOne
    private SelectorDataType type;

    @ManyToOne
    private SelectorData parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public SelectorDataType getType() {
        return type;
    }

    public void setType(SelectorDataType selectorDataType) {
        this.type = selectorDataType;
    }

    public SelectorData getParent() {
        return parent;
    }

    public void setParent(SelectorData selectorData) {
        this.parent = selectorData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SelectorData selectorData = (SelectorData) o;
        if(selectorData.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, selectorData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SelectorData{" +
            "id=" + id +
            ", key='" + key + "'" +
            ", text='" + text + "'" +
            ", orderNo='" + orderNo + "'" +
            '}';
    }
}
