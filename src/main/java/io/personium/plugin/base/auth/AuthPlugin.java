/**
 * Personium
 * Copyright 2014-2021 Personium Project Authors
 * - FUJITSU LIMITED
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

import java.util.List;
import java.util.Map;

import io.personium.plugin.base.Plugin;

/**
 * AuthPlugin.
 */
public interface AuthPlugin extends Plugin {
    /**
     * Get GrantType.
     * @return GrantType
     */
    String getGrantType();

    /**
     * Get AccountType.
     * @return AccountType
     */
    String getAccountType();

    /**
     * authenticate.
     * @param body map
     * @return PluginResult
     * @throws AuthPluginException AuthPluginException
     */
    AuthenticatedIdentity authenticate(Map<String, List<String>> body) throws AuthPluginException;
}
