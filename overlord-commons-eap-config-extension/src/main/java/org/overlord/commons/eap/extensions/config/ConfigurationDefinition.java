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

import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

/**
 * @author Kevin Conner
 */
public class ConfigurationDefinition extends SimpleResourceDefinition {

    protected ConfigurationDefinition() {
        super(SubsystemExtension.PATH_CONFIGURATION,
                SubsystemExtension.getResourceDescriptionResolver("configuration"), //$NON-NLS-1$
                ConfigurationAdd.INSTANCE, ConfigurationRemove.INSTANCE
        );
    }

    protected static final SimpleAttributeDefinition NAME =
            new SimpleAttributeDefinitionBuilder(Constants.ATTRIBUTE_NAME, ModelType.STRING, false)
                    .setXmlName(Attribute.NAME.getLocalName())
                    .setAllowExpression(false)
                    .setValidator(new StringLengthValidator(1))
                    .build();
    
    protected static final SimpleAttributeDefinition[] ALL_ATTRIBUTES = {NAME};

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        resourceRegistration.registerReadWriteAttribute(NAME, null, new ReloadRequiredWriteAttributeHandler(NAME));
    }
    
    @Override
    public void registerChildren(ManagementResourceRegistration resourceRegistration) {
        resourceRegistration.registerSubModel(new ConfigurationPropertyDefinition());
    }
}
