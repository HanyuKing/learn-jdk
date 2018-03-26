package beanutils.dest;

import java.util.Date;
import java.util.List;

public class ApplyWare extends ApplyVO {
    private Long minIdUnInclude;
    private String orderBy = " ID DESC ";
    private String name;
    private Long applyId;
    private Long wareId;
    private Long skuId;
    private String promoPrice;
    private long stockNum;
    private Long promoRfId;
    private Long erpRfId;
    private String imgRui;
    private String lockKey;
    private String extInfo;
    private Integer promoType;
    private List<Long> skuIdList;
    private Integer promoCreateStatus;
    private Date batchTime;
    private Integer payStatus;
    private Integer showArea;
    private Double score;
    private Integer checkMode;
    private String scoreDesc;
    private Integer plus;
    private String plusReason;

    public ApplyWare() {
    }

    public ApplyWare(Long id) {
        super(id);
    }

    public ApplyWare(Long batchId, Long columnId, Long placeId) {
        super(batchId, columnId, placeId);
    }

    public Long getMinIdUnInclude() {
        return this.minIdUnInclude;
    }

    public void setMinIdUnInclude(Long minIdUnInclude) {
        this.minIdUnInclude = minIdUnInclude;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getApplyId() {
        return this.applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getWareId() {
        return this.wareId;
    }

    public void setWareId(Long wareId) {
        this.wareId = wareId;
    }

    public Long getSkuId() {
        return this.skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getPromoPrice() {
        return this.promoPrice;
    }

    public void setPromoPrice(String promoPrice) {
        this.promoPrice = promoPrice;
    }

    public long getStockNum() {
        return this.stockNum;
    }

    public void setStockNum(long stockNum) {
        this.stockNum = stockNum;
    }

    public Long getPromoRfId() {
        return this.promoRfId;
    }

    public void setPromoRfId(Long promoRfId) {
        this.promoRfId = promoRfId;
    }

    public Long getErpRfId() {
        return this.erpRfId;
    }

    public void setErpRfId(Long erpRfId) {
        this.erpRfId = erpRfId;
    }

    public String getImgRui() {
        return this.imgRui;
    }

    public void setImgRui(String imgRui) {
        this.imgRui = imgRui;
    }

    public String getLockKey() {
        return this.lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public String getExtInfo() {
        return this.extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public List<Long> getSkuIdList() {
        return this.skuIdList;
    }

    public void setSkuIdList(List<Long> skuIdList) {
        this.skuIdList = skuIdList;
    }

    public Integer getPromoType() {
        return this.promoType;
    }

    public void setPromoType(Integer promoType) {
        this.promoType = promoType;
    }

    public Integer getPromoCreateStatus() {
        return this.promoCreateStatus;
    }

    public void setPromoCreateStatus(Integer promoCreateStatus) {
        this.promoCreateStatus = promoCreateStatus;
    }

    public Date getBatchTime() {
        return this.batchTime;
    }

    public void setBatchTime(Date batchTime) {
        this.batchTime = batchTime;
    }

    public Integer getPayStatus() {
        return this.payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getShowArea() {
        return this.showArea;
    }

    public void setShowArea(Integer showArea) {
        this.showArea = showArea;
    }

    public Double getScore() {
        return this.score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getCheckMode() {
        return this.checkMode;
    }

    public void setCheckMode(Integer checkMode) {
        this.checkMode = checkMode;
    }

    public String getScoreDesc() {
        return this.scoreDesc;
    }

    public void setScoreDesc(String scoreDesc) {
        this.scoreDesc = scoreDesc;
    }

    public Integer getPlus() {
        return this.plus;
    }

    public void setPlus(Integer plus) {
        this.plus = plus;
    }

    public String getPlusReason() {
        return this.plusReason;
    }

    public void setPlusReason(String plusReason) {
        this.plusReason = plusReason;
    }
}
