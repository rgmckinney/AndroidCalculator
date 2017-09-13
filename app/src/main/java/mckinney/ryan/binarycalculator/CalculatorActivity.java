package mckinney.ryan.binarycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private Button kp0, kp1, kp2, kp3, kp4, kp5, kp6, kp7, kp8, kp9, kpA, kpB, kpC, kpD, kpE, kpF, kpX;
    private Button opAdd, opSub, opToBin, opToHex, opToDec;
    private Button typeBtnBin, typeBtnHex, typeBtnDec;
    private Button mainClear, mainEquals;
    private ImageButton mainSettings;
    private EditText mainDisplay, sideDisplay;
    private String prevText = "";
    private DBHCalculator calculator = new DBHCalculator();
    private DBHCalculator.Type inputType = DBHCalculator.Type.BIN;
    private boolean typeSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        init();
    }

    @Override
    public void onClick(View view) {
        prevText = mainDisplay.getText().toString();
        keyPadClick(view);
        opButtonClick(view);
        typeButtonClick(view);
        mainButtonClick(view);
    }

    private boolean mainButtonClick(View view) {
        switch(view.getId()) {
            case R.id.keyPadEquals:
                operationEquals();
                break;
            case R.id.keyPadClear:
                clear();
                break;
            case R.id.keyPadSettings:
                break;
            default:
                return false;
        }

        return true;
    }

    private boolean typeButtonClick(View view) {
        switch(view.getId()) {
            case R.id.typeButtonBin:
                setOrResetInputType(DBHCalculator.Type.BIN);
                break;
            case R.id.typeButtonHex:
                setOrResetInputType(DBHCalculator.Type.HEX);
                break;
            case R.id.typeButtonDec:
                setOrResetInputType(DBHCalculator.Type.DEC);
                break;
            default:
                return false;
        }

        return true;
    }

    private boolean keyPadClick(View view) {
        char inputChar;

        switch(view.getId()) {
            case R.id.keyPad0:
                inputChar = '0';
                break;
            case R.id.keyPad1:
                inputChar = '1';
                break;
            case R.id.keyPad2:
                inputChar = '2';
                break;
            case R.id.keyPad3:
                inputChar = '3';
                break;
            case R.id.keyPad4:
                inputChar = '4';
                break;
            case R.id.keyPad5:
                inputChar = '5';
                break;
            case R.id.keyPad6:
                inputChar = '6';
                break;
            case R.id.keyPad7:
                inputChar = '7';
                break;
            case R.id.keyPad8:
                inputChar = '8';
                break;
            case R.id.keyPad9:
                inputChar = '9';
                break;
            case R.id.keyPadA:
                inputChar = 'A';
                break;
            case R.id.keyPadB:
                inputChar = 'B';
                break;
            case R.id.keyPadC:
                inputChar = 'C';
                break;
            case R.id.keyPadD:
                inputChar = 'D';
                break;
            case R.id.keyPadE:
                inputChar = 'E';
                break;
            case R.id.keyPadF:
                inputChar = 'F';
                break;
            case R.id.keyPadX:
                inputChar = 'X';
                break;
            default:
                return false;
        }

        updateDisplayText(mainDisplay, inputChar);

        return true;
    }

    private boolean opButtonClick(View view) {
        switch(view.getId()) {
            case R.id.opToBin:
                operationToBinary();
                break;
            case R.id.opToHex:
                operationToHex();
                break;
            case R.id.opToDec:
                operationToDecimal();
                break;
            case R.id.opAdd:
                operationAdd();
                break;
            case R.id.opSub:
                operationSub();
                break;
            default:
                return false;
        }

        sideDisplay.setText(prevText);
        return true;
    }

    private void operationEquals() {
        String inputString = mainDisplay.getText().toString();
        String outputString = calculator.calculate(inputString, getInputType());
        mainDisplay.setText(outputString);
    }

    private void operationConvert(DBHCalculator.Type outType) {
        String inputString = mainDisplay.getText().toString();
        DBHCalculator.Type inType;
        if (typeSet)
            inType = inputType;
        else
            inType = calculator.parseType(inputString);
        Log.d("Input", "Input: "+inType.toString());
        calculator.setInType(inType);
        calculator.setOutType(outType);

        mainDisplay.setText(calculator.convert(inputString));
    }

    private void operationToBinary() {
        operationConvert(DBHCalculator.Type.BIN);
    }

    private void operationToHex() {
        operationConvert(DBHCalculator.Type.HEX);
    }

    private void operationToDecimal() {
        operationConvert(DBHCalculator.Type.DEC);
    }

    private void operationCalculation(DBHCalculator.Op operation) {
        calculator.storeNum(mainDisplay.getText().toString(), getInputType());
        calculator.setCurOp(operation);
        clear();
    }

    private void operationAdd() {
        operationCalculation(DBHCalculator.Op.ADD);
        unhighlightOperations();
        highlightOperation(DBHCalculator.Op.ADD);
    }

    private void operationSub() {
        operationCalculation(DBHCalculator.Op.SUB);
        unhighlightOperations();
        highlightOperation(DBHCalculator.Op.SUB);
    }

    private void highlightOperation(DBHCalculator.Op op) {

        switch(op) {
            case ADD:
                opAdd.setBackgroundColor(getResources().getColor(R.color.opBackgroundEvenSelected));
                break;
            case SUB:
                opSub.setBackgroundColor(getResources().getColor(R.color.opBackgroundOddSelected));
                break;
            default:
                break;
        }
    }

    private void unhighlightOperations() {
        opAdd.setBackgroundColor(getResources().getColor(R.color.opBackgroundEven));
        opSub.setBackgroundColor(getResources().getColor(R.color.opBackgroundOdd));
    }

    private void clear() {
        sideDisplay.setText(mainDisplay.getText());
        prevText = mainDisplay.getText().toString();

        mainDisplay.setText("");
    }

    private void updateDisplayText(EditText textField, char c) {
        String inputString = textField.getText().toString();

        if (c == 'X') {
            textField.setText(inputString.subSequence(0, inputString.length()-1));
        }
        else if (textField.getText().length() < 16) {
            textField.setText(inputString + c);
        }
    }

    private void setOrResetInputType(DBHCalculator.Type type) {
        if (typeSet && inputType == type) {
            typeSet = false;
            typeBtnBin.setBackgroundColor(getResources().getColor(R.color.keyPadBackground));
            typeBtnHex.setBackgroundColor(getResources().getColor(R.color.keyPadBackground));
            typeBtnDec.setBackgroundColor(getResources().getColor(R.color.keyPadBackground));
            return;
        }

        inputType = type;
        typeSet = true;

        if (type == DBHCalculator.Type.BIN) {
            typeBtnBin.setBackgroundColor(getResources().getColor(R.color.typeSelected));
            typeBtnHex.setBackgroundColor(getResources().getColor(R.color.keyPadBackground));
            typeBtnDec.setBackgroundColor(getResources().getColor(R.color.keyPadBackground));
        }
        else if (type == DBHCalculator.Type.HEX) {
            typeBtnHex.setBackgroundColor(getResources().getColor(R.color.typeSelected));
            typeBtnBin.setBackgroundColor(getResources().getColor(R.color.keyPadBackground));
            typeBtnDec.setBackgroundColor(getResources().getColor(R.color.keyPadBackground));
        }
        else if (type == DBHCalculator.Type.DEC) {
            typeBtnDec.setBackgroundColor(getResources().getColor(R.color.typeSelected));
            typeBtnHex.setBackgroundColor(getResources().getColor(R.color.keyPadBackground));
            typeBtnBin.setBackgroundColor(getResources().getColor(R.color.keyPadBackground));
        }
    }

    private DBHCalculator.Type getInputType() {
        DBHCalculator.Type inType;
        if (typeSet)
            inType = inputType;
        else
            inType = calculator.parseType(mainDisplay.getText().toString());

        return inType;
    }

    private void init() {
        mainDisplay = (EditText)findViewById(R.id.mainDisplay);
        mainDisplay.setKeyListener(null);
        sideDisplay = (EditText)findViewById(R.id.sideDisplay);
        sideDisplay.setKeyListener(null);

        opToBin = (Button)findViewById(R.id.opToBin);
        opToHex = (Button)findViewById(R.id.opToHex);
        opToDec = (Button)findViewById(R.id.opToDec);
        opAdd = (Button)findViewById(R.id.opAdd);
        opSub = (Button)findViewById(R.id.opSub);

        typeBtnBin = (Button)findViewById(R.id.typeButtonBin);
        typeBtnHex = (Button)findViewById(R.id.typeButtonHex);
        typeBtnDec = (Button)findViewById(R.id.typeButtonDec);

        mainSettings = (ImageButton)findViewById(R.id.keyPadSettings);
        mainClear = (Button)findViewById(R.id.keyPadClear);
        mainEquals = (Button)findViewById(R.id.keyPadEquals);

        kp0 = (Button)findViewById(R.id.keyPad0);
        kp1 = (Button)findViewById(R.id.keyPad1);
        kp2 = (Button)findViewById(R.id.keyPad2);
        kp3 = (Button)findViewById(R.id.keyPad3);
        kp4 = (Button)findViewById(R.id.keyPad4);
        kp5 = (Button)findViewById(R.id.keyPad5);
        kp6 = (Button)findViewById(R.id.keyPad6);
        kp7 = (Button)findViewById(R.id.keyPad7);
        kp8 = (Button)findViewById(R.id.keyPad8);
        kp9 = (Button)findViewById(R.id.keyPad9);
        kpA = (Button)findViewById(R.id.keyPadA);
        kpB = (Button)findViewById(R.id.keyPadB);
        kpC = (Button)findViewById(R.id.keyPadC);
        kpD = (Button)findViewById(R.id.keyPadD);
        kpE = (Button)findViewById(R.id.keyPadE);
        kpF = (Button)findViewById(R.id.keyPadF);
        kpX = (Button)findViewById(R.id.keyPadX);

        opToBin.setOnClickListener(this);
        opToHex.setOnClickListener(this);
        opToDec.setOnClickListener(this);
        opAdd.setOnClickListener(this);
        opSub.setOnClickListener(this);

        typeBtnBin.setOnClickListener(this);
        typeBtnHex.setOnClickListener(this);
        typeBtnDec.setOnClickListener(this);

        mainSettings.setOnClickListener(this);
        mainClear.setOnClickListener(this);
        mainEquals.setOnClickListener(this);

        kp0.setOnClickListener(this);
        kp1.setOnClickListener(this);
        kp2.setOnClickListener(this);
        kp3.setOnClickListener(this);
        kp4.setOnClickListener(this);
        kp5.setOnClickListener(this);
        kp6.setOnClickListener(this);
        kp7.setOnClickListener(this);
        kp8.setOnClickListener(this);
        kp9.setOnClickListener(this);
        kpA.setOnClickListener(this);
        kpB.setOnClickListener(this);
        kpC.setOnClickListener(this);
        kpD.setOnClickListener(this);
        kpE.setOnClickListener(this);
        kpF.setOnClickListener(this);
        kpX.setOnClickListener(this);
    }
}
