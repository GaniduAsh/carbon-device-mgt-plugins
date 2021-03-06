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

package org.wso2.carbon.mdm.mobileservices.windows.operations;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.carbon.mdm.mobileservices.windows.operations.util.Constants;

/**
 * Challenge data pass through the device and Device Management server for the security purpose.
 */
@ApiModel(value = "ChallengeTag",
        description = "This class carries all information related to install application")
public class ChallengeTag {
    @ApiModelProperty(name = "meta", value = "Syncml MetaTag", required = true)
    MetaTag meta;

    public MetaTag getMeta() {
        return meta;
    }

    public void setMeta(MetaTag meta) {
        this.meta = meta;
    }

    public void buildChallElement(Document doc, Element rootElement) {
        Element chal = doc.createElement(Constants.CHALLENGE);
        rootElement.appendChild(chal);
        if (getMeta() != null) {
            getMeta().buildMetaElement(doc, chal);
        }
    }
}
