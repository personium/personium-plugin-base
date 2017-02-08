/**
 * personium.io
 * Copyright 2017 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.personium.plugin.base.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * Plugin Result.
 *
 */
public class AuthenticatedIdentity {
    /**
     * Result Map.
     */
    Map<String, String> ai = new HashMap<String, String>();

    /**
     * Result Map.
     */
    private String accountName;

    /**
     * Get Result.
     * @return ai Map
     */
    public Map<String, String> getResult() {
        return ai;
    }

    /**
     * Get Attributes value.
     * @param key String
     * @return String
     */
    public String getAttributes(String key) {
        return (String) ai.get(key);
    }

    /**
     * Set Attributes key value.
     * @param key String
     * @param value String
     */
    public void setAttributes(String key, String value) {
        ai.put(key, value);
    }

    /**
     * getAccountName.
     * @return accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * setAccountName.
     * @param name String
     */
    public void setAccountName(String name) {
        this.accountName = name;
    }
}
