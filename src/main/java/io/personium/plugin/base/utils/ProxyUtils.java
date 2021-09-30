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
package io.personium.plugin.base.utils;

import io.personium.plugin.base.PluginConfig;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Http Proxy Utilities.
 *
 */
public class ProxyUtils {
    /**
     * constructor.
     */
    private ProxyUtils() {
    }

    /**
     * isValid.
     * @param str String
     * @return boolean
     */
    public static boolean isValid(String str) {
        return str != null && str.trim().length() > 0;
    }

    /**
     * isProxyHost.
     * @return false/true boolean
     */
    public static boolean isProxyHost() {
        String host = PluginConfig.getProxyHostName();
        int port = PluginConfig.getProxyHostNumber();
        return isValid(host) && port > 0;
    }

    /**
     * compatible proxy Credentials.
     * @return httpClient CloseableHttpClient
     */
    public static CloseableHttpClient proxyHttpClient() {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        String host = PluginConfig.getProxyHostName();
        int port = PluginConfig.getProxyHostNumber();

        if (isValid(host) && port > 0) {
            String user = PluginConfig.getProxyUserName();
            String pswd = PluginConfig.getProxyPassword();
            if (isValid(user) && isValid(pswd)) {
                credsProvider.setCredentials(
                        new AuthScope(host, port),
                        new UsernamePasswordCredentials(user, pswd));
            }
        }
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();

        return httpClient;
    }

    /**
     * compatible proxy RequestConfig.
     * getRequestConfig.
     * @return config RequestConfig
     */
    public static RequestConfig getRequestConfig() {
        RequestConfig config = null;
        String host = PluginConfig.getProxyHostName();
        int port = PluginConfig.getProxyHostNumber();
        if (isValid(host) && port > 0) {
            HttpHost proxy = new HttpHost(host, port);
            config = RequestConfig.custom().setProxy(proxy).build();
        }

        return config;
    }
}
