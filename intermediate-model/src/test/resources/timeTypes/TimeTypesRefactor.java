/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package org.apache.kafka.connect.runtime.distributed;

import org.apache.kafka.clients.consumer.internals.AbstractCoordinator;
import org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient;
import org.apache.kafka.common.metrics.Measurable;
import org.apache.kafka.common.metrics.MetricConfig;
import org.apache.kafka.common.metrics.Metrics;
import org.apache.kafka.common.requests.JoinGroupRequest;
import org.apache.kafka.common.requests.JoinGroupRequest.ProtocolMetadata;
import org.apache.kafka.common.utils.CircularIterator;
import org.apache.kafka.common.utils.Time;
import org.apache.kafka.connect.storage.ConfigBackingStore;
import org.apache.kafka.connect.util.ConnectorTaskId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Year;
/**
 * This class manages the coordination process with the Kafka group coordinator on the broker for managing assignments
 * to workers.
 */
public final class WorkerCoordinator extends AbstractCoordinator implements Closeable {

    public void poll(long timeout) {
        // poll for io until the timeout expires
        long now = System.currentTimeMillis();
        long deadline = now + timeout;

        while (now - deadline <= 0) {
            if (coordinatorUnknown()) {
                ensureCoordinatorReady();
                now = System.currentTimeMillis();
            }
            Thread.sleep(deadline);
            if (needRejoin()) {
                ensureActiveGroup();
                now = System.currentTimeMillis();
            }

            pollHeartbeat(now);

            // Note that because the network client is shared with the background heartbeat thread,
            // we do not want to block in poll longer than the time to the next heartbeat.
            long remaining = Math.max(0, deadline - now);
            client.poll(Math.min(remaining, timeToNextHeartbeat(now)));
            now = System.currentTimeMillis();
            Year y = Year.of(2014);
            System.out.println(y.length());

        }
    }

    public void poll(long timeout, long now) {
        // poll for io until the timeout expires
        //long now = System.currentTimeMillis();
        long deadline = now + timeout;

        while (now - deadline <= 0) {
            if (coordinatorUnknown()) {
                ensureCoordinatorReady();
                now = System.currentTimeMillis();
            }
            Thread.sleep(deadline);
            if (needRejoin()) {
                ensureActiveGroup();
                now = System.currentTimeMillis();
            }

            pollHeartbeat(now);

            // Note that because the network client is shared with the background heartbeat thread,
            // we do not want to block in poll longer than the time to the next heartbeat.
            long remaining = Math.max(0, deadline - now);
            client.poll(Math.min(remaining, timeToNextHeartbeat(now)));
            now = System.currentTimeMillis();
            Year y = Year.of(2014);
            System.out.println(y.length());

        }
    }

    public void dummyError1(){
        this.poll(System.currentTimeMillis());
    }

    public void dummyError2(){
        long now = System.currentTimeMillis();
        this.poll(now);
    }

    public void dummyCorrect(){
        long now = System.currentTimeMillis();
        long now_2 = System.currentTimeMillis();
        this.poll(now_2-now);
    }

    public void dummyDoubleError1(){
        long ts = System.currentTimeMillis();
        long dur = ts - ts;
        this.poll(ts, ts);
    }
    public void dummyDoubleError2(){
        long ts = System.currentTimeMillis();
        long dur = ts - ts;
        this.poll(ts, dur);
    }
    public void dummyDoubleError3(){
        long ts = System.currentTimeMillis();
        long dur = ts - ts;
        this.poll(dur, dur);
    }
    public void correctDouble(){
        long ts = System.currentTimeMillis();
        long dur = ts - ts;
        this.poll(dur, ts);
    }

    public void compareWithZero(){
        long now = System.currentTimeMillis();
        if(now > 0){
            System.out.println("This is not an error");
        }
    }

    public void compareTimestampWrongly(){
        long now = System.currentTimeMillis();
        long later = System.currentTimeMillis();
        if(now - later > 0){
            System.out.println("This is not an error");
        }
    }

    public void compareTimestampCorrectly(){
        long now = System.currentTimeMillis();
        long later = System.currentTimeMillis();
        if(now - later > 0){
            System.out.println("This is not an error");
        }
    }

}
