/**
 * Personium
 * Copyright 2014-2021 Personium Project Authors
 * - FUJITSU LIMITED
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

/**
 * Exception for Plugins.
 */
public class PluginException extends Exception {

    /** Response status code. */
    private int statusCode;
    /** Response message. */
    private String message;

    /**
     * Constructor.
     * @param statusCode Response status code
     * @param message Response message
     */
    public PluginException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    /**
     * Get Response status code.
     * @return Response status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Get Response message.
     * @return Response message
     */
    public String getMessage() {
        return message;
    }
}
