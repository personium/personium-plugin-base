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

/**
 * Auth plugin result.
 *
 */
public class AuthenticatedIdentity {

    /** Account name. */
    private String accountName;
    /** Account type. */
    private String accountType;

    /**
     * getAccountName.
     * @return accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * setAccountName.
     * @param accountName String
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * getAccountType.
     * @return accountName
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * setAccountType.
     * @param accountType String
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
