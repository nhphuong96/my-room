package com.myroom.utils;

import android.content.Context;
import android.content.res.Resources;

import com.myroom.R;
import com.myroom.core.CurrencyId;
import com.myroom.database.dao.Currency;

public final class FormattedTextUtils {
    private FormattedTextUtils() {

    }

    public static String getLastIndexFormattedText(Context context) {
        return context.getResources().getString(R.string.last_index_text);
    }

    public static String getCurrentIndexFormattedText(Context context) {
        return context.getResources().getString(R.string.current_index_text);
    }

    public static String getPriceWithCurrencyTextFormat(Context context, Currency selectedCurrency) {
        Resources resources = context.getResources();
        CurrencyId currencyId = CurrencyId.valueOf(selectedCurrency.getCurrencyId());
        switch (currencyId) {
            case CNY:
                return resources.getString(R.string.price_with_cny);
            case EUR:
                return resources.getString(R.string.price_with_eur);
            case JPY:
                return resources.getString(R.string.price_with_jpy);
            case USD:
                return resources.getString(R.string.price_with_usd);
            case VND:
                return resources.getString(R.string.price_with_vnd);
            default:
                return resources.getString(R.string.price_with_vnd);
        }
    }
}
