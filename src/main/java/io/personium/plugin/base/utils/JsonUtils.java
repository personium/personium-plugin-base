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
package io.personium.plugin.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.http.impl.client.CloseableHttpClient;

import io.personium.plugin.base.PluginBaseException;
import io.personium.plugin.base.utils.ProxyUtils;
import io.personium.plugin.base.utils.PluginUtils;

/**
 * Json関連のユーティリティ.
 */
public final class JsonUtils {

    private JsonUtils() {
    }

    /**
     * tokenToJSON.
     * @param token String
     * @return ret JSONObject
     */
    public static JSONObject tokenToJSON(String token) {
        JSONObject ret = null;
        try {
            String decoded = new String(PluginUtils.decodeBase64Url(token), StandardCharsets.UTF_8);
            ret = (JSONObject) new JSONParser().parse(decoded);
        } catch (ParseException e) {
            // BASE64はOk.JSONのパースに失敗.
        	throw PluginBaseException.OData.JSON_PARSE_ERROR.params("Header and payload should be Base64 encoded JSON.");

        } catch (Exception e) {
            // BASE64が失敗.
            throw PluginBaseException.Auth.IDTOKEN_ENCODED_INVALID.params("Header and payload should be Base64 encoded.");
        }
        return ret;
    }

    /**
     * Cacheを効かせるため、ClientをStaticとする. たかだか限定されたURLのbodyを保存するのみであり、
     * 最大キャッシュサイズはCacheConfigクラスで定義された16kbyte程度である. そのため、Staticで持つこととした.
     */
    private static HttpClient httpClient = new CachingHttpClient();
    private static CloseableHttpClient httpProxyClient = ProxyUtils.proxyHttpClient();

    /**
     * HTTPでJSONオブジェクトを取得する処理. Cacheが利用可能であればその値を用いる.
     *
     * @param url URL
     * @return JSONObject
     */
    public static JSONObject getHttpJSON(String url) {
        HttpGet get = new HttpGet(url);
        HttpResponse res = null;
        int status = 0;
        try {
            // Connection Host
            if (ProxyUtils.isProxyHost()) {
                get.setConfig(ProxyUtils.getRequestConfig());
                res = httpProxyClient.execute(get);
            } else {
                res = httpClient.execute(get);
            }

            InputStream is = res.getEntity().getContent();
            status = res.getStatusLine().getStatusCode();
            String body = PluginUtils.readInputStreamAsString(is);
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(body);
            return jsonObj;
        } catch (ClientProtocolException e) {
            // HTTPのプロトコル違反
            throw PluginBaseException.NetWork.UNEXPECTED_RESPONSE.params(url, "proper HTTP response", status).reason(e);
        } catch (IOException e) {
            // サーバーに接続できない場合に発生
            throw PluginBaseException.NetWork.HTTP_REQUEST_FAILED.params(HttpGet.METHOD_NAME, url).reason(e);
        } catch (ParseException e) {
            // JSONでないものを返してきた
            throw PluginBaseException.NetWork.UNEXPECTED_RESPONSE.params(url, "JSON", status).reason(e);
        }
    }
}
