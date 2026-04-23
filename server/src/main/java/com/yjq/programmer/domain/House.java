package com.yjq.programmer.domain;

import java.math.BigDecimal;

public class House {
    private String id;

    private String buildingId;

    private String card;

    private BigDecimal area;

    private String userId;

    private String content;

    private Integer floor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
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

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", buildingId=").append(buildingId);
        sb.append(", card=").append(card);
        sb.append(", area=").append(area);
        sb.append(", userId=").append(userId);
        sb.append(", content=").append(content);
        sb.append(", floor=").append(floor);
        sb.append("]");
        return sb.toString();
    }
}