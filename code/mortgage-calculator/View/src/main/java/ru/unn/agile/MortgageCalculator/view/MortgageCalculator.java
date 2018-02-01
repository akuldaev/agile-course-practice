package ru.unn.agile.MortgageCalculator.view;

import ru.unn.agile.MortgageCalculator.viewmodel.ViewModel;
import ru.unn.agile.MortgageCalculator.infrastructure.FileLogger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Date;
import java.util.List;

public final class MortgageCalculator {

    public static void main(final String[] args) {
        JFrame frame = new JFrame("MortgageCalculator");
        String logFileName = String.format("./MortgageCalculator_%te_%<tm_%<tY.log", new Date());
        FileLogger logger = new FileLogger(logFileName);
        frame.setContentPane(new MortgageCalculator(new ViewModel(logger)).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setSize(FORM_WIDTH, FORM_HEIGHT);
        frame.setVisible(true);
    }

    private MortgageCalculator(final ViewModel viewModel) {
        this.viewModel = viewModel;

        backBind();

        loadListOfPayments();

        txtPeriodNumber.setEnabled(viewModel.isDifferentiatedTypeStatusUpdate());

        calculateButton.addActionListener(actionEvent -> {
            bind();
            viewModel.calculate();
            backBind();
        });

        cbPayment.addActionListener(actionEvent -> {
            bind();
            viewModel.setPaymentType((ViewModel.PaymentType) cbPayment.getSelectedItem());
            txtPeriodNumber.setEnabled(viewModel.isDifferentiatedTypeStatusUpdate());
            backBind();
        });

        cbPeriodType.addActionListener(actionEvent -> {
            bind();
            viewModel.setDurationOfCredit(cbPeriodType.getSelectedItem().toString());
            txtPeriodNumber.setEnabled(viewModel.isDifferentiatedTypeStatusUpdate());
            backBind();
        });

        setAmount.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }
        });

        txtPeriodNumber.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }
        });

        setRate.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }
        });

        setPeriod.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                bind();
                backBind();
            }
        });

    }

    private void loadListOfPayments() {
        ViewModel.PaymentType[] paymentType = ViewModel.PaymentType.values();
        cbPayment.setModel(new JComboBox<>(paymentType).getModel());
    }

    private void bind() {
        if (!setAmount.getText().equals("")) {
            viewModel.setAmountOfCredit(setAmount.getText());
        }
        if (!setRate.getText().equals("")) {
            viewModel.setInterestRate(setRate.getText());
        }
        if (!setPeriod.getText().equals("")) {
            viewModel.setPeriod(setPeriod.getText());
        }

        viewModel.setDurationOfCredit((String) cbPeriodType.getSelectedItem());
        String periodNumber = txtPeriodNumber.getText();
        if (txtPeriodNumber.isEnabled() && !periodNumber.equals("")) {
            viewModel.setPeriodNumber(txtPeriodNumber.getText());
        }
    }

    private void backBind() {
        textSum.setText(viewModel.getResultSum());
        lbStatus.setText(viewModel.getStatus());

        List<String> log = viewModel.getLog();
        String[] items = log.toArray(new String[log.size()]);
        listLog.setListData(items);
    }

    private JPanel mainPanel;
    private JTextField setAmount;
    private JTextField setRate;
    private JTextField setPeriod;
    private JButton calculateButton;
    private JTextField textSum;
    private JComboBox<ViewModel.PaymentType> cbPayment;
    private JLabel lbStatus;
    private JComboBox cbPeriodType;
    private JTextField txtPeriodNumber;
    private JList<String> listLog;
    private ViewModel viewModel;
    private static final Integer FORM_WIDTH = 500;
    private static final Integer FORM_HEIGHT = 450;
}
//n
