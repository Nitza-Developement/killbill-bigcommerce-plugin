package org.killbill.billing.plugin.bigcommerce;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.representation.StringRepresentation;
import org.restlet.data.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    private final ClientResource client;

    public ApiClient(final String url) {
        this.client = new ClientResource(url);
    }

    public Integer pay(final Map<String, Object> data) {

        StringRepresentation jRepresentation;

        try {
            jRepresentation = jsonRepresentation(data);
        } catch (JsonProcessingException e) {
            logger.error("Error converting json to string", e);
            return 400;
        }

        if (jRepresentation != null) {
            try {
                String response = client.post(jRepresentation).getText();

                if (!response.contains("success")){
                    logger.error("Error in response from flask api", response);
                    return 400;
                }

            } catch (ResourceException | IOException e) {
                logger.error("Error in api call", e);
                return 400;
            }
        }

        return 200;

    }

    public StringRepresentation jsonRepresentation(final Map<String, Object> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsoString = objectMapper.writeValueAsString(data);
        return new StringRepresentation(jsoString, MediaType.APPLICATION_JSON);
    }

}
