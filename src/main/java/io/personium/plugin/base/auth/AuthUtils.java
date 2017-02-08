/**
 * personium.io
 * Copyright 2014 FUJITSU LIMITED
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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.CharEncoding;

import io.personium.plugin.base.PluginConfig;
import io.personium.plugin.base.PluginException;
import io.personium.plugin.base.model.Account;
import io.personium.plugin.base.odata.OEntityWrapper;
import io.personium.plugin.base.utils.PluginUtils;

/**
 * 認証関連のユーティリティ.
 */
public final class AuthUtils {
    private static final String MD_ALGORITHM = "SHA-256";
    /** パスワード最小長. */
    private static final int MIN_PASSWORD_LENGTH = 6;
    /** パスワード最大長. */
    private static final int MAX_PASSWORD_LENGTH = 32;

    private AuthUtils() {
    }

    /**
     * パスワード文字列のHash文字列化を行う.
     * @param passwd 生パスワード文字列
     * @return ハッシュされたパスワード文字列
     */
    public static String hashPassword(final String passwd) {
        if (passwd == null) {
            return null;
        }

        // DC0 Ruby Code
        // Digest::SHA256.hexdigest(pw + "Password hash salt value")
        String str2hash = passwd + PluginConfig.getAuthPasswordSalt();
        try {
            MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
            byte[] digestBytes = md.digest(str2hash.getBytes(CharEncoding.UTF_8));
            // そちらのほうがデータ効率は良いが、DC0との互換性のためにこの実装としている。
            return PluginUtils.byteArray2HexString(digestBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * パスワードのバリデートチェックをする.
     * @param dcCredHeader dcCredHeader
     * @param entitySetName entitySetName
     * @return Hash文字列化されたパスワード
     */
    public static String checkValidatePassword(final String dcCredHeader, String entitySetName) {
        if (Account.EDM_TYPE_NAME.equals(entitySetName)) {
            if (dcCredHeader != null) {
                if ((dcCredHeader.length() >= MIN_PASSWORD_LENGTH)
                        && (dcCredHeader.length() <= MAX_PASSWORD_LENGTH)) {
                    String regex = "^[a-zA-Z0-9-_]{0,}$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher m = pattern.matcher(dcCredHeader);
                    if (!m.find()) {
                        throw PluginException.Auth.PASSWORD_INVALID;
                    }
                } else {
                    throw PluginException.Auth.PASSWORD_INVALID;
                }
            }
            String hPassStr = AuthUtils.hashPassword(dcCredHeader);
            return hPassStr;
        }
        return null;
    }

    /**
     * getAccountType.
     * @param oew oew
     * @return List<String>
     */
    public static List<String> getAccountType(OEntityWrapper oew) {
        String typeStr =  (String) oew.getProperty(Account.P_TYPE.getName()).getValue();
        String[] typeAry = typeStr.split(" ");
        return Arrays.asList(typeAry);
    }

    /**
     * isAccountTypeBasic.
     * @param oew oew
     * @return List<String>
    */
    public static boolean isAccountTypeBasic(OEntityWrapper oew) {
        return getAccountType(oew).contains(Account.TYPE_VALUE_BASIC);
    }
}
