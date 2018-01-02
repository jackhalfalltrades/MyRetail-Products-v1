package com.myretail.products.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class CurrentPrice implements Serializable {

    private final static long serialVersionUID = -93258022922430261L;

    private String value;

    @JsonProperty("currency_code")
    private String currencyCode;
}
