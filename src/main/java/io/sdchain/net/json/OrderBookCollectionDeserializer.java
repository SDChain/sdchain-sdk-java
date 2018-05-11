/*
 * Copyright SDChain Alliance
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

package io.sdchain.net.json;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import io.sdchain.model.OrderBook;
import io.sdchain.model.OrderBookCollection;
/**
 * 
 * ClassName: OrderBookCollectionDeserializer <br/>
 * the <em>OrderBook</em> utility of json deserializer
 * @see OrderBook
 * @author Sean
 */
public class OrderBookCollectionDeserializer implements JsonDeserializer<OrderBookCollection> {
	
	/**
	 * @param json the json element
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
    public OrderBookCollection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        if (json.isJsonArray()) {
            Type orderBookListType = new TypeToken<List<OrderBook>>() {
            }.getType();
            List<OrderBook> orderBook = gson.fromJson(json, orderBookListType);
            OrderBookCollection collection = new OrderBookCollection();
            collection.setData(orderBook);
            return collection;
        }
        return gson.fromJson(json, typeOfT);
    }
}
