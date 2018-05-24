
/**
 * Created by zpli on 2/7/17.
 */
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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.sdchain.model.BalanceCollection;
import io.sdchain.model.EffectCollection;
import io.sdchain.model.MemoCollection;
import io.sdchain.model.OrderCollection;
import io.sdchain.model.PaymentCollection;
import io.sdchain.model.TransactionCollection;
import io.sdchain.net.json.BalanceCollectionDeserializer;
import io.sdchain.net.json.EffectCollectionDeserializer;
import io.sdchain.net.json.MemoCollectionDeserializer;
import io.sdchain.net.json.OrderCollectionDeserializer;
import io.sdchain.net.json.PaymentCollectionDeserializer;
import io.sdchain.net.json.TransactionCollectionDeserializer;

/**
 * @author zp li.
 * @version 1.0
 *
 * Extends the abstract class when you need request anything from sdchain
 * Contains the basic info for servers
 */
public class ServerClass {

    /**
     * Gson object use to transform json string to SDChain object
     * Added Memo
     *            discard the
     *            .registerTypeAdapter(PaymentChoiceCollection.class, new PaymentChoiceCollectionDeserializer())
     *                        .registerTypeAdapter(PaymentCollection.class, new PaymentCollectionDeserializer())
     * canceled
     * .registerTypeAdapter(SettingsCollection.class, new SettingsCollectionDeserializer())
     */
    public static final Gson GSON = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapter(EffectCollection.class, new EffectCollectionDeserializer())
        .registerTypeAdapter(BalanceCollection.class, new BalanceCollectionDeserializer())
        .registerTypeAdapter(OrderCollection.class, new OrderCollectionDeserializer())
        .registerTypeAdapter(TransactionCollection.class, new TransactionCollectionDeserializer())
        .registerTypeAdapter(MemoCollection.class, new MemoCollectionDeserializer())
        .registerTypeAdapter(PaymentCollection.class, new PaymentCollectionDeserializer())
        .create();
}
