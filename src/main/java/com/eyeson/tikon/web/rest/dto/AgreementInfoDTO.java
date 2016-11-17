package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the AgreementInfo entity.
 */
public class AgreementInfoDTO implements Serializable {

    private Long id;

    private String rules;

    private String agreement;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AgreementInfoDTO agreementInfoDTO = (AgreementInfoDTO) o;

        if ( ! Objects.equals(id, agreementInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AgreementInfoDTO{" +
            "id=" + id +
            ", rules='" + rules + "'" +
            ", agreement='" + agreement + "'" +
            '}';
    }
}
