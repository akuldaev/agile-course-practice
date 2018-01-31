package ru.unn.agile.MortgageCalculator.Model;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class MortgageCalculator {
    public enum PeriodType { month, year };

    public MortgageCalculator(final float amount, final int period, final float rate) {
        this.amount = amount;
        this.period = period;
        this.rate = rate;
        payments = new ArrayList<Integer>();
    }

    public MortgageCalculator(final String amount, final String period, final String rate) {
        this.amount = Float.parseFloat(amount);
        this.period = Integer.parseInt(period);
        this.rate = Float.parseFloat(rate);
        payments = new ArrayList<Integer>();
    }

    public List<Integer> differentiatedPayment(final PeriodType paymentPeriod) {
        int periodNumber = 0;
        float amountT = amount;
        if (period != 0) {
            if (rate != 0) {
                switch (paymentPeriod) {
                    case month:
                        periodNumber = period;
                        break;
                    case year:
                        periodNumber = period * MONTH_IN_YEAR;
                        break;
                    default:
                        throw new IllegalArgumentException("Type of period is undefined");
                }
                for (int i = 0; i < periodNumber; i++) {
                    payments.add(round(amountT / (periodNumber - i) + amountT * (rate
                            / MONTH_IN_YEAR)));
                    amountT -= amount / periodNumber;
                }
                return payments;
            } else {
                throw new IllegalArgumentException("Rate is zero");
            }
        } else {
            throw new IllegalArgumentException("Period is zero");
        }
    }

    public int annuityPayment(final PeriodType paymentPeriod) {
        int n = period;
        if (period != 0) {
            if (rate != 0) {
                if (paymentPeriod != null && !paymentPeriod.equals(PeriodType.month)
                        && !paymentPeriod.equals(PeriodType.year)) {
                    throw new IllegalArgumentException("Type of period is undefined");
                } else if (paymentPeriod != null && paymentPeriod.equals(PeriodType.year)) {
                    n *= MONTH_IN_YEAR;
                }
                payment = (int) (amount * (((rate / MONTH_IN_YEAR) * pow(1 + rate / MONTH_IN_YEAR,
                        n)) / (pow(1 + (rate / MONTH_IN_YEAR), n) - 1)));
                return payment;
            } else {
                throw new IllegalArgumentException("Rate is zero");
            }
        } else {
            throw new IllegalArgumentException("Period is zero");
        }
    }

    public int getPayments(final String paymentType, final int periodNumber,
                           final PeriodType paymentPeriod) {
        switch (paymentType) {
            case "differentiated":
                return differentiatedPayment(paymentPeriod).get(periodNumber);
            case "annuity":
                return annuityPayment(paymentPeriod);
            default:
                throw new IllegalArgumentException("Can't print payment of undefined "
                        + "type of payment");
        }
    }

    private static final int MONTH_IN_YEAR = 12;
    private float amount;
    private int period;
    private float rate;
    private List<Integer> payments;
    private int payment;
}
