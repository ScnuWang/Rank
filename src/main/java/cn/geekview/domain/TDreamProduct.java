package cn.geekview.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TDreamProduct implements Serializable {
	private static final long serialVersionUID = -7026490024614475763L;

	private Integer pkId;

	private Integer originalId;

	private Integer website;

	private String productName;

	private String productUrl;

	private BigDecimal growthMoney;

	private Date updateDate;

	private String moneyCurrency;

	public Integer getOriginalId() {
		return originalId;
	}

	public void setOriginalId(Integer originalId) {
		this.originalId = originalId;
	}

	public String getMoneyCurrency() {
		return moneyCurrency;
	}

	public void setMoneyCurrency(String moneyCurrency) {
		this.moneyCurrency = moneyCurrency;
	}

	public Integer getPkId() {
		return pkId;
	}

	public void setPkId(Integer pkId) {
		this.pkId = pkId;
	}

	public Integer getWebsite() {
		return website;
	}

	public void setWebsite(Integer website) {
		this.website = website;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public BigDecimal getGrowthMoney() {
		return growthMoney;
	}

	public void setGrowthMoney(BigDecimal growthMoney) {
		this.growthMoney = growthMoney;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "TDreamProduct{" +
				"pkId=" + pkId +
				", originalId=" + originalId +
				", website=" + website +
				", productName='" + productName + '\'' +
				", productUrl='" + productUrl + '\'' +
				", growthMoney=" + growthMoney +
				", updateDate=" + updateDate +
				", moneyCurrency=" + moneyCurrency +
				'}';
	}
}