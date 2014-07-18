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

package org.overlord.commons.eap.extensions.config;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.dmr.ModelNode;

/**
 * Add a configuration property
 *
 * @author Kevin Conner
 */
public class ConfigurationPropertyAdd extends AbstractAddStepHandler {

    static final ConfigurationPropertyAdd INSTANCE = new ConfigurationPropertyAdd();

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        model.setEmptyObject();
        for (SimpleAttributeDefinition attr : ConfigurationPropertyDefinition.ALL_ATTRIBUTES) {
            attr.validateAndSet(operation, model);
        }
    }

    protected boolean requiresRuntimeVerification() {
        return false;
    }
}
