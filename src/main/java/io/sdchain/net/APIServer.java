/*
 * Copyright www.sdchain.io Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.sdchain.net;

import static io.sdchain.net.APIServer.RequestMethod.GET;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.sdchain.SDChainMessage;
import io.sdchain.exception.APIConnectionException;
import io.sdchain.exception.APIException;
import io.sdchain.exception.AuthenticationException;
import io.sdchain.exception.ChannelException;
import io.sdchain.exception.FailedException;
import io.sdchain.exception.InvalidRequestException;
import io.sdchain.util.Utility;

/**
 * Extends the server class when you need request anything from sdchain
 * the class only pass the parameters and data to the server
 * and return the data to the ,
 * not handling the response anymore.
 *
 */
public class APIServer extends ServerClass {
    /**
     * URLEncoder charset
     */
    public static final String CHARSET = "UTF-8";

    private static APIServer INSTANCE = null;
    static {
        INSTANCE = new APIServer();
    }

    private String versionURL;
    private String prefix = "prefix";

    private APIServer() {
        super("");
    }

    private APIServer(String in_url, String version_url) {
        // call the parent constructor to create the class
        super(in_url);
        this.versionURL = version_url;

        INSTANCE.setServerURL(in_url);
        INSTANCE.setVersionURL(version_url);
    }

    public static void init(String in_url, String version_url) {
        new APIServer(in_url, version_url);
    }

    @Override
    public void setServerURL(String in_url) {
        super.setServerURL(in_url);

        INSTANCE.serverURL = in_url;
    }

    public void setVersionURL(String version_url) {
        this.versionURL = version_url;

        INSTANCE.versionURL = version_url;
    }

    public String getVersionURL() {
        return versionURL;
    }

    // Set the prefix to return
    public void setPrefix(String in_prefix) {
        this.prefix = in_prefix;
    };

    // Return a client resouirce id for payment
    public String getClientId() {
        return prefix + Long.toString(System.currentTimeMillis());
    }

    /**
     * Http request method:
     * Get, Post and Delete
     */
    public enum RequestMethod {
        GET, POST, DELETE, POST_FORM
    }

    /**
     * Based on class name to built the string needed in URL
     *
     * @param clazz
     * @return clazz
     */
    private static String className(Class<?> clazz) {
        String className = clazz.getSimpleName().toLowerCase().replace("$", " ");
        if (className.equals("orderbook")) {
            className = "order_book";
        } else if (className.equals("settings")) {
            className = "settings";
        } else if (className.equals("relation")) {
            className = "trustlines";
        } else {
            className = className.concat("s");

        }
        return className;
    }

    /**
     * Built base URL
     *
     * @return class URL
     */
    protected static String classURL() {
        // return INSTANCE.getServerURL().concat(INSTANCE.getVersionURL());
        // check if the url is null
        // try{
        if (INSTANCE.getServerURL() != null) {
            // throw new APIConnectionException(
            // "Empty URL");

            // give out a exception if not

            return String.format(
                "%s/%s/accounts",
                INSTANCE.getServerURL(),
                INSTANCE.getVersionURL());
        } else
            return String.format("Empty url");
    }

    /**
     * @param param
     * @return formed URL
     */
    public static String formatURL(String param) {
        return String.format(
            "%s/%s/%s",
            INSTANCE.getServerURL(),
            INSTANCE.getVersionURL(),
            param);
    }

    /**
     * @param clazz object class
     * @param address sdchain address
     * @param param the param
     * @return formed URL
     * @throws InvalidRequestException
     */
    public static String formatURL(
        Class<?> clazz,
        String address,
        String param) throws InvalidRequestException {

        return String.format(
            "%s/%s/%s%s",
            classURL(),
            className(clazz),
            address,
            param);
    }

    public static String formatURL(
        Class<?> clazz,
        String address) throws InvalidRequestException {
        return String.format(
            "%s/%s/%s",
            classURL(),
            className(clazz), address);
    }

    public static String formatURL(
        Class<?> clazz,
        String address,
        String secret,
        String params,
        Boolean shouldAppendSignature) throws InvalidRequestException {

        if (shouldAppendSignature) {
            String signature = Utility.buildSignString(address, secret);
            return formatURL(clazz, address, signature + params);
        } else {
            return formatURL(clazz, address, params);
        }
    }

    /**
     * Should append signature string.
     *
     * @param requestMethod the request method
     * @param apiVersion    the api version
     * @return the string
     */
    public static Boolean shouldAppendSignature(
        RequestMethod requestMethod,
        String apiVersion) {
        if (requestMethod == GET && apiVersion.toLowerCase().equals("v1")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Error class
     */
    private static class Error {
        private String error_type;
        private String error;
        private String message;
        private String r1_code;
        private String r3_error_msg;
        private String ra_error_msg;
        private String ri_error_msg;

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (Utility.isNotEmpty(error_type)) {
                sb.append("Error type: ");
                sb.append(error_type);
                sb.append("\n");
            }
            if (Utility.isNotEmpty(error) || Utility.isNotEmpty(error_type)) {
                sb.append("\t ");
                sb.append(SDChainMessage.ERROR_MESSAGE);
                if (SDChainMessage.ACCOUNT_NOT_FOUND.equals(message)) {
                    sb.append(SDChainMessage.INACTIVATED_ACCOUNT);
                }
                sb.append(message);
                sb.append("\n");
            }
            if (Utility.isNotEmpty(r1_code)) {
                sb.append("\t ");
                sb.append(SDChainMessage.ERROR_CODE);
                sb.append(r1_code);
                sb.append("\n");
            }
            if (Utility.isNotEmpty(r3_error_msg)) {
                sb.append("\t ");
                sb.append(SDChainMessage.ERROR_MESSAGE);
                sb.append(r3_error_msg);
                sb.append("\n");
            }
            if (Utility.isNotEmpty(ra_error_msg)) {
                sb.append("\t ");
                sb.append(SDChainMessage.ERROR_MESSAGE);
                sb.append(ra_error_msg);
                sb.append("\n");
            }
            if (Utility.isNotEmpty(ri_error_msg)) {
                sb.append("\t ");
                sb.append(SDChainMessage.ERROR_MESSAGE);
                sb.append(ri_error_msg);
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    /**
     * @param method
     * @param url
     * @param query
     * @return SDChainResponse
     * @throws APIConnectionException
     */
    private static SDChainResponse makeRequest(
        RequestMethod method, String url, String query)
        throws APIConnectionException {
        HttpResponse<JsonNode> jsonResponse = null;
        Unirest.setTimeouts(30 * 1000, 80 * 1000);

        try {
            switch (method) {
                case GET :
                    jsonResponse = Unirest.get(url).asJson();
                    break;
                case POST :
                case POST_FORM :
                    jsonResponse = Unirest.post(url).header("Content-Type", "application/json").body(query).asJson();
                    break;
                case DELETE :
                    jsonResponse = Unirest.delete(url).header("Content-Type", "application/json").body(query).asJson();
                    break;
                default :
                    throw new APIConnectionException(
                        String.format(SDChainMessage.UNRECOGNIZED_HTTP_METHOD, method));
            }
            // trigger the request
            String rBody = jsonResponse.getBody().toString();
            Integer rCode = jsonResponse.getStatus();

            return new SDChainResponse(rCode, rBody);
        } catch (UnirestException e) {
            throw new APIConnectionException(
                String.format(
                    SDChainMessage.SERVER_ERROR, INSTANCE.getServerURL(), e.getMessage()),
                e);
        }
    }

    /**
     * Build class instance from jason format http response
     * @param method  the method of request
     * @param url     the request of url
     * @param params  the request of param
     * @param clazz   
     * @param <T>
     * @return class instance
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws FailedException
     */
    public static <T> T request(RequestMethod method, String url, String params, Class<T> clazz) throws AuthenticationException,
        InvalidRequestException, APIConnectionException, ChannelException, APIException, FailedException {

        SDChainResponse response;

        try {
            response = makeRequest(method, url, params);
        } catch (ClassCastException ce) {
            throw ce;
        }
        int rCode = response.getResponseCode();
        String rBody = response.getResponseBody();
        if (rCode < 200 || rCode >= 300) {
            handleAPIError(rBody, rCode, params);
        }

        return GSON.fromJson(rBody, clazz);
    }

    /**
     * Error handling
     *
     * @param rBody
     * @param rCode
     * @throws InvalidRequestException
     * @throws AuthenticationException
     * @throws APIException
     * @throws FailedException
     */
    private static void handleAPIError(String rBody, int rCode, String query)
        throws InvalidRequestException, AuthenticationException,
        APIException, ChannelException, FailedException {
        Error error;
        switch (rCode) {
            case 400 :
                error = GSON.fromJson(rBody,
                    Error.class);
                throw new InvalidRequestException(error.toString(), query, null);
            case 404 :
                error = GSON.fromJson(rBody,
                    Error.class);
                throw new InvalidRequestException(error.toString(), query, null);
            case 403 :
                error = GSON.fromJson(rBody,
                    Error.class);
                throw new ChannelException(error.toString(), query, null);
            case 401 :
                error = GSON.fromJson(rBody,
                    Error.class);
                throw new AuthenticationException(error.toString());
            case 500 :
                error = GSON.fromJson(rBody,
                    Error.class);
                throw new FailedException(error.toString());
            default :
                throw new APIException(rBody.toString(), null);
        }
    }
}
