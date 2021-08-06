package io.personium.plugin.base.auth;

import java.util.ArrayList;

/**
 * Interface of providing multiple AuthPlugin instance.
 */
public interface AuthPluginLoader {
    /**
     * Instanciate Multiple AuthPlugin
     * @return
     */
    ArrayList<AuthPlugin> loadInstances();
}
