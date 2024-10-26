package org.killbill.billing.plugin.bigcommerce;


import java.util.Map;

import org.restlet.resource.ClientResource;
import org.restlet.representation.StringRepresentation;
import org.restlet.data.MediaType;

public class ApiClient {

    private final String url;
    private final ClientResource client;

    public ApiClient(final String url){
        this.url = url;
        this.client = new ClientResource(url);
    }


    public Integer post(final Map data){

        System.out.println(data);

        return 200;

    }

}
