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
import io.personium.plugin.base.PluginMessageUtils;
import io.personium.plugin.base.PluginMessageUtils.Severity;
import io.personium.plugin.base.auth.OAuth2Helper.Error;

/**
 * AuthPluginException.
 *
 * @author fjqs
 */
@SuppressWarnings("serial")
public final class AuthPluginException extends PluginException {

    /**
     * Grant-Typeの値が異常.
     */
    public static final AuthPluginException UNSUPPORTED_GRANT_TYPE = create("PR400-AN-0001", Error.UNSUPPORTED_GRANT_TYPE);
    /**
     * トークンパースエラー.
     */
    public static final AuthPluginException TOKEN_PARSE_ERROR = create("PR400-AN-0009", Error.INVALID_GRANT);
    /**
     * 有効期限切れ.
     */
    public static final AuthPluginException TOKEN_EXPIRED = create("PR400-AN-0010", Error.INVALID_GRANT);
    /**
     * 署名検証をエラー.
     */
    public static final AuthPluginException TOKEN_DSIG_INVALID = create("PR400-AN-0011", Error.INVALID_GRANT);

    /**
     * 必須パラメータが無い.
     * {0}:パラメータキー名
     */
    public static final AuthPluginException REQUIRED_PARAM_MISSING = create("PR400-AN-0016", Error.INVALID_REQUEST);
    /**
     * IDTokenの検証の中で、受け取ったIdTokenのAudienceが信頼するClientIDのリストに無かった.
     */
    public static final AuthPluginException OIDC_WRONG_AUDIENCE = create("PR400-AN-0030", Error.INVALID_GRANT);
    /**
     * OIDCの認証エラー.
     */
    public static final AuthPluginException OIDC_AUTHN_FAILED = create("PR400-AN-0031", Error.INVALID_GRANT);
    /**
     * 無効なIDToken.
     */
    public static final AuthPluginException OIDC_INVALID_ID_TOKEN = create("PR400-AN-0032", Error.INVALID_GRANT);
    /**
     * IDTokenの有効期限切れ.
     */
    public static final AuthPluginException OIDC_EXPIRED_ID_TOKEN = create("PR400-AN-0033", Error.INVALID_GRANT);

    /**
     * 接続先が想定外の値を返却.
     */
    public static final AuthPluginException OIDC_UNEXPECTED_VALUE = create("PR400-AN-0034", Error.INVALID_GRANT);

    /**
     * 公開鍵の形式ｉ異常を返却.
     */
    public static final AuthPluginException OIDC_INVALID_KEY = create("PR400-AN-0035", Error.INVALID_GRANT);
    
    /**
     * インナークラスを強制的にロードする.
     */
    public static void loadConfig() {
    }

    String error;
    String realm;

    /**
     * コンストラクタ.
     * @param status HTTPレスポンスステータス
     * @param severityエラーレベル
     * @param code エラーコード
     * @param message エラーメッセージ
     * @param error OAuth認証エラーのエラーコード
     * @param realm WWWW-Authenticateヘッダを返す場合はここにrealm値を設定する
     */
    AuthPluginException(final String code,
            final Severity severity,
            final String message,
            final int status,
            final String error,
            final String realm) {
        super(code, severity, message, status);
        this.error = error;
        this.realm = realm;
    }

    /**
     * realmを設定してオブジェクト生成.
     * @param realm2set realm
     * @return CoreAuthnException
     */
    public AuthPluginException realm(String realm2set) {
        // クローンを作成
        return new AuthPluginException(this.code, this.severity, this.message, this.status, this.error, realm2set);
    }

    /**
     * 原因例外を追加したものを作成して返します.
     * @param t 原因例外
     * @return PluginException
     */
    public PluginException reason(final Throwable t) {
        // クローンを作成して
        PluginException ret = new AuthPluginException(
                this.code, this.severity, this.message, this.status, this.error, this.realm);
        // スタックトレースをセット
        ret.setStackTrace(t.getStackTrace());
        return ret;
    }

    /**
     * ファクトリーメソッド.
     * @param code メッセージコード
     * @param error OAuth2エラーコード
     * @return AuthPluginException
     */
    public static AuthPluginException create(String code, String error) {
        int statusCode = PluginException.parseCode(code);

        // ログレベルの取得
        Severity severity = PluginMessageUtils.getSeverity(code);
        if (severity == null) {
            // ログレベルが設定されていなかったらレスポンスコードから自動的に判定する。
            severity = decideSeverity(statusCode);
        }

        // ログメッセージの取得
        String message = PluginMessageUtils.getMessage(code);

        return new AuthPluginException(code, severity, message, statusCode, error, null);
    }
}
