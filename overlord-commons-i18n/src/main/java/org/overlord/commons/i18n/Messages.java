package org.overlord.commons.i18n;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.overlord.commons.services.ServiceRegistryUtil;

public class Messages extends AbstractMessages {

    private static Map<String, Messages> messages;

    private final static MySecurityManager mySecurityManager = new MySecurityManager();

    static {
        messages = new HashMap<String, Messages>();
        Set<MessagesPathProvider> providers = ServiceRegistryUtil.getServices(MessagesPathProvider.class);
        for (MessagesPathProvider provider : providers) {
            for (String messagesPath : provider.getMessagesPaths()) {
                messages.put(messagesPath, new Messages(messagesPath));
            }
        }
    }

    public static Messages getInstance() {

        // Here we get the callerPackage

        String callerPackage = getCallerPackage();

        // Algorithm that would return the Messages object that better match the
        // package param

        Messages messages = getMessages(callerPackage);
        return messages;

    }


    private static Messages getMessages(String packageName) {
        while (!packageName.isEmpty()) {
            if (messages.containsKey(packageName + ".i18n")) {
                return messages.get(packageName + ".i18n");
            }
            if (packageName.contains(".")) {
                packageName = packageName.substring(0, packageName.lastIndexOf("."));
            } else {
                packageName = "";
            }
        }
        return new NonExistingMessages();
    }

    private static String getCallerPackage() {
        String fullClass = mySecurityManager.getCallerClassName(3);
        if (fullClass.contains(".")) {
            return fullClass.substring(0, fullClass.lastIndexOf("."));
        } else {
            return "";
        }
    }

    public Messages(String packageName) {
        super(packageName);
    }

    /**
     * A custom security manager that exposes the getClassContext() information
     */
    static class MySecurityManager extends SecurityManager {
        public String getCallerClassName(int callStackDepth) {
            return getClassContext()[callStackDepth].getName();
        }
    }
}
