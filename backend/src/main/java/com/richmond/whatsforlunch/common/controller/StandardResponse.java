package com.richmond.whatsforlunch.common.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;

/**
 * Standard response body
 * @param data body data
 * @param <T> data type
 */
public record StandardResponse<T> (@JsonProperty("data") Collection<T> data) {

    public StandardResponse(final T data) {
        this(List.of(data));
    }

    public StandardResponse(final Collection<T> data) {
        Assert.notNull(data, "Response data is required");
        this.data = data;
    }
}
