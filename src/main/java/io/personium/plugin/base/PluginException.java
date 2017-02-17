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
 * Exception for Plug-ins.
 */
@SuppressWarnings("serial")
public class PluginException extends Exception {

	private static final int PLUGIN_TYPE_NETWORK_ERROR = 11;
	private static final int PLUGIN_TYPE_HTTP_REQUEST_FAILED = 12;
	private static final int PLUGIN_TYPE_UNEXPECTED_RESPONSE = 13;
	private static final int PLUGIN_TYPE_UNEXPECTED_VALUE = 14;

	private static final int PLUGIN_TYPE_PASSWORD_INVALID = 21;
	private static final int PLUGIN_TYPE_REQUEST_PARAM_INVALID = 22;
	private static final int PLUGIN_TYPE_P_CREDENTIAL_REQUIRED = 23;
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

	private static final int PLUGIN_TYPE_UNSUPPORTED_GRANT_TYPE = 101;
	private static final int PLUGIN_TYPE_TOKEN_PARSE_ERROR = 102;
	private static final int PLUGIN_TYPE_TOKEN_EXPIRED = 103;
	private static final int PLUGIN_TYPE_TOKEN_DSIG_INVALID = 104;
	private static final int PLUGIN_TYPE_REQUIRED_PARAM_MISSING = 105;
	private static final int PLUGIN_TYPE_OIDC_WRONG_AUDIENCE = 106;
	private static final int PLUGIN_TYPE_OIDC_AUTHN_FAILED = 107;
	private static final int PLUGIN_TYPE_OIDC_INVALID_ID_TOKEN = 108;
	private static final int PLUGIN_TYPE_OIDC_EXPIRED_ID_TOKEN = 109;
	private static final int PLUGIN_TYPE_OIDC_UNEXPECTED_VALUE = 110;
	private static final int PLUGIN_TYPE_OIDC_INVALID_KEY = 111;

	private static final int PLUGIN_TYPE_USER_DEFINED = 0;
	
    /**
     * NetWork関連エラー.
     */
    public static class NetWork extends PluginException {
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

        /**
         * 
         * @return
         */
        public static PluginException.NetWork create(
        		final String customErrorCode,
                final Severity customSeverity,
                final String customMessage,
                final int customStatus,
                final String oauthError,
                final Throwable t) {
        	return new PluginException.NetWork(customErrorCode, customSeverity, customMessage, customStatus, oauthError, t);
        }

        public NetWork(final String customErrorCode,
                final Severity customSeverity,
                final String customMessage,
                final int customStatus,
                final String oauthError,
                final Throwable t) {
        	super(customErrorCode, customSeverity, customMessage, customStatus, t);
        	this.oauthError = oauthError;
        }

        String oauthError;

		public String getOAuthError() {
			return oauthError;
		}
    }

    /**
     * 認証系エラー.
     */
    public static class Auth extends PluginException {
    	
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
        public static final PluginException P_CREDENTIAL_REQUIRED = new PluginException(PLUGIN_TYPE_P_CREDENTIAL_REQUIRED);

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
 
        /**
         * 
         * @return
         */
        public static PluginException.Auth create(
        		final String customErrorCode,
                final Severity customSeverity,
                final String customMessage,
                final int customStatus,
                final String oauthError,
                final Throwable t) {
        	return new PluginException.Auth(customErrorCode, customSeverity, customMessage, customStatus, oauthError, t);
        }

        public Auth(final String customErrorCode,
                final Severity customSeverity,
                final String customMessage,
                final int customStatus,
                final String oauthError,
                final Throwable t) {
        	super(customErrorCode, customSeverity, customMessage, customStatus, t);
        	this.oauthError = oauthError;
        }

        String oauthError;

		public String getOAuthError() {
			return oauthError;
		}
    }

    /**
     * 認証系エラー.
     */
    public static class Authn extends PluginException {
	    /**
	     * Grant-Typeの値が異常.
	     */
	    public static final PluginException UNSUPPORTED_GRANT_TYPE = new PluginException(PLUGIN_TYPE_UNSUPPORTED_GRANT_TYPE);
	    /**
	     * トークンパースエラー.
	     */
	    public static final PluginException TOKEN_PARSE_ERROR = new PluginException(PLUGIN_TYPE_TOKEN_PARSE_ERROR);
	    /**
	     * 有効期限切れ.
	     */
	    public static final PluginException TOKEN_EXPIRED = new PluginException(PLUGIN_TYPE_TOKEN_EXPIRED);
	    /**
	     * 署名検証をエラー.
	     */
	    public static final PluginException TOKEN_DSIG_INVALID = new PluginException(PLUGIN_TYPE_TOKEN_DSIG_INVALID);
	
	    /**
	     * 必須パラメータが無い.
	     * {0}:パラメータキー名
	     */
	    public static final PluginException REQUIRED_PARAM_MISSING = new PluginException(PLUGIN_TYPE_REQUIRED_PARAM_MISSING);
	    /**
	     * IDTokenの検証の中で、受け取ったIdTokenのAudienceが信頼するClientIDのリストに無かった.
	     */
	    public static final PluginException OIDC_WRONG_AUDIENCE = new PluginException(PLUGIN_TYPE_OIDC_WRONG_AUDIENCE);
	    /**
	     * OIDCの認証エラー.
	     */
	    public static final PluginException OIDC_AUTHN_FAILED = new PluginException(PLUGIN_TYPE_OIDC_AUTHN_FAILED);
	    /**
	     * 無効なIDToken.
	     */
	    public static final PluginException OIDC_INVALID_ID_TOKEN = new PluginException(PLUGIN_TYPE_OIDC_INVALID_ID_TOKEN);
	    /**
	     * IDTokenの有効期限切れ.
	     */
	    public static final PluginException OIDC_EXPIRED_ID_TOKEN = new PluginException(PLUGIN_TYPE_OIDC_EXPIRED_ID_TOKEN);
	
	    /**
	     * 接続先が想定外の値を返却.
	     */
	    public static final PluginException OIDC_UNEXPECTED_VALUE = new PluginException(PLUGIN_TYPE_OIDC_UNEXPECTED_VALUE);
	
	    /**
	     * 公開鍵の形式ｉ異常を返却.
	     */
	    public static final PluginException OIDC_INVALID_KEY = new PluginException(PLUGIN_TYPE_OIDC_INVALID_KEY);

        /**
         * 
         * @return
         */
        public static PluginException.Authn create(
        		final String customErrorCode,
                final Severity customSeverity,
                final String customMessage,
                final int customStatus,
                final String oauthError,
                final Throwable t) {
        	return new PluginException.Authn(customErrorCode, customSeverity, customMessage, customStatus, oauthError, t);
        }

        public Authn(final String customErrorCode,
                final Severity customSeverity,
                final String customMessage,
                final int customStatus,
                final String oauthError,
                final Throwable t) {
        	super(customErrorCode, customSeverity, customMessage, customStatus, t);
        	this.oauthError = oauthError;
        }

        String oauthError;

		public String getOAuthError() {
			return oauthError;
		}
    }

    public String customErrorCode;
    public Severity customSeverity;
    public String customMessage;
    public int customStatus;

    protected int type;
    protected Object[] params;

	public int getType() {
		return type;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	/**
     * インナークラスを強制的にロードする.
     * エラー分類のインナークラスが追加になったらここに追加すること.
     */
    public static void loadConfig() {
        new NetWork(null, null, null, 0, null, null);
        new Auth(null, null, null, 0, null, null);
        new Authn(null, null, null, 0, null, null);
    }

    /**
     * constructor.
     */
    private PluginException() {
        super();
    }

    /**
     * constructor.
     */
    private PluginException(int type) {
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
    private PluginException(final String customErrorCode,
		            final Severity customSeverity,
		            final String customMessage,
		            final int customStatus,
		            final Throwable t) {
        super(t);
        this.type = PLUGIN_TYPE_USER_DEFINED;
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
    private PluginException(final String customErrorCode,
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
     * 
     * @return
     */
    public static PluginException create(
    		final String customErrorCode,
            final Severity customSeverity,
            final String customMessage,
            final int customStatus,
            final Throwable t) {
    	return new PluginException(customErrorCode, customSeverity, customMessage, customStatus, t);
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
