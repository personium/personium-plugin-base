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
import io.personium.plugin.base.utils.EscapeControlCode;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

/**
 * ログメッセージ作成クラス.
 */
@SuppressWarnings("serial")
public class PluginException extends Exception {

    /**
     * NetWork関連エラー.
     */
    public static class NetWork {
        /**
         * NetWork関連エラー.
         */
        public static final PluginException NETWORK_ERROR = create("PR500-NW-0000");
        /**
         * HTTPリクエストに失敗.
         */
        public static final PluginException HTTP_REQUEST_FAILED = create("PR500-NW-0001");
        /**
         * 接続先が想定外の応答を返却.
         */
        public static final PluginException UNEXPECTED_RESPONSE = create("PR500-NW-0002");
        /**
         * 接続先が想定外の値を返却.
         */
        public static final PluginException UNEXPECTED_VALUE = create("PR500-NW-0003");
    }

    /**
     * 認証系エラー.
     */
    public static class Auth {
        /**
         * パスワード文字列が不正.
         */
        public static final PluginException PASSWORD_INVALID = create("PR400-AU-0001");
        /**
         * リクエストパラメータが不正.
         */
        public static final PluginException REQUEST_PARAM_INVALID = create("PR400-AU-0002");
        /**
         * パスワード文字列が不正.
         */
        public static final PluginException DC_CREDENTIAL_REQUIRED = create("PR400-AU-0003");

        /**
         * ユニットユーザアクセスではない.
         */
        public static final PluginException UNITUSER_ACCESS_REQUIRED = create("PR403-AU-0001");
        /**
         * 必要な権限が無い.
         */
        public static final PluginException NECESSARY_PRIVILEGE_LACKING = create("PR403-AU-0002");
        /**
         * 認証ヘッダに指定されたユニットユーザではアクセセスできない.
         */
        public static final PluginException NOT_YOURS = create("PR403-AU-0003");
        /**
         * スキーマ認証が必要.
         */
        public static final PluginException SCHEMA_AUTH_REQUIRED = create("PR403-AU-0004");
        /**
         * このスキーマ認証ではアクセスできない.
         */
        public static final PluginException SCHEMA_MISMATCH = create("PR403-AU-0005");
        /**
         * スキーマ認証レベルが不足.
         */
        public static final PluginException INSUFFICIENT_SCHEMA_AUTHZ_LEVEL = create("PR403-AU-0006");
        /**
         * ルートCA証明書の設定エラー.
         */
        public static final PluginException ROOT_CA_CRT_SETTING_ERROR = create("PR500-AN-0001");
        /**
         * リクエストパラメータが不正.
         */
        public static final PluginException REQUEST_PARAM_CLIENTID_INVALID = create("PR400-AZ-0002");
        /**
         * リクエストパラメータが不正.
         */
        public static final PluginException REQUEST_PARAM_REDIRECT_INVALID = create("PR400-AZ-0003");
        /**
         * IdTokenが不正.
         */
        public static final PluginException IDTOKEN_ENCODED_INVALID = create("PR400-AZ-0004");

        /**
         * JSONのパースに失敗したとき.
         */
        public static final PluginException JSON_PARSE_ERROR = create("PR400-OD-0001");
    }

    public String code;
    public Severity severity;
    public String message;
    public int status;

    /**
     * インナークラスを強制的にロードする.
     * エラー分類のインナークラスが追加になったらここに追加すること.
     */
    public static void loadConfig() {
        new Auth();
        new NetWork();
    }

    /**
     * コンストラクタ.
     * @param status HTTPレスポンスステータス
     * @param severityエラーレベル
     * @param code エラーコード
     * @param message エラーメッセージ
     */
    PluginException(final String code,
            final Severity severity,
            final String message,
            final int status,
            final Throwable t) {
        super(t);
        this.code = code;
        this.severity = severity;
        this.message = message;
        this.status = status;
    }

    /**
     * コンストラクタ.
     * @param status HTTPレスポンスステータス
     * @param severityエラーレベル
     * @param code エラーコード
     * @param message エラーメッセージ
     */
    protected PluginException(final String code,
            final Severity severity,
            final String message,
            final int status) {
        this(code, severity, message, status, null);
    }

    /**
     * ログレベルを返却する.
     * @return ログレベル
     */
    public Severity getSeverity() {
        return this.severity;
    }

    /**
     * HTTPステータスコードを返却する.
     * @return HTTPステータスコード
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * エラーコードを返却する.
     * @return エラーコード
     */
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * 原因例外を追加したものを作成して返します.
     * @param t 原因例外
     * @return PluginException
     */
    public PluginException reason(final Throwable t) {
        // クローンを作成
        PluginException ret = new PluginException(this.code, this.severity, this.message, this.status, t);
        return ret;
    }

    /**
     * メッセージをパラメタ置換したものを作成して返します. エラーメッセージ上の $1 $2 等の表現がパラメタ置換用キーワードです。
     * @param params 付加メッセージ
     * @return PluginException
     */
    public PluginException params(final Object... params) {
        // 置換メッセージ作成
        String ms = MessageFormat.format(this.message, params);

        // 制御コードのエスケープ処理
        ms = EscapeControlCode.escape(ms);

        // メッセージ置換クローンを作成
        PluginException ret = new PluginException(this.code, this.severity, ms, this.status);
        return ret;
    }

    /**
     * ファクトリーメソッド.
     * @param code メッセージコード
     * @return PluginException
     */
    public static PluginException create(String code) {
        int statusCode = parseCode(code);

        // ログレベルの取得
        Severity severity = PluginMessageUtils.getSeverity(code);
        if (severity == null) {
            // ログレベルが設定されていなかったらレスポンスコードから自動的に判定する。
            severity = decideSeverity(statusCode);
        }

        // ログメッセージの取得
        String message = PluginMessageUtils.getMessage(code);

        return new PluginException(code, severity, message, statusCode);
    }

    /**
     * レスポンスコードからログレベルの判定.
     * @param statusCode ステータスコード
     * @return ステータスコードから判定されたログレベル
     */
    public static Severity decideSeverity(int statusCode) {
        // 設定が省略されている場合はエラーコードからログレベルを取得
        if (statusCode >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            // 500系の場合はウォーニング（500以上はまとめてウォーニング）
            return Severity.WARN;
        } else if (statusCode >= HttpStatus.SC_BAD_REQUEST) {
            // 400系の場合はインフォ
            return Severity.INFO;
        } else {
            // それ以外の場合は考えられないのでウォーニング.
            // 200系とか300系をPersoiumCoreExceptionで処理する場合はログレベル設定をちゃんと書きましょう.
            return Severity.WARN;
        }
    }

    /**
     * メッセージコードのパース.
     * @param code メッセージコード
     * @return ステータスコードまたはログメッセージの場合は-1。
     */
    public static int parseCode(String code) {
        Pattern p = Pattern.compile("^PR(\\d{3})-\\w{2}-\\d{4}$");
        Matcher m = p.matcher(code);
        if (!m.matches()) {
            throw new IllegalArgumentException(
                    "message code should be in \"PR000-OD-0000\" format. code=[" + code + "].");
        }
        return Integer.parseInt(m.group(1));
    }

}
