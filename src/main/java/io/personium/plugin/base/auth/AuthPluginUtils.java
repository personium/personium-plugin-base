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

import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.personium.plugin.base.utils.PluginUtils;

/**
 * AuthPlugin関連のユーティリティ.
 */
public final class AuthPluginUtils {

    private AuthPluginUtils() {
    }

    /**
     * tokenToJSON.
     * @param token String
     * @return JSONObject
     * @throws ParseException ParseException
     */
    public static JSONObject tokenToJSON(String token) throws ParseException {
        JSONObject ret = null;
        String decoded = new String(PluginUtils.decodeBase64Url(token), StandardCharsets.UTF_8);
        ret = (JSONObject) new JSONParser().parse(decoded);
        return ret;
    }
}
