/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
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
 *
 */
package com.huitian.oamanager.api;

public class ApiConstants {
    //    public static final String SERVICE_URL = "http://cy1798.imwork.net";    // 正式服务器地址
//    public static final String SERVICE_URL = "http://192.168.1.44:84";    // 程哥服务器地址
//    public static final String SERVICE_URL = "http://192.168.1.180:82";    // 小胖服务器地址
    public static final String SERVICE_URL = "http://61.152.218.34:555/Businesssystem"; // 服务器地址


    public static final String HUITIAN_OA_URL = SERVICE_URL + "/index.php/";

    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.HUITIAN_URL:
                host = HUITIAN_OA_URL;
                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
