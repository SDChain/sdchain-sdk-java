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
package io.sdchain.model;

public class Memo {
    private String memo_type;
    private String memo_data;

    public void setMemoType(String in_str) {
        memo_type = in_str;
    }
    public void setMemoData(String in_str) {
        memo_data = in_str;
    }

    public String getMemoType() {
        return memo_type;
    }
    public String getMemoData() {
        return memo_data;
    }
    @Override
    public String toString() {
        return "Memo [memo_type=" + memo_type + ", memo_data=" + memo_data + "]";
    }

}
