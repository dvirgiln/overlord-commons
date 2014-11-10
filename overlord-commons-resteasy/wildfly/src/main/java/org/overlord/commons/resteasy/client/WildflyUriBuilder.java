package org.overlord.commons.resteasy.client;

import javax.ws.rs.core.UriBuilder;

import org.jboss.resteasy.specimpl.ResteasyUriBuilder;
import org.overlord.commons.resteasy.client.OverlordUriBuilder;

/**
 * Implementations of the URIBuilder valid for WIldly environments. It is also
 * valid for other environments that attach the resteasy version 3.0.8.Final.
 * 
 * @author David Virgil Naranjo
 */
public class WildflyUriBuilder implements OverlordUriBuilder {

    @Override
    public UriBuilder getBuilder(String uriTemplate) {
        return new ResteasyUriBuilder().uriTemplate(uriTemplate);
    }

}
