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
package io.personium.plugin.base.auth;

import org.apache.http.HttpStatus;

import io.personium.plugin.base.PluginException;

/**
 * Exception for AuthPlugins.
 */
public class AuthPluginException extends PluginException {

    /** OAuth2.0 response "error". */
    private String oAuthError;

    /**
     * Constructor.
     * @param oAuth2Error OAuth2 error code
     * @param statusCode Response status code
     * @param message Response message
     */
    protected AuthPluginException(String oAuth2Error, int statusCode, String message) {
        super(statusCode, message);
        oAuthError = oAuth2Error;
    }

    /**
     * Get OAuth2.0 response "error".
     * @return OAuth2.0 response "error"
     */
    public String getOAuthError() {
        return oAuthError;
    }

    /**
     * InvalidRequest.
     */
    public static class InvalidRequest extends AuthPluginException {
        /**
         * The request is missing a required parameter, includes an
         * unsupported parameter value (other than grant type),
         * repeats a parameter, includes multiple credentials,
         * utilizes more than one mechanism for authenticating the
         * client, or is otherwise malformed.
         * @param message Response message
         */
        public InvalidRequest(String message) {
            super(OAuth2Helper.Error.INVALID_REQUEST, HttpStatus.SC_BAD_REQUEST, message);
        }
    }

    /**
     * InvalidClient.
     */
    public static class InvalidClient extends AuthPluginException {
        /**
         * Client authentication failed (e.g. unknown client, no
         * client authentication included, or unsupported
         * authentication method). The authorization server MAY
         * return an HTTP 401 (Unauthorized) status code to indicate
         * which HTTP authentication schemes are supported. If the
         * client attempted to authenticate via the "Authorization"
         * request header field, the authorization server MUST
         * respond with an HTTP 401 (Unauthorized) status code, and
         * include the "WWW-Authenticate" response header field
         * matching the authentication scheme used by the client.
         * @param message Response message
         */
        public InvalidClient(String message) {
            super(OAuth2Helper.Error.INVALID_CLIENT, HttpStatus.SC_BAD_REQUEST, message);
        }
    }

    /**
     * InvalidGrant.
     */
    public static class InvalidGrant extends AuthPluginException {
        /**
         * The provided authorization grant (e.g. authorization code, resource owner credentials) or refresh token is
         * invalid, expired, revoked, does not match the redirection
         * URI used in the authorization request, or was issued to
         * another client.
         * @param message Response message
         */
        public InvalidGrant(String message) {
            super(OAuth2Helper.Error.INVALID_GRANT, HttpStatus.SC_BAD_REQUEST, message);
        }
    }

    /**
     * UnauthorizedClient.
     */
    public static class UnauthorizedClient extends AuthPluginException {
        /**
         * The authenticated client is not authorized to use this authorization grant type.
         * @param message Response message
         */
        public UnauthorizedClient(String message) {
            super(OAuth2Helper.Error.UNAUTHORIZED_CLIENT, HttpStatus.SC_UNAUTHORIZED, message);
        }
    }

    /**
     * AccessDenied.
     */
    public static class AccessDenied extends AuthPluginException {
        /**
         * The resource owner or authorization server denied the request.
         * @param message Response message
         */
        public AccessDenied(String message) {
            super(OAuth2Helper.Error.ACCESS_DENIED, HttpStatus.SC_UNAUTHORIZED, message);
        }
    }

    /**
     * UnsupportedGrantType.
     */
    public static class UnsupportedGrantType extends AuthPluginException {
        /**
         * The authorization grant type is not supported by the authorization server.
         * @param message Response message
         */
        public UnsupportedGrantType(String message) {
            super(OAuth2Helper.Error.UNSUPPORTED_GRANT_TYPE, HttpStatus.SC_BAD_REQUEST, message);
        }
    }

    /**
     * UnsupportedResponseType.
     */
    public static class UnsupportedResponseType extends AuthPluginException {
        /**
         * The authorization response_type is not supported by the authorization server.
         * @param message Response message
         */
        public UnsupportedResponseType(String message) {
            super(OAuth2Helper.Error.UNSUPPORTED_RESPONSE_TYPE, HttpStatus.SC_BAD_REQUEST, message);
        }
    }

    /**
     * InvalidScope.
     */
    public static class InvalidScope extends AuthPluginException {
        /**
         * The requested scope is invalid, unknown, malformed, or
         * exceeds the scope granted by the resource owner.
         * @param message Response message
         */
        public InvalidScope(String message) {
            super(OAuth2Helper.Error.INVALID_SCOPE, HttpStatus.SC_BAD_REQUEST, message);
        }
    }

    /**
     * ServerError.
     */
    public static class ServerError extends AuthPluginException {
        /**
         * server_error.
         * @param message Response message
         */
        public ServerError(String message) {
            super(OAuth2Helper.Error.SERVER_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR, message);
        }
    }

    /**
     * TemporarilyUnavailable.
     */
    public static class TemporarilyUnavailable extends AuthPluginException {
        /**
         * temporarily_unavailable.
         * @param message Response message
         */
        public TemporarilyUnavailable(String message) {
            super(OAuth2Helper.Error.TEMPORARILY_UNAVAILABLE, HttpStatus.SC_SERVICE_UNAVAILABLE, message);
        }
    }
}
