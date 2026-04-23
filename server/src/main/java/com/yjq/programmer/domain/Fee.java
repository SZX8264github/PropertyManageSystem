package com.yjq.programmer.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Fee {
    private String id;

    private String userId;

    private String content;

    private Date createTime;

    private Integer state;

    private Date deadTime;

    private Date payTime;

    private BigDecimal price;

    private BigDecimal deadPrice;

    private String taskId;

    private BigDecimal addPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDeadPrice() {
        return deadPrice;
    }

    public void setDeadPrice(BigDecimal deadPrice) {
        this.deadPrice = deadPrice;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public BigDecimal getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(BigDecimal addPrice) {
        this.addPrice = addPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", content=").append(content);
        sb.append(", createTime=").append(createTime);
        sb.append(", state=").append(state);
        sb.append(", deadTime=").append(deadTime);
        sb.append(", payTime=").append(payTime);
        sb.append(", price=").append(price);
        sb.append(", deadPrice=").append(deadPrice);
        sb.append(", taskId=").append(taskId);
        sb.append(", addPrice=").append(addPrice);
        sb.append("]");
        return sb.toString();
    }
}