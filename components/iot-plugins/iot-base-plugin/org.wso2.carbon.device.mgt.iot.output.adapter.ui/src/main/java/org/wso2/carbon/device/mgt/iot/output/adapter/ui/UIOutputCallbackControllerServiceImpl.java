/*
 *
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.device.mgt.iot.output.adapter.ui;

import com.google.gson.JsonObject;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.device.mgt.iot.output.adapter.ui.internal.UIEventAdaptorServiceDataHolder;
import org.wso2.carbon.device.mgt.iot.output.adapter.ui.util.WebSocketSessionUtil;
import org.wso2.carbon.device.mgt.iot.output.adapter.ui.util.UIEventAdapterConstants;

import javax.websocket.Session;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Service implementation class which exposes to front end
 */
public class UIOutputCallbackControllerServiceImpl implements UIOutputCallbackControllerService {

    private ConcurrentHashMap<Integer, ConcurrentHashMap<String, CopyOnWriteArrayList<WebSocketSessionUtil>>>
            outputEventAdaptorSessionMap;

    public UIOutputCallbackControllerServiceImpl() {
        outputEventAdaptorSessionMap = new ConcurrentHashMap<>();
    }

    /**
     * Used to subscribe the session id and stream id for later web socket connectivity
     *
     * @param streamName - Stream name which user register to.
     * @param version    - Stream version which user uses.
     * @param session    - Session which user registered.
     */
    public void subscribeWebsocket(String streamName, String version, Session session) {

        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();

        if (version == null || " ".equals(version)) {
            version = UIEventAdapterConstants.ADAPTER_UI_DEFAULT_OUTPUT_STREAM_VERSION;
        }
        String streamId = streamName + UIEventAdapterConstants.ADAPTER_UI_COLON + version;
        ConcurrentHashMap<String, CopyOnWriteArrayList<WebSocketSessionUtil>> tenantSpecificAdaptorMap =
                outputEventAdaptorSessionMap.get(tenantId);
        if (tenantSpecificAdaptorMap == null) {
            tenantSpecificAdaptorMap = new ConcurrentHashMap<>();
            if (null != outputEventAdaptorSessionMap.putIfAbsent(tenantId, tenantSpecificAdaptorMap)) {
                tenantSpecificAdaptorMap = outputEventAdaptorSessionMap.get(tenantId);
            }
        }
        CopyOnWriteArrayList<WebSocketSessionUtil> adapterSpecificSessions = tenantSpecificAdaptorMap.get(streamId);
        if (adapterSpecificSessions == null) {
            adapterSpecificSessions = new CopyOnWriteArrayList<>();
            if (null != tenantSpecificAdaptorMap.putIfAbsent(streamId, adapterSpecificSessions)) {
                adapterSpecificSessions = tenantSpecificAdaptorMap.get(streamId);
            }
        }

        WebSocketSessionUtil webSocketSessionUtil = new WebSocketSessionUtil(session);
        adapterSpecificSessions.add(webSocketSessionUtil);
    }

    /**
     * Used to return registered sessions per streamId
     *
     * @param tenantId - Tenant id of the user.
     * @param streamId - Stream name and version which user register to.
     * @return the sessions list.
     */
    public CopyOnWriteArrayList<WebSocketSessionUtil> getSessions(int tenantId, String streamId) {
        ConcurrentHashMap<String, CopyOnWriteArrayList<WebSocketSessionUtil>> tenantSpecificAdaptorMap
                = outputEventAdaptorSessionMap.get(tenantId);
        if (tenantSpecificAdaptorMap != null) {
            return tenantSpecificAdaptorMap.get(streamId);
        }
        return null;
    }

    /**
     * Used to return events per streamId
     *
     * @param tenanId    - Tenant id of the user.
     * @param streamName - Stream name which user register to.
     * @param version    - Stream version which user uses.
     * @return the events list.
     */
    public LinkedBlockingDeque<Object> getEvents(int tenanId, String streamName, String version) {
        ConcurrentHashMap<String, LinkedBlockingDeque<Object>> tenantSpecificStreamMap =
                UIEventAdaptorServiceDataHolder.getTenantSpecificStreamEventMap().get(tenanId);
        if (tenantSpecificStreamMap != null) {
            String streamId = streamName + UIEventAdapterConstants.ADAPTER_UI_COLON + version;
            return tenantSpecificStreamMap.get(streamId);
        }
        return null;
    }

    /**
     * Used to return events per streamId
     *
     * @param streamName - Stream name which user register to.
     * @param version    - Stream version which user uses.
     * @param session    - Session which user subscribed to.
     */
    public void unsubscribeWebsocket(String streamName, String version, Session session) {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        if (version == null || " ".equals(version)) {
            version = UIEventAdapterConstants.ADAPTER_UI_DEFAULT_OUTPUT_STREAM_VERSION;
        }
        String id = streamName + UIEventAdapterConstants.ADAPTER_UI_COLON + version;
        ConcurrentHashMap<String, CopyOnWriteArrayList<WebSocketSessionUtil>> tenantSpecificAdaptorMap
                = outputEventAdaptorSessionMap.get(tenantId);
        if (tenantSpecificAdaptorMap != null) {
            CopyOnWriteArrayList<WebSocketSessionUtil> adapterSpecificSessions = tenantSpecificAdaptorMap.get(id);
            if (adapterSpecificSessions != null) {
                WebSocketSessionUtil sessionToRemove = null;
                Iterator<WebSocketSessionUtil> iterator = adapterSpecificSessions.iterator();
                while (iterator.hasNext()) {
                    WebSocketSessionUtil webSocketSessionUtil = iterator.next();
                    if (session.getId().equals(webSocketSessionUtil.getSession().getId())) {
                        sessionToRemove = webSocketSessionUtil;
                        break;
                    }
                }
                if (sessionToRemove != null) {
                    adapterSpecificSessions.remove(sessionToRemove);
                }
            }
        }
    }

}
