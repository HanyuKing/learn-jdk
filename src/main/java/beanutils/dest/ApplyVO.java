package beanutils.dest;

import java.io.Serializable;
import java.util.Date;

public class ApplyVO implements Serializable {
    protected Long id;
    protected Integer type;
    protected Integer venderId;
    protected Integer shopId;
    protected String shopName;
    protected Long activityId;
    protected String activityName;
    protected Long columnId;
    protected Long batchId;
    protected Long areaId;
    protected Long themeId;
    protected Long placeId;
    protected String columnName;
    protected String themeName;
    protected Integer applyStatus;
    protected Integer recommanded;
    protected Integer selectStatus;
    protected String erpPin;
    protected Integer seq;
    protected Long pid;
    protected Integer status;
    protected Date created;
    protected Date modified;
    protected String remark;
    protected Integer taskSeq;
    protected Long taskId;

    public ApplyVO() {
    }

    public ApplyVO(Long id) {
        this.id = id;
    }

    public ApplyVO(Long batchId, Long columnId, Long placeId) {
        this.batchId = batchId;
        this.columnId = columnId;
        this.placeId = placeId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getVenderId() {
        return this.venderId;
    }

    public void setVenderId(Integer venderId) {
        this.venderId = venderId;
    }

    public Integer getShopId() {
        return this.shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return this.shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getColumnId() {
        return this.columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Long getBatchId() {
        return this.batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getAreaId() {
        return this.areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getThemeId() {
        return this.themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getPlaceId() {
        return this.placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getThemeName() {
        return this.themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public Integer getApplyStatus() {
        return this.applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getRecommanded() {
        return this.recommanded;
    }

    public void setRecommanded(Integer recommanded) {
        this.recommanded = recommanded;
    }

    public Integer getSelectStatus() {
        return this.selectStatus;
    }

    public void setSelectStatus(Integer selectStatus) {
        this.selectStatus = selectStatus;
    }

    public String getErpPin() {
        return this.erpPin;
    }

    public void setErpPin(String erpPin) {
        this.erpPin = erpPin;
    }

    public Integer getSeq() {
        return this.seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Long getPid() {
        return this.pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return this.modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTaskSeq() {
        return this.taskSeq;
    }

    public void setTaskSeq(Integer taskSeq) {
        this.taskSeq = taskSeq;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
