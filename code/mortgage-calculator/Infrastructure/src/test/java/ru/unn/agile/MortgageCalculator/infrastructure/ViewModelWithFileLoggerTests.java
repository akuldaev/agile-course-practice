package ru.unn.agile.MortgageCalculator.infrastructure;

import ru.unn.agile.MortgageCalculator.viewmodel.ViewModel;
import ru.unn.agile.MortgageCalculator.viewmodel.ViewModelTests;

public class ViewModelWithFileLoggerTests extends ViewModelTests {
    @Override
    public void setUp() {
        FileLogger realLogger =
                new FileLogger("./ViewModelWithFileLoggerTests.log");
        super.setViewModel(new ViewModel(realLogger));
    }
}
