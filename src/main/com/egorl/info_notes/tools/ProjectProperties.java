package main.com.egorl.info_notes.tools;

import java.util.ResourceBundle;

public class ProjectProperties {

    ResourceBundle messages;

    public ProjectProperties() {
        messages = ResourceBundle.getBundle("locale/locale");
    }

    public String getLocalizedMessage(String key) {
        return messages.getString(key);
    }
}
