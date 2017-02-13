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

import io.personium.plugin.base.Plugin;
import io.personium.plugin.base.PluginException;

import java.util.Map;

/**
 * AuthPlugin.
 * @author coe
 *
 */
public interface AuthPlugin extends Plugin {
    /**
     * getGrantType.
     * @return String
     */
    String getGrantType();

    /**
     * authenticate.
     * @param body map
     * @return pr PluginResult
     * @throws PluginException
     */
    AuthenticatedIdentity authenticate(Map<String, String> body) throws PluginException;
}
