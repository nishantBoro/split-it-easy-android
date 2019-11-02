package com.nishantboro.splititeasy;

import java.math.BigDecimal;
import java.util.Comparator;

public class BalanceComparator implements Comparator<Balance> {

    // descending order
    @Override
    public int compare(Balance o1, Balance o2) {
        BigDecimal res = o2.balance.subtract(o1.balance);
        return res.compareTo(new BigDecimal("0.00"));
    }
}



