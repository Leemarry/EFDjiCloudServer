package com.efuav.sdk.mqtt.property;

import com.efuav.sdk.mqtt.CommonTopicResponse;

/**
 * 统一主题请求格式
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/15
 */
public class TopicPropertySetResponse<T> extends CommonTopicResponse<T> {

    @Override
    public String toString() {
        return "TopicPropertySetResponse{" +
                "tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

    public TopicPropertySetResponse() {
    }

    public String getTid() {
        return tid;
    }

    public TopicPropertySetResponse<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public TopicPropertySetResponse<T> setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public T getData() {
        return data;
    }

    public TopicPropertySetResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public TopicPropertySetResponse<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}