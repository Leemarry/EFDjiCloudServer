package com.efuav.sdk.mqtt;

/**
 * 统一的主题请求格式。
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/10
 */
public class CommonTopicRequest<T> {

    /**
     * 发送命令，并且通过消息中的tid和bid字段来匹配响应，
     * 回复应保持tid和出价不变。
     */
    protected String tid;

    protected String bid;

    protected Long timestamp;

    protected T data;

    public CommonTopicRequest() {
    }

    @Override
    public String toString() {
        return "CommonTopicRequest{" +
                "tid='" + tid + '\'' +
                ", bid='" + bid + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }

    public String getTid() {
        return tid;
    }

    public CommonTopicRequest<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public CommonTopicRequest<T> setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public CommonTopicRequest<T> setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getData() {
        return data;
    }

    public CommonTopicRequest<T> setData(T data) {
        this.data = data;
        return this;
    }
}