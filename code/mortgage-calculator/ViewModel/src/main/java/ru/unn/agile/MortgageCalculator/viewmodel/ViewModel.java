
package ru.unn.agile.MortgageCalculator.viewmodel;

import ru.unn.agile.MortgageCalculator.Model.MortgageCalculator;
import ru.unn.agile.MortgageCalculator.Model.MortgageCalculator.PeriodType;

import java.util.List;

public class ViewModel {

    private ILogger logger;

    public ViewModel(final ILogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Parameter Logger can not be null");
        }
        this.logger = logger;

        this.amountOfCredit = "0.0";
        this.interestRate = "0.0";
        this.period = "0";
        this.paymentType = PaymentType.Annuity;
        this.durationOfCredit = "Month";
        this.resultSum = "";
        this.status = Status.WAITING;
        periodNumber = "0";
    }

    public List<String> getLog() {
        return logger.getLog();
    }

    public void setAmountOfCredit(final String amountOfCredit) {

        if (!isNumbersInString(amountOfCredit)) {
            status = Status.BAD_FORMAT;
            return;
        }
        if (!this.amountOfCredit.equals(amountOfCredit)) {
            logger.log(String.format("%s%s", LogMessage.CHANGE_AMOUNT_OF_CREDIT, amountOfCredit));
        }
        this.amountOfCredit = amountOfCredit;
    }

    public void setPeriodNumber(final String periodNumber) {

        if (!this.periodNumber.equals(periodNumber)) {
            logger.log(String.format("%s%s",
                    LogMessage.CHANGE_PERIOD_FOR_DIFFERENTIATED, periodNumber));
        }
        this.periodNumber = periodNumber;
    }

    public void setInterestRate(final String interestRate) {

        if (!this.interestRate.equals(interestRate)) {
            logger.log(String.format("%s%s", LogMessage.CHANGE_INTEREST_RATE, interestRate));
        }
        if (!isNumbersInString(interestRate)) {
            status = Status.BAD_FORMAT;
            return;
        }

        this.interestRate = interestRate;
    }

    public void setDurationOfCredit(final String durationOfCredit) {
        if (!this.durationOfCredit.equals(durationOfCredit)) {
            logger.log(String.format("%s%s",
                    LogMessage.DURATION_OF_CREDIT_WAS_CHANGED, durationOfCredit));
        }
        this.durationOfCredit = durationOfCredit;
    }

    public void setPaymentType(final PaymentType paymentType) {
        if (this.paymentType != paymentType) {
            logger.log(LogMessage.PAYMENT_TYPE_WAS_CHANGED + paymentType.toString());
        }
        this.paymentType = paymentType;
    }

    public void setPeriod(final String period) {

        if (!this.period.equals(period)) {
            logger.log(String.format("%s%s", LogMessage.CHANGE_PERIOD, period));
        }
        this.period = period;
    }

    public String getResultSum() {
        return resultSum;
    }

    public String getAmountOfCredit() {
        return amountOfCredit;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public String getPeriod() {
        return period;
    }

    public String getPeriodNumber() {
        return periodNumber;
    }

    public String getDuration() {
        return durationOfCredit;
    }

    public String getPaymentType() {
        return paymentType.toString();
    }

    public String getStatus() {
        return status;
    }

    public void processTextChanged() {
        parseInput();
    }

    private boolean isCorrectPeriodNumber() {
        if (!isNumbersInString(periodNumber)) {
            return false;
        } else {
            int periodNumberInteger = Integer.parseInt(periodNumber);
            if (periodNumberInteger <= 0 || periodNumberInteger > Integer.parseInt(period)) {
                return false;
            }
        }
        return true;
    }

    public void calculate() {

        if (!parseInput()) {
            return;
        }

        MortgageCalculator mortagageCalculator = new MortgageCalculator(amountOfCredit,
                period, interestRate);

        int indexPeriodNumber = Integer.parseInt(periodNumber) - 1;
        int payment = mortagageCalculator.getPayments(paymentType.toString(),
                indexPeriodNumber, getDurationOfCredit());

        if (payment != 0) {
            this.resultSum = Integer.toString(payment);
            status = Status.SUCCESS;
        } else {
            status = Status.BAD_FORMAT;
        }
        this.resultSum = Integer.toString(payment);
        logger.log(calculateLogMessage());
    }

    public static final class LogMessage {
        public static final String CHANGE_AMOUNT_OF_CREDIT =
                "Change amount of credit on ";
        public static final String CHANGE_INTEREST_RATE =
                "Change interest rate on ";
        public static final String CHANGE_PERIOD =
                "Change credit period on ";
        public static final String CHANGE_PERIOD_FOR_DIFFERENTIATED =
                "Change period for differentiated payment on ";
        public static final String CALCULATE_WAS_PRESSED =
                "Calculate was pressed. Calculate ";
        public static final String PAYMENT_TYPE_WAS_CHANGED =
                "Payment type was changed to ";
        public static final String DURATION_OF_CREDIT_WAS_CHANGED =
                "Duration of credit was changed to ";

        private LogMessage() {
        }
    }

    private String calculateLogMessage() {
        return LogMessage.CALCULATE_WAS_PRESSED + "Arguments"
                + ": Amount of credit = " + amountOfCredit
                + "; Interest rate = " + interestRate
                + "; Credit period = " + period + " " + durationOfCredit
                + "; Period for differentiated payment = " + periodNumber
                + "; PaymentType = " + paymentType.toString()
                + "; Result = " + getResultSum();
    }

    public final class Status {
        public static final String WAITING = "Please provide input data";
        public static final String READY = "Press 'Calculate' or Enter";
        public static final String BAD_FORMAT = "Bad format";
        public static final String SUCCESS = "Success";

        private Status() {
        }
    }

    public enum PaymentType {
        Annuity("annuity"),
        Differentiated("differentiated");
        private final String name;

        PaymentType(final String name) {

            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public boolean isDifferentiatedTypeStatusUpdate() {
        return (paymentType == PaymentType.Differentiated);
    }

    private String amountOfCredit;
    private String interestRate;
    private String period;
    private String resultSum;
    private PaymentType paymentType;
    private String status;
    private String durationOfCredit;
    private String periodNumber;

    private boolean parseInput() {
        if (!isCorrectData()) {
            status = Status.BAD_FORMAT;
            return false;
        }

        if (isDifferentiatedTypeStatusUpdate() && !isCorrectPeriodNumber()) {
            status = Status.BAD_FORMAT;
            return false;
        }

        try {
            if (!amountOfCredit.isEmpty()) {
                Float.parseFloat(amountOfCredit);
            }
            if (!interestRate.isEmpty()) {
                Float.parseFloat(interestRate);
            }
            if (!period.isEmpty()) {
                Integer.parseInt(period);
            }
        } catch (Exception e) {
            status = Status.BAD_FORMAT;
            return false;
        }

        if (isInputAvailable()) {
            status = Status.READY;
        } else {
            status = Status.WAITING;
        }

        return isInputAvailable();
    }

    private boolean isInputAvailable() {
        return !amountOfCredit.isEmpty() && !interestRate.isEmpty() && !period.isEmpty();
    }

    private PeriodType getDurationOfCredit() {
        PeriodType periodType = null;

        switch (durationOfCredit) {
            case "Month":
                periodType = PeriodType.month;
                break;
            case "Year":
                periodType = PeriodType.year;
                break;
            default:
                break;
        }
        return periodType;
    }

    private boolean isCorrectFloatValue(final String value) {
        return (isNumbersInString(value) && Float.parseFloat(value) > 0.0f);
    }

    private boolean isCorrectIntValue(final String value) {
        return (isNumbersInString(value) && Integer.parseInt(value) > 0.0f);
    }

    private boolean isCorrectData() {
        return (isCorrectFloatValue(amountOfCredit) && isCorrectFloatValue(interestRate)
                && isCorrectIntValue(period));
    }

    private boolean isNumbersInString(final String line) {
        return line.matches("-?\\d+(\\.\\d+)?");
    }
}
