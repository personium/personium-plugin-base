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

import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.personium.plugin.base.PluginException;
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
     * @return ret JSONObject
     * @throws PluginException 
     */
    public static JSONObject tokenToJSON(String token) throws PluginException {
        JSONObject ret = null;
        try {
            String decoded = new String(PluginUtils.decodeBase64Url(token), StandardCharsets.UTF_8);
            ret = (JSONObject) new JSONParser().parse(decoded);
        } catch (ParseException e) {
            // BASE64はOk.JSONのパースに失敗.
            throw PluginException.Auth.JSON_PARSE_ERROR.params("Header and payload should be Base64 encoded JSON.");

        } catch (Exception e) {
            // BASE64が失敗.
            throw PluginException.Auth.IDTOKEN_ENCODED_INVALID.params("Header and payload should be Base64 encoded.");
        }
        return ret;
    }
}
