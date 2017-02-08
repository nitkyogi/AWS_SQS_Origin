/**
 * Copyright 2015 StreamSets Inc.
 *
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.origin.sqs;

import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.ExecutionMode;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.StageDef;

@StageDef(
    version = 1,
    label = "SQS Origin",
    description = "",
    icon = "default.png",
    execution = ExecutionMode.STANDALONE,
    recordsByRef = true,
    onlineHelpRefUrl = ""
)
@ConfigGroups(value = Groups.class)
@GenerateResourceBundle
public class SampleDSource extends SampleSource {

  @ConfigDef(
          required = true,
          type = ConfigDef.Type.STRING,
          defaultValue = "",
          label = "Access Key",
          displayPosition = 10,
          group = "SQS"
  )
  public String access_key;

  @ConfigDef(
          required = true,
          type = ConfigDef.Type.STRING,
          defaultValue = "",
          label = "Secrete Key",
          displayPosition = 10,
          group = "SQS"
  )
  public String secrete_key;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      defaultValue = "",
      label = "Name",
      displayPosition = 10,
      group = "SQS"
  )
  public String queue_name;

  @ConfigDef(
          required = true,
          type = ConfigDef.Type.STRING,
          defaultValue = "",
          label = "End Point",
          displayPosition = 10,
          group = "SQS"
  )
  public String end_point;

  @ConfigDef(
          required = true,
          type = ConfigDef.Type.BOOLEAN,
          defaultValue = "",
          label = "Delete Message",
          displayPosition = 10,
          group = "SQS"
  )
  public Boolean delete_flag;


  /** {@inheritDoc} */
  @Override
  public String getEndPoint() {
    return end_point;
  }

  /** {@inheritDoc} */
  @Override
  public String getQueueName() {
    return queue_name;
  }


  /** {@inheritDoc} */
  @Override
  public String getAccessKey() {
    return access_key;
  }

  /** {@inheritDoc} */
  @Override
  public String getSecreteKey() {
    return secrete_key;
  }

  /** {@inheritDoc} */
  @Override
  public Boolean getDeleteFlag() {
    return delete_flag;
  }
}
