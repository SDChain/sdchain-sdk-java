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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.sdchain.core.SDChainMessage;
import io.sdchain.exception.RequestException;
import io.sdchain.util.Utility;

/**
 * Extends the server class when you need request anything from sdchain
 * the class only pass the parameters and data to the server
 * and return the data to the ,
 * not handling the response anymore.
 *
 */
public class APIServer extends ServerClass {
    public static Map<String, Router> routes = new HashMap<String, Router>();
    public static final String API_GETBALANCE = "GETBALANCE";
    public static final String API_SUBMITPAYMENT = "SUBMITPAYMENT";
    public static final String API_GETPAYMENTLIST = "GETPAYMENTLIST";
    public static final String API_GETPAYMENTINFO = "GETPAYMENTINFO";
    public static final String API_GETTRANSACTIONLIST = "GETTRANSACTIONLIST";
    public static final String API_GETTRANSACTIONINFO = "GETTRANSACTIONINFO";
    public static final String API_GETORDERLIST = "GETORDERLIST";
    public static final String API_SUBMITORDER = "SUBMITORDER";
    public static final String API_DELETEORDER = "DELETEORDER";
    public static final String API_GETORDERINFO = "GETORDERINFO";
    public static final String API_GETTRANSACTIONFEE = "/transaction-fee";
    public static final String API_GETINFO = "/server";
    public static final String API_ISCONNECTED = "/server/connected";
    static {
        routes.put(API_GETBALANCE, new Router(RequestMethod.GET, "/accounts/balances/{:address}"));
        routes.put(API_SUBMITPAYMENT, new Router(RequestMethod.POST, "/accounts/payments/{:address}"));
        routes.put(API_GETPAYMENTLIST, new Router(RequestMethod.GET, "/accounts/payments/{:address}"));
        routes.put(API_GETPAYMENTINFO, new Router(RequestMethod.GET, "/accounts/payments/{:address}/{:hash}"));
        routes.put(API_GETTRANSACTIONLIST, new Router(RequestMethod.GET, "/accounts/transactions/{:address}"));
        routes.put(API_GETTRANSACTIONINFO, new Router(RequestMethod.GET, "/accounts/transactions/{:address}/{:hash}"));
        routes.put(API_GETORDERLIST, new Router(RequestMethod.GET, "/accounts/orders/{:address}"));
        routes.put(API_SUBMITORDER, new Router(RequestMethod.POST, "/accounts/orders/{:address}"));
        routes.put(API_DELETEORDER, new Router(RequestMethod.DELETE, "/accounts/orders/{:address}/{:sequence}"));
        routes.put(API_GETORDERINFO, new Router(RequestMethod.GET, "/accounts/orders/{:address}/{:hash}"));
        routes.put(API_GETTRANSACTIONFEE, new Router(RequestMethod.GET, "/transaction-fee"));
        routes.put(API_GETINFO, new Router(RequestMethod.GET, "/server"));
        routes.put(API_ISCONNECTED, new Router(RequestMethod.GET, "/server/connected"));
    }

    private static APIServer INSTANCE = null;
    static {
        INSTANCE = new APIServer();
    }

    private String serverUrl;
    private String version;

    private APIServer() {
    }

    /**
     * 服务信息初始化
     * @param serverUrl
     * @param version
     */
    public static void init(String serverUrl, String version) {
        INSTANCE.setServerUrl(serverUrl);
        INSTANCE.setVersion(version);
    }

    protected static String baseURL() {
        if (INSTANCE.getServerUrl() != null) {
            return String.format(
                "%s/%s",
                INSTANCE.getServerUrl(),
                INSTANCE.getVersion());
        } else {
            return String.format("Empty url");
        }
    }

    /**
     * 创建http请求
     * @param method
     * @param url
     * @param query
     * @return SDChainResponse
     * @throws APIConnectionException
     * @throws RequestException 
     */
    private static RequestResponse makeRequest(RequestMethod method, String url, String query) throws RequestException {
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
                    throw new RequestException(String.format(SDChainMessage.UNRECOGNIZED_HTTP_METHOD, method));
            }
            // trigger the request
            String rBody = jsonResponse.getBody().toString();
            Integer rCode = jsonResponse.getStatus();

            return new RequestResponse(rCode, rBody);
        } catch (UnirestException e) {
            throw new RequestException(String.format(SDChainMessage.SERVER_ERROR, INSTANCE.getServerUrl(), e.getMessage()), e);
        }
    }

    /**
     * 发起对服务器的api请求
     * @param api 调用的API
     * @param pathVars 路径参数
     * @param urlParams url中的请求参数
     * @param body 请求体(post put delete等有效)
     * @param clazz
     * @return
     * @throws RequestException
     */
    public static <T> T request(String api, Map<String, String> pathVars, Map<String, String> urlParams, String body, Class<T> clazz) throws RequestException {
        Router router = routes.get(api);
        if (router == null) {
            throw new RequestException(api + " route not found!");
        }
        String url = buildRequestUrl(router.getUrl(), pathVars, urlParams);
        RequestResponse response = null;
        try {
            response = makeRequest(router.getMethod(), url, body);
        } catch (ClassCastException ce) {
            throw new RequestException(ce);
        }
        int rCode = response.getResponseCode();
        String rBody = response.getResponseBody();
        System.out.println(router.getMethod() + " : " + url + "\n" + "params : " + body + "\n" + "rBody : " + rBody);
        if (rCode < 200 || rCode >= 300) {
            handleAPIError(rBody, rCode, body);
        }
        return GSON.fromJson(rBody, clazz);
    }

    private static String buildRequestUrl(String url, Map<String, String> pathVars, Map<String, String> urlParams) {
        String subUrl = url;
        if (pathVars != null) {
            Set<Entry<String, String>> varSet = pathVars.entrySet();
            for (Entry<String, String> var : varSet) {
                String key = var.getKey();
                String value = var.getValue();
                if (Utility.isNotEmpty(key) && Utility.isNotEmpty(value)) {
                    subUrl = subUrl.replaceAll("\\{:" + var.getKey() + "\\}", var.getValue());
                }
            }
        }
        String requestParams = buildUrlParams(urlParams);
        return String.format(
            "%s%s%s",
            baseURL(),
            subUrl,
            requestParams);
    }

    private static String buildUrlParams(Map<String, String> urlParams) {
        if (urlParams == null) {
            return "";
        }
        StringBuilder params = new StringBuilder();
        Set<Entry<String, String>> paramSet = urlParams.entrySet();
        for (Entry<String, String> entry : paramSet) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (Utility.isNotEmpty(key) && Utility.isNotEmpty(value)) {
                if (Utility.isNotEmpty(params.toString())) {
                    params.append("&");
                } else {
                    params.append("?");
                }
                params.append(key + "=" + value);
            }
        }
        return params.toString();
    }

    /**
     * Error handling
     * @param rBody
     * @param rCode
     * @throws RequestException 
     */
    private static void handleAPIError(String rBody, int rCode, String query) throws RequestException {
        throw new RequestException(rCode + "\n" + rBody + "\n " + query);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
