package org.overlord.commons.i18n;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component(name = "Overlord Commons Path Provider", immediate = true)
@Service(value = org.overlord.commons.i18n.MessagesPathProvider.class)
public class OverlordCommonsMessagesPathProvider implements MessagesPathProvider {

    @Override
    public List<String> getMessagesPaths() {
        List<String> paths = new ArrayList<String>();
        paths.add("org.overlord.commons.auth.i18n");
        paths.add("org.overlord.commons.auth.jboss7.i18n");
        paths.add("org.overlord.commons.auth.tomcat7.i18n");
        paths.add("org.overlord.commons.auth.jetty8.i18n");
        paths.add("org.overlord.commons.karaf.commands.i18n");
        paths.add("org.overlord.commons.osgi.i18n");
        return paths;
    }

}
