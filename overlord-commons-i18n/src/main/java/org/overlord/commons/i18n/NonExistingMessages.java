package org.overlord.commons.i18n;


public class NonExistingMessages extends Messages {

    public NonExistingMessages() {
        super("");
    }

    @Override
    public String format(String key, Object... params) {
        return key;
    }

}
