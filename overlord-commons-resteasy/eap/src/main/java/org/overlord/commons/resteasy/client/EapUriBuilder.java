/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.overlord.commons.resteasy.client;

import javax.ws.rs.core.UriBuilder;

import org.jboss.resteasy.specimpl.UriBuilderImpl;
import org.overlord.commons.resteasy.client.OverlordUriBuilder;

/**
 * Implementations of the URIBuilder valid for EAP environments.
 *
 * @author David Virgil Naranjo
 */
public class EapUriBuilder implements OverlordUriBuilder {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.overlord.sramp.atom.client.OverlordUriBuilder#getBuilder(java.lang
     * .String)
     */
    @Override
    public UriBuilder getBuilder(String uriTemplate) {
        return new UriBuilderImpl().uriTemplate(uriTemplate);
    }

}
