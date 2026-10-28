package com.javaweb.Beans;

public class TransactionCreateRequest {
    private Long typeId;
    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }
    private String note;
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
