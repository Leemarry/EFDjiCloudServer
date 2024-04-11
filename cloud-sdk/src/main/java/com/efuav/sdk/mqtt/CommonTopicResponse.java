package com.efuav.sdk.mqtt;

/**
 * 统一主题响应格式
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/15
 */
public class CommonTopicResponse<T> {

    /**
     * 发送命令，并且通过消息中的tid和bid字段来匹配响应，
     * 回复应保持tid和出价不变。
     */
    protected String tid;

    protected String bid;

    protected T data;

    protected Long timestamp;

    @Override
    public String toString() {
        return "CommonTopicResponse{" +
                "tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

    public CommonTopicResponse() {
    }

    public String getTid() {
        return tid;
    }

    public CommonTopicResponse<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public CommonTopicResponse<T> setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public T getData() {
        return data;
    }

    public CommonTopicResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public CommonTopicResponse<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}