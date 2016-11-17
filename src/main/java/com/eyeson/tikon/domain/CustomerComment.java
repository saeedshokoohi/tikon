package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CustomerComment.
 */
@Entity
@Table(name = "customer_comment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customercomment")
public class CustomerComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "comment_text")
    private String commentText;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private ServiceItem serviceItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerComment customerComment = (CustomerComment) o;
        if(customerComment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customerComment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerComment{" +
            "id=" + id +
            ", createDate='" + createDate + "'" +
            ", commentText='" + commentText + "'" +
            '}';
    }
}
