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
package io.personium.plugin.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 設定情報を保持するクラス. このクラスからクラスパス上にある personium-unit-config.propertiesの内容にアクセスできます。
 */
public class PluginConfig {

    /**
     * personium-unit-config.propertiesの設定ファイルパスキー.
     */
    static final String KEY_CONFIG_FILE = "io.personium.configurationFile";

    /**
     * 本アプリで使うプロパティキーのプレフィクス.
     */
    static final String KEY_ROOT = "io.personium.core.";

    /**
     * Core version設定のキー.
     */
    public static final String CORE_VERSION = KEY_ROOT + "version";

    /**
     * マスタートークン設定のキー.
     */
    public static final String MASTER_TOKEN = KEY_ROOT + "masterToken";

    /**
     * ユニットユーザトークン発行者として認定するホスト名.
     */
    public static final String UNIT_USER_ISSUERS = KEY_ROOT + "unitUser.issuers";

    /**
     * ユニットのスキーム設定キー.
     */
    public static final String UNIT_SCHEME = KEY_ROOT + "unitScheme";

    /**
     * プラグインのパス設定キー.
     */
    public static final String PLUGIN_PATH = KEY_ROOT + "plugin.path";

    /**
     * Proxy関連の設定.
     */
    public static final class Proxy {
        /**
         * PROXY ホスト名.
         */
        public static final String HOST_NAME = KEY_ROOT + "proxy.host";

        /**
         * PROXY ポート番号.
         */
        public static final String PORT_NUMBER = KEY_ROOT + "proxy.port";

        /**
         * PROXY ユーザ名.
         */
        public static final String USER_NAME = KEY_ROOT + "proxy.user";

        /**
         * PROXY パスワード.
         */
        public static final String USER_PSWD = KEY_ROOT + "proxy.pswd";
    }

    static {
        // 各種メッセージ出力クラスを強制的にロードする
        PluginLog.loadConfig();
        PluginException.loadConfig();
    }

    /**
     * singleton.
     */
    private static PluginConfig singleton = new PluginConfig();

    /**
     * 設定値を格納するプロパティ実体.
     */
    private final Properties props = new Properties();

    /**
     * オーバーライドする設定値を格納するプロパティ実体.
     */
    private final Properties propsOverride = new Properties();

    /**
     * protectedなコンストラクタ.
     */
    protected PluginConfig() {
        this.doReload();
    }

    /**
     * 設定のリロード.
     */
    private synchronized void doReload() {
        Logger log = LoggerFactory.getLogger(PluginConfig.class);
        Properties properties = getUnitConfigDefaultProperties();
        Properties propertiesOverride = getPersoniumConfigProperties();
        // 読み込みに成功した場合、メンバ変数へ置換する
        if (!properties.isEmpty()) {
            this.props.clear();
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    continue;
                }
                this.props.setProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (!propertiesOverride.isEmpty()) {
            this.propsOverride.clear();
            for (Map.Entry<Object, Object> entry : propertiesOverride.entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    continue;
                }
                this.propsOverride.setProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        for (Object keyObj : propsOverride.keySet()) {
            String key = (String) keyObj;
            String value = this.propsOverride.getProperty(key);
            if (value == null) {
                continue;
            }
            log.debug("Overriding Config " + key + "=" + value);
            this.props.setProperty(key, value);
        }
    }

    /**
     * personium-unit-config-default.propertiesファイルを読み込む.
     * @return personium-unit-config-default.properties
     */
    protected Properties getUnitConfigDefaultProperties() {
        Properties properties = new Properties();
        InputStream is = PluginConfig.class.getClassLoader().getResourceAsStream(
                "personium-unit-config-default.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("failed to load config!", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("failed to close config stream", e);
            }
        }
        return properties;
    }

    /**
     * personium-unit-config.propertiesファイルを読み込む.
     * @return personium-unit-config.properties
     */
    protected Properties getPersoniumConfigProperties() {
        Logger log = LoggerFactory.getLogger(PluginConfig.class);
        Properties propertiesOverride = new Properties();
        String configFilePath = System.getProperty(KEY_CONFIG_FILE);
        InputStream is = getConfigFileInputStream(configFilePath);
        try {
            if (is != null) {
                propertiesOverride.load(is);
            } else {
                log.debug("[personium-unit-config.properties] file not found on the classpath. using default config.");
            }
        } catch (IOException e) {
            log.debug("IO Exception when loading [personium-unit-config.properties] file.");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.debug("IO Exception when closing [personium-unit-config.properties] file.");
            }
        }
        return propertiesOverride;
    }

    /**
     * personium-unit-config.propertiesをInputStream形式で取得する.
     * @param configFilePath 設定ファイルパス
     * @return personium-unit-config.properties
     */
    protected InputStream getConfigFileInputStream(String configFilePath) {
        Logger log = LoggerFactory.getLogger(PluginConfig.class);
        InputStream configFileInputStream = null;
        if (configFilePath == null) {
            configFileInputStream = PluginConfig.class.getClassLoader().getResourceAsStream(
                    "personium-unit-config.properties");
            return configFileInputStream;
        }

        try {
            // 設定ファイルを指定されたパスから読み込む
            File configFile = new File(configFilePath);
            configFileInputStream = new FileInputStream(configFile);
            log.info("personium-unit-config.properties from system properties.");
        } catch (FileNotFoundException e) {
            // 指定されたパスにファイルが存在しない場合は、クラスパス上のファイルを読み込む
            configFileInputStream = PluginConfig.class.getClassLoader().getResourceAsStream(
                    "personium-unit-config.properties");
            log.info("personium-unit-config.properties from class path.");
        }
        return configFileInputStream;
    }

    /**
     * 設定値の取得.
     * @param key キー
     * @return 設定値
     */
    private String doGet(final String key) {
        return props.getProperty(key);
    }

    /**
     * 設定値の設定.
     * @param key キー
     * @param value 値
     */
    private void doSet(final String key, final String value) {
        props.setProperty(key, value);
    }

    /**
     * すべてのプロパティを取得します。
     * @return プロパティ一覧オブジェクト
     */
    public static Properties getProperties() {
        return singleton.props;
    }

    /**
     * Key文字列を指定して設定情報を取得します.
     * @param key 設定キー
     * @return 設定値
     */
    public static String get(final String key) {
        return singleton.doGet(key);
    }

    /**
     * Key文字列を指定して設定情報を変更します.
     * @param key 設定キー
     * @param value 値
     */
    public static void set(final String key, final String value) {
        singleton.doSet(key, value);
    }

    /**
     * Core Versionの値を取得します.
     * @return Core Versionの値
     */
    public static String getCoreVersion() {
        return get(CORE_VERSION);
    }

    /**
     * ユニットマスタートークンの値を取得します.
     * @return マスタートークンの値
     */
    public static String getMasterToken() {
        return get(MASTER_TOKEN);
    }

    /**
     * @return ユニットユーザトークン発行者として認定するホスト名.
     */
    public static String getUnitUserIssuers() {
        return get(UNIT_USER_ISSUERS);
    }

    /**
     * @return ユニットのスキーム設定キー.
     */
    public static String getUnitScheme() {
        return get(UNIT_SCHEME);
    }

    /**
     * @return プラグインのパス設定キー.
     */
    public static String getPluginPath() {
        return get(PLUGIN_PATH);
    }

    /**
     * @return $proxyホスト名.
     */
    public static String getProxyHostName() {
        return get(Proxy.HOST_NAME);
    }

    /**
     * @return $proxyホスト番号.
     */
    public static int getProxyHostNumber() {
        String port = get(Proxy.PORT_NUMBER);
        if (StringUtils.isNotEmpty(port)) {
            return Integer.parseInt(port);
        }
        return 0;
    }

    /**
     * @return $proxyユーザ名.
     */
    public static String getProxyUserName() {
        return get(Proxy.USER_NAME);
    }

    /**
     * @return $proxyパスワード.
     */
    public static String getProxyPassword() {
        return get(Proxy.USER_PSWD);
    }
}
