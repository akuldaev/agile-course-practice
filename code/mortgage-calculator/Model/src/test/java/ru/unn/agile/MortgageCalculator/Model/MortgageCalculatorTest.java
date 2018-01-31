package ru.unn.agile.MortgageCalculator.Model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;

import static ru.unn.agile.MortgageCalculator.Model.MortgageCalculator.PeriodType;

public class MortgageCalculatorTest {
    @Test
    public void canCalculateDifferentiatedMortgage() {
        MortgageCalculator calc = new MortgageCalculator(1000, 3, 0.05f);
        assertEquals(Arrays.asList(338, 336, 335), calc.differentiatedPayment(PeriodType.month));
    }

    @Test
    public void canCalculateAnnuityMortgage() {
        MortgageCalculator calc = new MortgageCalculator(1000, 3, 0.05f);
        assertEquals(336, calc.annuityPayment(PeriodType.month));
    }

    @Test
    public void canCalculateDifferentiatedMortgageWithYearPeriodType() {
        MortgageCalculator calc = new MortgageCalculator(100000, 1, 0.05f);
        assertEquals(Arrays.asList(8750, 8715, 8681, 8646, 8611, 8576, 8542, 8507,
                8472, 8437, 8403, 8368), calc.differentiatedPayment(PeriodType.year));
    }

    @Test
    public void canCalculateAnnuityMortgageWithYearPeriodType() {
        MortgageCalculator calc = new MortgageCalculator(100000, 1, 0.05f);
        assertEquals(8560, calc.annuityPayment(PeriodType.year));
    }

    @Test
    public void canGetPaymentForDifferentiatedMortgage() {
        MortgageCalculator calc = new MortgageCalculator(1000, 3, 0.05f);
        assertEquals(336, calc.getPayments("differentiated", 1, PeriodType.month));
    }

    @Test
    public void canReturnZeroWhenCalculateDifferentiatedMortgageWithZeroAmount() {
        MortgageCalculator calc = new MortgageCalculator(0, 3, 0.05f);
        assertEquals(Arrays.asList(0, 0, 0), calc.differentiatedPayment(PeriodType.month));
    }

    @Test
    public void canReturnZeroWhenCalculateAnnuityMortgageWithZeroAmount() {
        MortgageCalculator calc = new MortgageCalculator(0, 3, 0.05f);
        assertEquals(0, calc.annuityPayment(PeriodType.month));
    }

    @Test
    public void canGetPaymentForDifferentiatedMortgageWithZeroAmount() {
        MortgageCalculator calc = new MortgageCalculator(0, 3, 0.05f);
        assertEquals(0, calc.getPayments("differentiated", 1, PeriodType.month));
    }

    @Test
    public void canCatchErrorWhenCalculateDifferentiatedMortgageWithZeroPeriod() {
        try {
            MortgageCalculator calc = new MortgageCalculator(1000, 0, 0.05f);
            calc.differentiatedPayment(PeriodType.month);
        } catch (IllegalArgumentException thrown) {
            assertEquals("Period is zero", thrown.getMessage());
        }
    }

    @Test
    public void canCatchErrorWhenCalculateAnnuityMortgageWithZeroPeriod() {
        try {
            MortgageCalculator calc = new MortgageCalculator(1000, 0, 0.05f);
            calc.annuityPayment(PeriodType.month);
        } catch (IllegalArgumentException thrown) {
            assertEquals("Period is zero", thrown.getMessage());
        }
    }

    @Test
    public void canCatchErrorWhenCalculateDifferentiatedMortgageWithZeroRate() {
        try {
            MortgageCalculator calc = new MortgageCalculator(1000, 12, 0);
            calc.differentiatedPayment(PeriodType.month);
        } catch (IllegalArgumentException thrown) {
            assertEquals("Rate is zero", thrown.getMessage());
        }
    }

    @Test
    public void canCatchErrorWhenCalculateAnnuityMortgageWithZeroRate() {
        try {
            MortgageCalculator calc = new MortgageCalculator(1000, 12, 0);
            calc.annuityPayment(PeriodType.month);
        } catch (IllegalArgumentException thrown) {
            assertEquals("Rate is zero", thrown.getMessage());
        }
    }

    @Test
    public void canGetPaymentForDifferentiatedMortgageWithUndefinedTypeOfPayment() {
        try {
            MortgageCalculator calc = new MortgageCalculator(1000, 3, 0.05f);
            calc.getPayments("abcde", 1, PeriodType.month);
        } catch (IllegalArgumentException thrown) {
            assertEquals("Can't print payment of undefined type of payment", thrown.getMessage());
        }
    }
}
