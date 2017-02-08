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

/**
 * Auth Constants.
 * @author coe
 *
 */
public final class AuthConst {
    /** plugin type **/
    /** Auth. **/
    public static final String TYPE_AUTH = "auth";

    /** map key **/
    /** set body   token. **/
    public static final String KEY_TOKEN = "token";
    /** get result email. **/
    public static final String KEY_ACCOUT = "accountName";
    /** oidc. **/
    public static final String KEY_OIDC_TYPE = "oidc";

    /** message. **/
    public static final String KEY_MESSAGE = "message";

    /**
     * Can not instantiate.
     */
    private AuthConst() {
    }
}
