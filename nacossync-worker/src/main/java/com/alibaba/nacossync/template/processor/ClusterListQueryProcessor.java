/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacossync.template.processor;

import com.alibaba.nacossync.dao.ClusterAccessService;
import com.alibaba.nacossync.pojo.QueryCondition;
import com.alibaba.nacossync.pojo.model.ClusterDO;
import com.alibaba.nacossync.pojo.request.ClusterListQueryRequest;
import com.alibaba.nacossync.pojo.result.ClusterListQueryResult;
import com.alibaba.nacossync.pojo.view.ClusterModel;
import com.alibaba.nacossync.template.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author NacosSync
 * @version $Id: ClusterListQueryProcessor.java, v 0.1 2018-09-30 PM2:33 NacosSync Exp $$
 */
@Service
public class ClusterListQueryProcessor implements Processor<ClusterListQueryRequest, ClusterListQueryResult> {
    
    private final ClusterAccessService clusterAccessService;
    
    public ClusterListQueryProcessor(ClusterAccessService clusterAccessService) {
        this.clusterAccessService = clusterAccessService;
    }
    
    @Override
    public void process(ClusterListQueryRequest clusterListQueryRequest, ClusterListQueryResult clusterListQueryResult,
            Object... others) {
        
        Page<ClusterDO> clusterDOS;
        
        if (StringUtils.isNotBlank(clusterListQueryRequest.getClusterName())) {
            
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setServiceName(clusterListQueryRequest.getClusterName());
            clusterDOS = clusterAccessService.findPageCriteria(clusterListQueryRequest.getPageNum() - 1,
                    clusterListQueryRequest.getPageSize(), queryCondition);
            
        } else {
            
            clusterDOS = clusterAccessService.findPageNoCriteria(clusterListQueryRequest.getPageNum() - 1,
                    clusterListQueryRequest.getPageSize());
            
        }
        List<ClusterModel> clusterModels = clusterDOS.stream().map(ClusterModel::from).toList();
        clusterListQueryResult.setClusterModels(clusterModels);
        clusterListQueryResult.setTotalPage(clusterDOS.getTotalPages());
        clusterListQueryResult.setCurrentSize(clusterModels.size());
        clusterListQueryResult.setTotalSize(clusterDOS.getTotalElements());
        
    }
}
