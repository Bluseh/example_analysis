package com.example.alertdialog.pojo;

import java.io.Serializable;
import java.sql.Timestamp;


import java.sql.Timestamp;

public class Express implements Serializable {
	private String id;
	private Integer type;
	private Float weight;
	private Float fee;
	private Integer status;
	private Integer sender;
	private Integer receiver;
	private String senderTel;
	private String receiverTel;
	private String senderName;
	private String receiverName;
	private String senderAddress;
	private String receiverAddress;
	private String senderRegionCode;
	private String receiverRegionCode;
	private String createTime;
	private Integer picker;
	private Timestamp pickTime;
	private Integer deliver;
	private Timestamp deliverTime;
	private Integer signer;
	private Timestamp signTime;

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getFee() {
		return fee;
	}

	public void setFee(Float fee) {
		this.fee = fee;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSender() {
		return sender;
	}

	public void setSender(Integer sender) {
		this.sender = sender;
	}

	public Integer getReceiver() {
		return receiver;
	}

	public void setReceiver(Integer receiver) {
		this.receiver = receiver;
	}

	public String getSenderTel() {
		return senderTel;
	}

	public void setSenderTel(String senderTel) {
		this.senderTel = senderTel;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getSenderRegionCode() {
		return senderRegionCode;
	}

	public void setSenderRegionCode(String senderRegionCode) {
		this.senderRegionCode = senderRegionCode;
	}

	public String getReceiverRegionCode() {
		return receiverRegionCode;
	}

	public void setReceiverRegionCode(String receiverRegionCode) {
		this.receiverRegionCode = receiverRegionCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getPicker() {
		return picker;
	}

	public void setPicker(Integer picker) {
		this.picker = picker;
	}

	public Timestamp getPickTime() {
		return pickTime;
	}

	public void setPickTime(Timestamp pickTime) {
		this.pickTime = pickTime;
	}

	public Integer getDeliver() {
		return deliver;
	}

	public void setDeliver(Integer deliver) {
		this.deliver = deliver;
	}

	public Timestamp getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Timestamp deliverTime) {
		this.deliverTime = deliverTime;
	}

	public Integer getSigner() {
		return signer;
	}

	public void setSigner(Integer signer) {
		this.signer = signer;
	}

	public Timestamp getSignTime() {
		return signTime;
	}

	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}

	// toString method
	@Override
	public String toString() {
		return "Delivery{" +
				"id='" + id + '\'' +
				", type=" + type +
				", weight=" + weight +
				", fee=" + fee +
				", status=" + status +
				", sender=" + sender +
				", receiver=" + receiver +
				", senderTel='" + senderTel + '\'' +
				", receiverTel='" + receiverTel + '\'' +
				", senderName='" + senderName + '\'' +
				", receiverName='" + receiverName + '\'' +
				", senderAddress='" + senderAddress + '\'' +
				", receiverAddress='" + receiverAddress + '\'' +
				", senderRegionCode='" + senderRegionCode + '\'' +
				", receiverRegionCode='" + receiverRegionCode + '\'' +
				", createTime='" + createTime + '\'' +
				", picker=" + picker +
				", pickTime=" + pickTime +
				", deliver=" + deliver +
				", deliverTime=" + deliverTime +
				", signer=" + signer +
				", signTime=" + signTime +
				'}';
	}
	public static final class STATUS{
		public static final int STATUS_CREATED = 0;
		public static final int STATUS_PICKED = 1;
		public static final int STATUS_TRANSPORTING = 2;
		public static final int STATUS_DELIVERING = 3;
		public static final int STATUS_SIGNED = 4;
		public static final int STATUS_SORTED = 5;
		public static final int STATUS_UNSORTED = 6;
		public static final int STATUS_WAIT_DELIVER = 7;
	}
}



