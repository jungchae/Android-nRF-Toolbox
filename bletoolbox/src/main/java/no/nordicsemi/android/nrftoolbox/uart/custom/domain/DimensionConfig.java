package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ianpinto.androidrangeseekbar.rangeseekbar.RangeSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.BloodPressure;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.IReport;

/**
 * Created by jungchae on 17. 4. 19.
 */

class Dimension implements IReport{
    public static final String NAME = "Dimension";
    public static final String NAME_CUFFSIZE = "cuffsize";
    public static final String NAME_UPPERARMLEN = "upperarmlen";
    public static final String NAME_UPPERARMCIR = "upperarmcir";
    public static final String NAME_UNIT = "cm";
    public String cuffsize = "";
    public int upperarmlen = 0;
    public int upperarmcir = 0;

    @Override
    public int getMaxTrialCount() {
        return -1;
    }

    @Override
    public boolean getBoolean(String sKey) {
        return false;
    }

    @Override
    public int getInt(String sKey) {
        int ret = -1;
        switch(sKey) {
            case NAME_UPPERARMLEN:
                ret = upperarmlen;
                break;
            case NAME_UPPERARMCIR:
                ret = upperarmcir;
                break;
        }

        return ret;
    }

    @Override
    public String getString(String sKey) {
        String ret = "";
        switch(sKey) {
            case "NAME":
                ret = NAME;
                break;
            case NAME_CUFFSIZE:
                ret = cuffsize;
                break;
            case NAME_UPPERARMLEN:
                ret = upperarmlen + " " + NAME_UNIT;
                break;
            case NAME_UPPERARMCIR:
                ret = upperarmcir + " " + NAME_UNIT;
                break;
            default:
        }

        return ret;
    }

    @Override
    public String[] getRegisteredFileInfo() {
        return new String[0];
    }

    @Override
    public BloodPressure[] getBloodPressure() {
        return new BloodPressure[0];
    }
}

public class DimensionConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mEolGroupHeader;
    final private RadioGroup mEolGroup;
    final private RadioButton mEolGroup_lf;
    final private RadioButton mEolGroup_cr;
    final private RadioButton mEolGroup_cr_lf;
    final private TextView mSbrUpperArmLenHeader;
    final private RangeSeekBar mSbrUpperArmLen;
    final private TextView mSbrUpperArmCirHeader;
    final private RangeSeekBar mSbrUpperArmCir;

    private Dimension mReport;

    public DimensionConfig(View view) {
        mView = view;

        mEolGroupHeader = (TextView) mView.findViewById(R.id.uart_eol_header);
        mEolGroup = (RadioGroup) mView.findViewById(R.id.uart_eol);
        mEolGroup_lf = (RadioButton) mView.findViewById(R.id.uart_eol_lf);
        mEolGroup_cr = (RadioButton) mView.findViewById(R.id.uart_eol_cr);
        mEolGroup_cr_lf = (RadioButton) mView.findViewById(R.id.uart_eol_cr_lf);

        mSbrUpperArmLenHeader = (TextView) mView.findViewById(R.id.seekBarUpperarmLenHeader);
        mSbrUpperArmLen = (RangeSeekBar) mView.findViewById(R.id.seekBarUpperarmLen);
        mSbrUpperArmCirHeader = (TextView) mView.findViewById(R.id.seekBarUpperarmCirHeader);
        mSbrUpperArmCir = (RangeSeekBar) mView.findViewById(R.id.seekBarUpperarmCir);

        mReport = new Dimension();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;

        mEolGroup.setVisibility(mode);
        mEolGroupHeader.setText(R.string.exp_eol_header_cuff);
        mEolGroup_lf.setText(R.string.exp_eol_lf_header_cuff);
        mEolGroup_cr.setText(R.string.exp_eol_cr_header_cuff);
        mEolGroup_cr_lf.setText(R.string.exp_eol_cr_lf_header_cuff);
        mEolGroup.check(R.id.uart_eol_lf);

        mSbrUpperArmLenHeader.setVisibility(mode);
        mSbrUpperArmLen.setVisibility(mode);
        mSbrUpperArmCirHeader.setVisibility(mode);
        mSbrUpperArmCir.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        if(mEolGroup != null) {
            int i = mEolGroup.getCheckedRadioButtonId();
            if (i == R.id.uart_eol_lf) {
                mReport.cuffsize = mEolGroup_lf.getText().toString();
            } else if (i == R.id.uart_eol_cr) {
                mReport.cuffsize = mEolGroup_cr.getText().toString();
            } else {
                mReport.cuffsize = mEolGroup_cr_lf.getText().toString();
            }
        }
        mReport.upperarmlen = mSbrUpperArmLen.getSelectedMaxValue().intValue();
        mReport.upperarmcir = mSbrUpperArmCir.getSelectedMaxValue().intValue();

        try {
            jsonObj.put(Dimension.NAME_CUFFSIZE, mReport.cuffsize);
            jsonObj.put(Dimension.NAME_UPPERARMLEN, mReport.upperarmlen);
            jsonObj.put(Dimension.NAME_UPPERARMCIR, mReport.upperarmcir);

            jsonWrapper.put(Dimension.NAME, jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonWrapper.toString();
    }

    static public Object cmdJSONparse(String cmd) {
        Dimension result = new Dimension();
        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(Dimension.NAME);

            result.cuffsize = jObj.getString(Dimension.NAME_CUFFSIZE);
            result.upperarmlen = jObj.getInt(Dimension.NAME_UPPERARMLEN);
            result.upperarmcir = jObj.getInt(Dimension.NAME_UPPERARMCIR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RadioGroup getEolGroup(){
        return null;
    }

    private void uiConfiguration(String cmd) {
        if(cmd=="") return;

        mReport = (Dimension)cmdJSONparse(cmd);

        if( mReport.cuffsize !=null && mReport.cuffsize.equals(mEolGroup_lf.getText().toString()) ) {
            mEolGroup.check(R.id.uart_eol_lf);
        } else if( mReport.cuffsize !=null && mReport.cuffsize.equals(mEolGroup_cr.getText().toString()) ) {
            mEolGroup.check(R.id.uart_eol_cr);
        } else {
            mEolGroup.check(R.id.uart_eol_cr_lf);
        }
        mSbrUpperArmLen.setSelectedMaxValue(mReport.upperarmlen);
        mSbrUpperArmCir.setSelectedMaxValue(mReport.upperarmcir);
    }
}
