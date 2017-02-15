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

import io.personium.plugin.base.PluginException;
import io.personium.plugin.base.PluginMessageUtils.Severity;

/**
 * AuthPluginException.
 *
 */
@SuppressWarnings("serial")
public final class AuthPluginException extends PluginException {

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

    /**
     * Grant-Typeの値が異常.
     */
    public static final AuthPluginException UNSUPPORTED_GRANT_TYPE = new AuthPluginException(PLUGIN_TYPE_UNSUPPORTED_GRANT_TYPE);
    /**
     * トークンパースエラー.
     */
    public static final AuthPluginException TOKEN_PARSE_ERROR = new AuthPluginException(PLUGIN_TYPE_TOKEN_PARSE_ERROR);
    /**
     * 有効期限切れ.
     */
    public static final AuthPluginException TOKEN_EXPIRED = new AuthPluginException(PLUGIN_TYPE_TOKEN_EXPIRED);
    /**
     * 署名検証をエラー.
     */
    public static final AuthPluginException TOKEN_DSIG_INVALID = new AuthPluginException(PLUGIN_TYPE_TOKEN_DSIG_INVALID);

    /**
     * 必須パラメータが無い.
     * {0}:パラメータキー名
     */
    public static final AuthPluginException REQUIRED_PARAM_MISSING = new AuthPluginException(PLUGIN_TYPE_REQUIRED_PARAM_MISSING);
    /**
     * IDTokenの検証の中で、受け取ったIdTokenのAudienceが信頼するClientIDのリストに無かった.
     */
    public static final AuthPluginException OIDC_WRONG_AUDIENCE = new AuthPluginException(PLUGIN_TYPE_OIDC_WRONG_AUDIENCE);
    /**
     * OIDCの認証エラー.
     */
    public static final AuthPluginException OIDC_AUTHN_FAILED = new AuthPluginException(PLUGIN_TYPE_OIDC_AUTHN_FAILED);
    /**
     * 無効なIDToken.
     */
    public static final AuthPluginException OIDC_INVALID_ID_TOKEN = new AuthPluginException(PLUGIN_TYPE_OIDC_INVALID_ID_TOKEN);
    /**
     * IDTokenの有効期限切れ.
     */
    public static final AuthPluginException OIDC_EXPIRED_ID_TOKEN = new AuthPluginException(PLUGIN_TYPE_OIDC_EXPIRED_ID_TOKEN);

    /**
     * 接続先が想定外の値を返却.
     */
    public static final AuthPluginException OIDC_UNEXPECTED_VALUE = new AuthPluginException(PLUGIN_TYPE_OIDC_UNEXPECTED_VALUE);

    /**
     * 公開鍵の形式ｉ異常を返却.
     */
    public static final AuthPluginException OIDC_INVALID_KEY = new AuthPluginException(PLUGIN_TYPE_OIDC_INVALID_KEY);

    /**
     * インナークラスを強制的にロードする.
     */
    public static void loadConfig() {
    }

    String customError;

    /**
     * constructor.
     */
    public AuthPluginException(int type) {
        super(type);
    }

    /**
     * constructor.
     * @param customStatus HTTPレスポンスステータス
     * @param severityエラーレベル
     * @param code エラーコード
     * @param customMessage エラーメッセージ
     * @param error OAuth認証エラーのエラーコード
     */
    public AuthPluginException(final String customErrorCode,
            final Severity customSeverity,
            final String customMessage,
            final int customStatus,
            final String customError) {
        super(customErrorCode, customSeverity, customMessage, customStatus);
        this.customError = customError;
    }

    /**
     * realmを設定してオブジェクト生成.
     * @param realm2set realm
     * @return CoreAuthnException
     */
    public AuthPluginException realm(String realm2set) {
        // クローンを作成
        return new AuthPluginException(this.customErrorCode, this.customSeverity, this.customMessage, this.customStatus, this.customError);
    }

    /**
     * メッセージをパラメタ置換したものを作成して返します. エラーメッセージ上の $1 $2 等の表現がパラメタ置換用キーワードです。
     * @param params 付加メッセージ
     * @return PluginException
     */
    public AuthPluginException params(final String... params) {
        // メッセージ置換クローンを作成
    	AuthPluginException ret = new AuthPluginException(this.getType());
        ret.setParams(params);
        return ret;
    }

    /**
     * 原因例外を追加したものを作成して返します.
     * @param t 原因例外
     * @return PluginException
     */
    public AuthPluginException reason(final Throwable t) {
        // クローンを作成して
        AuthPluginException ret = new AuthPluginException(
                this.customErrorCode, this.customSeverity, this.customMessage, this.customStatus, this.customError);
        // スタックトレースをセット
        ret.setStackTrace(t.getStackTrace());
        return ret;
    }

    /**
     * conversion AuthPluginException.
     * @param PluginException pe
     * @return ape AuthPluginException
     */
    public static AuthPluginException conversion(final PluginException pe){
    	AuthPluginException ape = new AuthPluginException(pe.getType());
    	ape.setParams(pe.getParams());
        ape.setStackTrace(pe.getStackTrace());
//    	ape.reason(pe);
    	return ape;
    }
}
