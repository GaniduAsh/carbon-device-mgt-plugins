/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.mdm.mobileservices.windows.services.discovery.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiscoveryRequest")
@SuppressWarnings("unused")
@ApiModel(value = "DiscoveryRequest",
        description = "This class carries all information related to Discovery request.")
public class DiscoveryRequest implements Serializable {

    @XmlElement(name = "EmailAddress", required = true)
    @ApiModelProperty(name = "emailId", value = "Email ID.", required = true)
    private String emailId;

    @XmlElement(name = "RequestVersion")
    @ApiModelProperty(name = "version", value = "Request Version.", required = true)
    private String version;

    @XmlElement(name = "DeviceType")
    @ApiModelProperty(name = "deviceType", value = "Type of the Device.", required = true)
    private String deviceType;

    public String getEmailId() {
        return emailId;
    }

    public String getVersion() {
        return version;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}