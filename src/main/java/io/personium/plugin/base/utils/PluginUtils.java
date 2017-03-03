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

import io.personium.plugin.base.PluginException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pluginを作成する人が使用したいであろう各種ユーティリティ関数を集めたクラス.
 */
public final class PluginUtils {

    static Logger log = LoggerFactory.getLogger(PluginUtils.class);

    private PluginUtils() {
    }

    /**
     * プログラムリソース中のファイルをStringとして読み出します.
     * @param resPath リソースパス
     * @param encoding Encoding
     * @return 読み出した文字列
     */
    public static String readStringResource(final String resPath, final String encoding) {
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = PluginUtils.class.getClassLoader().getResourceAsStream(resPath);
            br = new BufferedReader(new InputStreamReader(is, encoding));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Base64urlのエンコードを行う.
     * 厳密にはRFC４648参照。（といいたいが、少し表現があいまい。） ---------------------------------------------------
     * http://tools.ietf.org/html/rfc4648 --------------------------------------------------- 5. Base 64 Encoding with
     * URL and Filename Safe Alphabet The Base 64 encoding with an URL and filename safe alphabet has been used in [12].
     * ＵＲＬとファイル名で安全なアルファベットのベース６４符号化は[12]で 使われました。 An alternative alphabet has been suggested that would use "~" as the
     * 63rd character. Since the "~" character has special meaning in some file system environments, the encoding
     * described in this section is recommended instead. The remaining unreserved URI character is ".", but some file
     * system environments do not permit multiple "." in a filename, thus making the "." character unattractive as well.
     * ６３番目のアルファベットの代わりの文字として"~"を使うのが示唆され ました。"~"文字があるファイルシステム環境で特別な意味を持つので、 この章で記述された符号化はその代わりとして勧められます。残りの予
     * 約なしのＵＲＩ文字は"."ですが、あるファイルシステム環境で複数の"." は許されず、"."文字も魅力的でありません。 The pad character "=" is typically percent-encoded
     * when used in an URI [9], but if the data length is known implicitly, this can be avoided by skipping the padding;
     * see section 3.2. ＵＲＩ[9]で使われるとき、穴埋め文字"="は一般にパーセント符号化さ れますが、もしデータ長が暗黙のうちにわかるなら、穴埋めをスキップす
     * ることによってこれを避けれます；３.２章を見て下さい。 This encoding may be referred to as "base64url". This encoding should not be regarded
     * as the same as the "base64" encoding and should not be referred to as only "base64". Unless clarified otherwise,
     * "base64" refers to the base 64 in the previous section. この符号化は"base64url"と述べられるかもしれません。この符号化は
     * "base64"符号化と同じと見なされるべきではなくて、単純に「ベース６ ４」と述べるべきではありません。他に明示されない限り、"base64"が 前章のベース６４を意味します。 This encoding is
     * technically identical to the previous one, except for the 62:nd and 63:rd alphabet character, as indicated in
     * Table 2. この符号化は、６２番目と６３番目のアルファベット文字が表２で示さ れたものである以外は、技術的に前のものとまったく同じです。 Table 2: The "URL and Filename safe" Base
     * 64 Alphabet 表２：「ＵＲＬとファイル名で安全な」ベース６４アルファベット Value Encoding Value Encoding Value Encoding Value Encoding 0 A 17 R
     * 34 i 51 z 1 B 18 S 35 j 52 0 2 C 19 T 36 k 53 1 3 D 20 U 37 l 54 2 4 E 21 V 38 m 55 3 5 F 22 W 39 n 56 4 6 G 23 X
     * 40 o 57 5 7 H 24 Y 41 p 58 6 8 I 25 Z 42 q 59 7 9 J 26 a 43 r 60 8 10 K 27 b 44 s 61 9 11 L 28 c 45 t 62 -
     * (minus) 12 M 29 d 46 u 63 _ 13 N 30 e 47 v (underline) 14 O 31 f 48 w 15 P 32 g 49 x 16 Q 33 h 50 y (pad) =
     * @param in エンコードしたいbyte列
     * @return エンコードされたあとの文字列
     */
    public static String encodeBase64Url(final byte[] in) {
        return Base64.encodeBase64URLSafeString(in);
    }

    /**
     * Base64urlのエンコードを行う.
     * @param inStr 入力ストリーム
     * @return 文字列
     */
    public static String encodeBase64Url(final InputStream inStr) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int l = 0;
        try {
            while ((l = inStr.read()) != -1) {
                baos.write(l);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Base64.encodeBase64URLSafeString(baos.toByteArray());
    }

    /**
     * Base64urlのデコードを行う.
     * @param in デコードしたい文字列
     * @return デコードされたbyte列
     */
    public static byte[] decodeBase64Url(final String in) {
        return Base64.decodeBase64(in);
    }

    static final int BITS_HEX_DIGIT = 4;
    static final int HEX_DIGIT_MASK = 0x0F;

    /**
     * バイト列を16進数の文字列に変換する.
     * TODO さすがにこんなのどこかにライブラリありそうだけど.
     * @param input 入力バイト列
     * @return 16進数文字列
     */
    public static String byteArray2HexString(final byte[] input) {
        StringBuffer buff = new StringBuffer();
        int count = input.length;
        for (int i = 0; i < count; i++) {
            buff.append(Integer.toHexString((input[i] >> BITS_HEX_DIGIT) & HEX_DIGIT_MASK));
            buff.append(Integer.toHexString(input[i] & HEX_DIGIT_MASK));
        }
        return buff.toString();
    }

    /**
     * URLエンコードのエンコードを行う.
     * @param in Urlエンコードしたい文字列
     * @return Urlエンコードされた文字列
     */
    public static String encodeUrlComp(final String in) {
        try {
            return URLEncoder.encode(in, CharEncoding.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * URLエンコードのデコードを行う.
     * @param in Urlエンコードされた文字列
     * @return 元の文字列
     */
    public static String decodeUrlComp(final String in) {
        try {
            return URLDecoder.decode(in, CharEncoding.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * InputStreamをすべて読み、String型で返す.
     * @param is InputStream
     * @return 文字列
     */
    public static String readInputStreamAsString(InputStream is) {

        InputStreamReader isr = null;
        BufferedReader reader = null;
        String bodyString = null;
        try {

            isr = new InputStreamReader(is, "UTF-8");
            reader = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            int chr;
            while ((chr = is.read()) != -1) {
                sb.append((char) chr);
            }
            bodyString = sb.toString();
        } catch (IllegalStateException e) {
            log.info(e.getMessage());
        } catch (IOException e) {
            log.info(e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        return bodyString;
    }

    /**
     * Cacheを効かせるため、ClientをStaticとする. たかだか限定されたURLのbodyを保存するのみであり、
     * 最大キャッシュサイズはCacheConfigクラスで定義された16kbyte程度である. そのため、Staticで持つこととした.
     */
    private static HttpClient httpClient = new CachingHttpClient();

    /**
     * HTTPでJSONオブジェクトを取得する処理. Cacheが利用可能であればその値を用いる.
     *
     * @param url URL
     * @return JSONObject
     * @throws PluginException 
     */
    public static JSONObject getHttpJSON(String url) throws PluginException {
        HttpGet get = new HttpGet(url);
        HttpResponse res = null;
        String status = null;
        CloseableHttpClient httpProxyClient = null;
        try {
            // Connection Host
            if (ProxyUtils.isProxyHost()) {
                httpProxyClient = ProxyUtils.proxyHttpClient();
                get.setConfig(ProxyUtils.getRequestConfig());
                res = httpProxyClient.execute(get);
            } else {
                res = httpClient.execute(get);
            }

            InputStream is = res.getEntity().getContent();
            status = String.valueOf(res.getStatusLine().getStatusCode());
            String body = PluginUtils.readInputStreamAsString(is);
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(body);
            return jsonObj;
        } catch (ClientProtocolException e) {
            // HTTPのプロトコル違反
            throw PluginException.NetWork.UNEXPECTED_RESPONSE.params(url, "proper HTTP response", status);
        } catch (IOException e) {
            // サーバーに接続できない場合に発生
            throw PluginException.NetWork.HTTP_REQUEST_FAILED.params(HttpGet.METHOD_NAME, url, status);
        } catch (ParseException e) {
            // JSONでないものを返してきた
            throw PluginException.NetWork.UNEXPECTED_RESPONSE.params(url, "JSON", status);
        }
    }
}
