package other;

import java.io.Serializable;

public class CouponMessage implements Serializable {
    private String batchKey;

    private Long venderId;

    public CouponMessage(String batchKey, Long venderId) {
        this.batchKey = batchKey;
        this.venderId = venderId;
    }

    public String getBatchKey() {
        return batchKey;
    }

    public void setBatchKey(String batchKey) {
        this.batchKey = batchKey;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }
}