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

import com.amazonaws.services.sqs.model.Message;
import com.streamsets.pipeline.lib.AWSSQSUtil;
import com.streamsets.pipeline.lib.Errors;
import com.streamsets.pipeline.api.BatchMaker;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.base.BaseSource;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This source is an example and does not actually read from anywhere.
 * It does however, generate generate a simple record with one field.
 */
public abstract class SampleSource extends BaseSource {

    /**
     * Gives access to the UI configuration of the stage provided by the {@link SampleDSource} class.
     */
    public abstract String getEndPoint();
    public abstract String getQueueName();
    public abstract String getAccessKey();
    public abstract String getSecreteKey();
    public abstract Boolean getDeleteFlag();

    @Override
    protected List<ConfigIssue> init() {
        // Validate configuration values and open any required resources.
        List<ConfigIssue> issues = super.init();

        if (getEndPoint().isEmpty() || getQueueName().isEmpty() || getAccessKey().isEmpty() || getSecreteKey().isEmpty()) {
            issues.add(
                    getContext().createConfigIssue(
                            Groups.SQS.name(), "config", Errors.SAMPLE_00, "Povide required parameters.."
                    )
            );
        }

        // If issues is not empty, the UI will inform the user of each configuration issue in the list.
        return issues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        // Clean up any open resources.
        super.destroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String produce(String lastSourceOffset, int maxBatchSize, BatchMaker batchMaker) throws StageException {
        // Offsets can vary depending on the data source. Here we use an integer as an example only.
        long nextSourceOffset = 0;
        if (lastSourceOffset != null) {
            nextSourceOffset = Long.parseLong(lastSourceOffset);
        }

        int numRecords = 0;

        // TODO: As the developer, implement your logic that reads from a data source in this method.

        // Create records and add to batch. Records must have a string id. This can include the source offset
        // or other metadata to help uniquely identify the record itself.

        AWSSQSUtil awssqsUtil = new AWSSQSUtil(getAccessKey(),getSecreteKey(),getQueueName(),getEndPoint());

        String queuName = awssqsUtil.getQueueName();
        String queueUrl = awssqsUtil.getQueueUrl(queuName);

        //maximum number of meesage that can be retrieve in one request
        int maxMessagCount = 10;

            List<Message> messages = awssqsUtil.getMessagesFromQueue(queueUrl,maxMessagCount);
            for (Message message : messages) {
                Record record = getContext().createRecord("messageId::" + message.getMessageId());
                Map<String, Field> map = new HashMap<>();
                map.put("receipt_handle", Field.create(message.getReceiptHandle()));
                map.put("md5_of_body", Field.create(message.getMD5OfBody()));
                map.put("body", Field.create(message.getBody()));

                JSONObject attributeJson = new JSONObject();

                for (Map.Entry<String, String> entry : message.getAttributes().entrySet()) {
                    attributeJson.put(entry.getKey(), entry.getValue());
                }

                map.put("attribute_list", Field.create(attributeJson.toString()));

                record.set(Field.create(map));
                batchMaker.addRecord(record);
                ++nextSourceOffset;
                ++numRecords;
                if(getDeleteFlag()){
                    awssqsUtil.deleteMessageFromQueue(queueUrl,message);
                }
            }



        return String.valueOf(nextSourceOffset);
    }
}

