package com.myroom.database.dao;

public class Currency {
    public static final String TABLE_NAME = "currency";
    public enum Column {
        COLUMN_CURRENCY_KEY(0, "currency_key"),
        COLUMN_CURRENCY_ID(1, "currency_id"),
        COLUMN_CURRENCY_ICON(2, "currency_icon"),
        COLUMN_IS_SELECTED(3, "is_selected");

        private int index;
        private String colName;

        Column(int index, String colName) {
            this.index = index;
            this.colName = colName;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getColName() {
            return colName;
        }

        public void setColName(String colName) {
            this.colName = colName;
        }
    }

    private long currencyKey;
    private String currencyId;
    private String currencyIcon;
    private int isSelected;

    public long getCurrencyKey() {
        return currencyKey;
    }

    public void setCurrencyKey(long currencyKey) {
        this.currencyKey = currencyKey;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyIcon() {
        return currencyIcon;
    }

    public void setCurrencyIcon(String currencyIcon) {
        this.currencyIcon = currencyIcon;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}
