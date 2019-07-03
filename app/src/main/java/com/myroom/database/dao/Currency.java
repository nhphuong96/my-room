package com.myroom.database.dao;

public class Currency extends BaseModel {
    public static final String TABLE_NAME = "currency";
    public enum Column {
        COLUMN_CURRENCY_CD(1, "currency_cd"),
        COLUMN_CURRENCY_ICON(2, "currency_icon"),
        COLUMN_IS_DECIMAL(3, "is_decimal");

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

    private String currencyCd;
    private String currencyIcon;
    private int isDecimal;
    private boolean selected;

    public String getCurrencyCd() {
        return currencyCd;
    }

    public void setCurrencyCd(String currencyCd) {
        this.currencyCd = currencyCd;
    }

    public String getCurrencyIcon() {
        return currencyIcon;
    }

    public void setCurrencyIcon(String currencyIcon) {
        this.currencyIcon = currencyIcon;
    }

    public int getIsDecimal() {
        return isDecimal;
    }

    public void setIsDecimal(int isDecimal) {
        this.isDecimal = isDecimal;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
