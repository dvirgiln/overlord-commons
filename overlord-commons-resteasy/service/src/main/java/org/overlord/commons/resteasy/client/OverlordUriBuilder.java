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

/**
 * Builter that geterates a URIBuilder from a template. This is used because
 * different implementations of the builder can be injected in runtime,
 * depending on the version of resteeasy. As we are deploying the application in
 * different environments this version can not be always the same.
 *
 * @author David Virgil Naranjo
 */
public interface OverlordUriBuilder {

    /**
     * Creates a {@link UriBuilder} for the given URI template.
     *
     * @param uriTemplate
     *            the uri template
     * @return the builder
     */
    public UriBuilder getBuilder(String uriTemplate);
}
