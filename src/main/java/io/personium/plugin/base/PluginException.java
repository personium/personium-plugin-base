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

import io.personium.plugin.base.PluginMessageUtils.Severity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ログメッセージ作成クラス.
 */
@SuppressWarnings("serial")
public class PluginException extends Exception {

	private static final int PLUGIN_TYPE_NETWORK_ERROR = 11;
	private static final int PLUGIN_TYPE_HTTP_REQUEST_FAILED = 12;
	private static final int PLUGIN_TYPE_UNEXPECTED_RESPONSE = 13;
	private static final int PLUGIN_TYPE_UNEXPECTED_VALUE = 14;

	private static final int PLUGIN_TYPE_PASSWORD_INVALID = 21;
	private static final int PLUGIN_TYPE_REQUEST_PARAM_INVALID = 22;
	private static final int PLUGIN_TYPE_DC_CREDENTIAL_REQUIRED = 23;
	private static final int PLUGIN_TYPE_UNITUSER_ACCESS_REQUIRED = 24;
	private static final int PLUGIN_TYPE_NECESSARY_PRIVILEGE_LACKING = 25;
	private static final int PLUGIN_TYPE_NOT_YOURS = 26;
	private static final int PLUGIN_TYPE_SCHEMA_AUTH_REQUIRED = 27;
	private static final int PLUGIN_TYPE_SCHEMA_MISMATCH = 28;
	private static final int PLUGIN_TYPE_INSUFFICIENT_SCHEMA_AUTHZ_LEVEL = 29;
	private static final int PLUGIN_TYPE_ROOT_CA_CRT_SETTING_ERROR = 30;
	private static final int PLUGIN_TYPE_REQUEST_PARAM_CLIENTID_INVALID = 31;
	private static final int PLUGIN_TYPE_REQUEST_PARAM_REDIRECT_INVALID = 32;
	private static final int PLUGIN_TYPE_IDTOKEN_ENCODED_INVALID = 33;
	private static final int PLUGIN_TYPE_JSON_PARSE_ERROR = 34;

    /**
     * NetWork関連エラー.
     */
    public static class NetWork {
        /**
         * NetWork関連エラー.
         */
        public static final PluginException NETWORK_ERROR = new PluginException(PLUGIN_TYPE_NETWORK_ERROR);
        /**
         * HTTPリクエストに失敗.
         */
        public static final PluginException HTTP_REQUEST_FAILED = new PluginException(PLUGIN_TYPE_HTTP_REQUEST_FAILED);
        /**
         * 接続先が想定外の応答を返却.
         */
        public static final PluginException UNEXPECTED_RESPONSE = new PluginException(PLUGIN_TYPE_UNEXPECTED_RESPONSE);
        /**
         * 接続先が想定外の値を返却.
         */
        public static final PluginException UNEXPECTED_VALUE = new PluginException(PLUGIN_TYPE_UNEXPECTED_VALUE);
    }

    /**
     * 認証系エラー.
     */
    public static class Auth {
        /**
         * パスワード文字列が不正.
         */
        public static final PluginException PASSWORD_INVALID = new PluginException(PLUGIN_TYPE_PASSWORD_INVALID);
        /**
         * リクエストパラメータが不正.
         */
        public static final PluginException REQUEST_PARAM_INVALID = new PluginException(PLUGIN_TYPE_REQUEST_PARAM_INVALID);
        /**
         * パスワード文字列が不正.
         */
        public static final PluginException DC_CREDENTIAL_REQUIRED = new PluginException(PLUGIN_TYPE_DC_CREDENTIAL_REQUIRED);

        /**
         * ユニットユーザアクセスではない.
         */
        public static final PluginException UNITUSER_ACCESS_REQUIRED = new PluginException(PLUGIN_TYPE_UNITUSER_ACCESS_REQUIRED);
        /**
         * 必要な権限が無い.
         */
        public static final PluginException NECESSARY_PRIVILEGE_LACKING = new PluginException(PLUGIN_TYPE_NECESSARY_PRIVILEGE_LACKING);
        /**
         * 認証ヘッダに指定されたユニットユーザではアクセセスできない.
         */
        public static final PluginException NOT_YOURS = new PluginException(PLUGIN_TYPE_NOT_YOURS);
        /**
         * スキーマ認証が必要.
         */
        public static final PluginException SCHEMA_AUTH_REQUIRED = new PluginException(PLUGIN_TYPE_SCHEMA_AUTH_REQUIRED);
        /**
         * このスキーマ認証ではアクセスできない.
         */
        public static final PluginException SCHEMA_MISMATCH = new PluginException(PLUGIN_TYPE_SCHEMA_MISMATCH);
        /**
         * スキーマ認証レベルが不足.
         */
        public static final PluginException INSUFFICIENT_SCHEMA_AUTHZ_LEVEL = new PluginException(PLUGIN_TYPE_INSUFFICIENT_SCHEMA_AUTHZ_LEVEL);
        /**
         * ルートCA証明書の設定エラー.
         */
        public static final PluginException ROOT_CA_CRT_SETTING_ERROR = new PluginException(PLUGIN_TYPE_ROOT_CA_CRT_SETTING_ERROR);
        /**
         * リクエストパラメータが不正.
         */
        public static final PluginException REQUEST_PARAM_CLIENTID_INVALID = new PluginException(PLUGIN_TYPE_REQUEST_PARAM_CLIENTID_INVALID);
        /**
         * リクエストパラメータが不正.
         */
        public static final PluginException REQUEST_PARAM_REDIRECT_INVALID = new PluginException(PLUGIN_TYPE_REQUEST_PARAM_REDIRECT_INVALID);
        /**
         * IdTokenが不正.
         */
        public static final PluginException IDTOKEN_ENCODED_INVALID = new PluginException(PLUGIN_TYPE_IDTOKEN_ENCODED_INVALID);

        /**
         * JSONのパースに失敗したとき.
         */
        public static final PluginException JSON_PARSE_ERROR = new PluginException(PLUGIN_TYPE_JSON_PARSE_ERROR);
    }
    
    public String customErrorCode;
    public Severity customSeverity;
    public String customMessage;
    public int customStatus;

    protected int type;
    protected String[] params;

	public int getType() {
		return type;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	/**
     * インナークラスを強制的にロードする.
     * エラー分類のインナークラスが追加になったらここに追加すること.
     */
    public static void loadConfig() {
        new Auth();
        new NetWork();
    }

    /**
     * constructor.
     */
    protected PluginException(int type) {
        super();
        this.type = type;
    }

    /**
     * constructor.
     * @param customStatus HTTPレスポンスステータス
     * @param severityエラーレベル
     * @param customErrorCode エラーコード
     * @param customMessage エラーメッセージ
     */
    PluginException(final String customErrorCode,
            final Severity customSeverity,
            final String customMessage,
            final int customStatus,
            final Throwable t) {
        super(t);
        this.customErrorCode = customErrorCode;
        this.customSeverity = customSeverity;
        this.customMessage = customMessage;
        this.customStatus = customStatus;
    }

    /**
     * constructor.
     * @param customStatus HTTPレスポンスステータス
     * @param customSeverityエラーレベル
     * @param customErrorCode エラーコード
     * @param customMessage エラーメッセージ
     */
    protected PluginException(final String customErrorCode,
            final Severity customSeverity,
            final String customMessage,
            final int customStatus) {
        this(customErrorCode, customSeverity, customMessage, customStatus, null);
    }

    /**
     * ログレベルを返却する.
     * @return ログレベル
     */
    public Severity getSeverity() {
        return this.customSeverity;
    }

    /**
     * HTTPステータスコードを返却する.
     * @return HTTPステータスコード
     */
    public int getStatus() {
        return this.customStatus;
    }

    /**
     * エラーコードを返却する.
     * @return エラーコード
     */
    public String getCustomErrorCode() {
        return this.customErrorCode;
    }

    @Override
    public String getMessage() {
        return this.customMessage;
    }

    /**
     * 原因例外を追加したものを作成して返します.
     * @param t 原因例外
     * @return PluginException
     */
    public PluginException reason(final Throwable t) {
        // クローンを作成
        PluginException ret = new PluginException(this.customErrorCode, this.customSeverity, this.customMessage, this.customStatus, t);
        return ret;
    }

    /**
     * メッセージをパラメタ置換したものを作成して返します. エラーメッセージ上の $1 $2 等の表現がパラメタ置換用キーワードです。
     * @param params 付加メッセージ
     * @return PluginException
     */
    public PluginException params(final String... params) {
        // メッセージ置換クローンを作成
        PluginException ret = new PluginException(this.getType());
        ret.setParams(params);
        return ret;
    }

    /**
     * メッセージコードのパース.
     * @param customErrorCode メッセージコード
     * @return ステータスコードまたはログメッセージの場合は-1。
     */
    public static int parseCode(String customErrorCode) {
        Pattern p = Pattern.compile("^PR(\\d{3})-\\w{2}-\\d{4}$");
        Matcher m = p.matcher(customErrorCode);
        if (!m.matches()) {
            throw new IllegalArgumentException(
                    "customMessage code should be in \"PR000-OD-0000\" format. code=[" + customErrorCode + "].");
        }
        return Integer.parseInt(m.group(1));
    }

}
