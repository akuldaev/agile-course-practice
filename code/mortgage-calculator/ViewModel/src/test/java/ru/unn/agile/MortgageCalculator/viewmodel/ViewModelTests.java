package ru.unn.agile.MortgageCalculator.viewmodel;

import ru.unn.agile.MortgageCalculator.viewmodel.ViewModel.Status;
import ru.unn.agile.MortgageCalculator.viewmodel.ViewModel.PaymentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ViewModelTests {

    public void setViewModel(final ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Before
    public void setUp() {
        viewModel = new ViewModel(new FakeLogger());
    }

    @After
    public void clean() {
        viewModel = null;
    }

    @Test
    public void canSetDefaultValue() {
        assertEquals("", viewModel.getResultSum());
    }

    @Test
    public void canNotSetInterestRateValue() {
        viewModel.setInterestRate("de");
        viewModel.processTextChanged();
        assertEquals(Status.BAD_FORMAT, viewModel.getStatus());
    }

    @Test
    public void canNotSetInterestRateNegativeValue() {
        viewModel.setInterestRate("-2");
        viewModel.processTextChanged();
        assertEquals(Status.BAD_FORMAT, viewModel.getStatus());
    }

    @Test
    public void canNotSetCreditPeriodValue() {
        viewModel.setPeriod("ew");
        viewModel.processTextChanged();
        assertEquals(Status.BAD_FORMAT, viewModel.getStatus());
    }

    @Test
    public void canNotSetAmountOfCreditNegativeValue() {
        viewModel.setAmountOfCredit("-2");
        viewModel.processTextChanged();
        assertEquals(Status.BAD_FORMAT, viewModel.getStatus());
    }

    @Test
    public void canNotSetAmountOfCreditValue() {
        viewModel.setAmountOfCredit("a");
        viewModel.processTextChanged();
        assertEquals(Status.BAD_FORMAT, viewModel.getStatus());
    }

    @Test
    public void canSetAllValues() {
        fillInputFields();
        viewModel.processTextChanged();
        assertEquals(Status.READY, viewModel.getStatus());
    }

    @Test
    public void canCalculateAnnuityMortgagePerMonth() {
        fillInputFields();
        viewModel.setDurationOfCredit("Month");
        viewModel.calculate();
        assertEquals("336", viewModel.getResultSum());
    }

    @Test
    public void canCalculateAnnuityMortgagePerYear() {
        fillInputFields();
        viewModel.setDurationOfCredit("Year");
        viewModel.calculate();
        assertEquals("29", viewModel.getResultSum());
    }

    @Test
    public void canCalculateDifferentiatedMortgagePerMonth() {
        fillInputFields();
        viewModel.setDurationOfCredit("Month");
        viewModel.setPeriodNumber("2");
        viewModel.setPaymentType(PaymentType.Differentiated);
        viewModel.calculate();
        assertEquals("336", viewModel.getResultSum());
    }

    @Test
    public void canCalculateDifferentiatedMortgagePerYear() {
        fillInputFields();
        viewModel.setPeriodNumber("2");
        viewModel.setDurationOfCredit("Year");
        viewModel.setPaymentType(PaymentType.Differentiated);
        viewModel.calculate();
        assertEquals("32", viewModel.getResultSum());
    }

    @Test
    public void canNotCalculateDifferentiatedMortgagePerYear() {
        viewModel.setAmountOfCredit("1000");
        viewModel.setInterestRate("0.05");
        viewModel.setPeriodNumber("0");
        viewModel.setDurationOfCredit("Year");
        viewModel.setPaymentType(PaymentType.Differentiated);
        viewModel.calculate();
        assertEquals(Status.BAD_FORMAT, viewModel.getStatus());
    }

    @Test
    public void canNotCalculateAnnuityMortgagePerYearWithEmptyValue() {
        viewModel.setAmountOfCredit("");
        viewModel.setInterestRate("");
        viewModel.setPeriod("");
        viewModel.setDurationOfCredit("Year");
        viewModel.calculate();
        assertEquals(Status.BAD_FORMAT, viewModel.getStatus());
    }

    @Test
    public void canNotCalculateDifferentiatedMortgageWhenPeriodTypeIsZero() {
        viewModel.setAmountOfCredit("1000");
        viewModel.setInterestRate("0.05");
        viewModel.setPeriod("0");
        viewModel.setDurationOfCredit("Year");
        viewModel.calculate();
        assertEquals(Status.BAD_FORMAT, viewModel.getStatus());
    }


    @Test
    public void canCreateViewModelWithLogger() {
        FakeLogger log = new FakeLogger();
        ViewModel viewModelWithLogger = new ViewModel(log);
        assertNotNull(viewModelWithLogger);
    }

    @Test
    public void viewModelConstructorThrowsExceptionWithNullLogger() {
        try {
            new ViewModel(null);
            fail("Exception wasn't thrown");
        } catch (IllegalArgumentException exception) {
            assertEquals("Parameter Logger can not be null", exception.getMessage());
        } catch (Exception exception) {
            fail("Invalid exception type");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void canThrowExceptionWhenLoggerIsNull() {
        new ViewModel(null);
    }

    @Test
    public void isLogEmptyAfterStart() {
        List<String> log = viewModel.getLog();
        assertEquals(0, log.size());
    }

    @Test
    public void isCalculatePuttingSomething() {
       fillInputFields();
        viewModel.calculate();

        List<String> log = viewModel.getLog();
        assertNotEquals(0, log.size());
    }

    @Test
    public void canAddLogWhenChangeInput() {
        viewModel.setAmountOfCredit("43");
        viewModel.setAmountOfCredit("2");
        List<String> log = viewModel.getLog();

        assertEquals(2, log.size());
    }

    @Test
    public void doesNotLogWhenInputWasNotChanged() {
        viewModel.setAmountOfCredit("2");
        viewModel.setAmountOfCredit("2");
        List<String> log = viewModel.getLog();

        assertEquals(1, log.size());
    }

    @Test
    public void doesLogContainInputArgumentWhenChangeInput() {
        String inputString = "1";
        viewModel.setAmountOfCredit(inputString);
        List<String> log = viewModel.getLog();

        assertTrue(log.get(0).matches(".*" + inputString + ".*"));
    }

    @Test
    public void doesLogContainLogMessageWhenChangeInput() {
        viewModel.setAmountOfCredit("5344");
        List<String> log = viewModel.getLog();

        assertTrue(log.get(0).matches(".*" + ViewModel.LogMessage.CHANGE_AMOUNT_OF_CREDIT + ".*"));
    }

    @Test
    public void doesLogContainArgumentWhenConvertWasClicked() {
        fillInputFields();
        viewModel.calculate();
        List<String> message = viewModel.getLog();
        assertTrue(message.get(3).matches(".*" + viewModel.getAmountOfCredit()
                + ".*" + viewModel.getInterestRate()
                + ".*" + viewModel.getPeriod() + ".*"
        ));
    }

    @Test
    public void isProperlyFormattingInfoAboutArguments() {
        fillInputFields();
        viewModel.setDurationOfCredit("Year");
        viewModel.calculate();
        List<String> message = viewModel.getLog();
        assertTrue(message.get(4).matches(".*Arguments"
                + ": Amount of credit = " + viewModel.getAmountOfCredit()
                + "; Interest rate = " + viewModel.getInterestRate()
                + "; Credit period = " + viewModel.getPeriod() +" "+ viewModel.getDuration()
                + "; Period for differentiated payment = " + viewModel.getPeriodNumber()
                + "; PaymentType = " + viewModel.getPaymentType()
                + "; Result = " + viewModel.getResultSum() + ".*"
        ));
    }

    @Test
    public void isOperationMentionedInTheLog() {
        fillInputFields();
        viewModel.setPaymentType(PaymentType.Differentiated);
        viewModel.setPeriodNumber("2");
        viewModel.calculate();

        List<String>  message = viewModel.getLog();
       assertTrue(message.get(5).matches(".*differentiated.*"));
    }

    @Test
    public void isMulOperationMentionedInTheLog() {
        fillInputFields();
        viewModel.setDurationOfCredit("Month");
        viewModel.setPaymentType(PaymentType.Differentiated);
        viewModel.setPaymentType(PaymentType.Annuity);
        viewModel.calculate();
        List<String>  message = viewModel.getLog();
        assertTrue(message.get(5).matches(".*annuity.*"));
    }

    @Test
    public void canPutSeveralLogMessages() {
        fillInputFields();
        viewModel.calculate();
        viewModel.calculate();
        viewModel.calculate();
        assertEquals(6, viewModel.getLog().size());
    }

    @Test
    public void canSeeOperationChangeInLog() {
        fillInputFields();
        viewModel.setPaymentType(PaymentType.Differentiated);
        viewModel.setPeriodNumber("2");
        viewModel.calculate();
        List<String> message = viewModel.getLog();
        assertTrue(message.get(5).matches(".*; PaymentType = " + "differentiated.*"));
    }

    @Test
    public void isOperationNotLoggedWhenNotChanged() {
        viewModel.setPaymentType(PaymentType.Differentiated);
        viewModel.setPaymentType(PaymentType.Differentiated);

        assertEquals(1, viewModel.getLog().size());
    }
    private ViewModel viewModel;

    private void fillInputFields() {
        viewModel.setAmountOfCredit("1000");
        viewModel.setPeriod("3");
        viewModel.setInterestRate("0.05");
    }
}
//