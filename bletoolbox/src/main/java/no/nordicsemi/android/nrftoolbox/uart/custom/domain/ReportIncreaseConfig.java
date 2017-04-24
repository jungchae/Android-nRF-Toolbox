package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ianpinto.androidrangeseekbar.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.BloodPressure;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.IReport;

/**
 * Created by jungchae on 17. 4. 19.
 */

class ReportIncrease implements IReport{
    public static final String NAME = "ReportIncrease";
    public static final String NAME_LOADFIRST = "loadfirst";
    public static final String NAME_LOADSECOND = "loadsecond";
    public static final String NAME_SYS = "systolic";
    public static final String NAME_DIA = "diastolic";
    public static final String NAME_VALS = "values";
    public static final String NAME_FNAMES = "fnames";
    public static final String NAME_FEXT = "bin";
    public static final String NAME_UNIT = "mmHg";
    private static final int TRIAL_COUNT = 4;
    public int loadfirst;
    public int loadsecond;
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT];
    public String[] fnames = new String[TRIAL_COUNT];
    ReportIncrease() {
        for (int i = 0; i < TRIAL_COUNT; i++) {
            values[i] = new BloodPressure();
            fnames[i] = new String("");
        }
    }

    @Override
    public int getMaxTrialCount() {
        return TRIAL_COUNT;
    }

    @Override
    public boolean getBoolean(String sKey) {
        return false;
    }

    @Override
    public int getInt(String sKey) {
        int ret = -1;

        switch(sKey) {
            case NAME_LOADFIRST:
                ret = loadfirst;
                break;
            case NAME_LOADSECOND:
                ret = loadsecond;
                break;
            default:
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
            case NAME_LOADFIRST:
                ret = loadfirst + " kg";
                break;
            case NAME_LOADSECOND:
                ret = loadsecond + " kg";
                break;
            default:
        }
        return ret;
    }

    @Override
    public String[] getRegisteredFileInfo() {
        return fnames;
    }

    @Override
    public BloodPressure[] getBloodPressure() {
        return new BloodPressure[0];
    }
}

public class ReportIncreaseConfig implements IExperimentProtocol {

    final private View mView;

    final private TextView mSbrIncreaseLoadHeader;
    final private TextView mSbrIncreaseHeader;
    
    final private RangeSeekBar mSbrIncreaseLoadFirst;
    final private RangeSeekBar mSbrIncreaseLoadSecond;
    final private RangeSeekBar mSbrIncreaseFirst;
    final private RangeSeekBar mSbrIncreaseSecond;
    final private RangeSeekBar mSbrIncreaseThird;
    final private RangeSeekBar mSbrIncreaseFourth;

    final private EditText mEditIncreaseFirstFname;
    final private EditText mEditIncreaseSecondFname;
    final private EditText mEditIncreaseThirdFname;
    final private EditText mEditIncreaseFourthFname;

    private ReportIncrease mReport;

    static public ReportIncrease cmdJSONparse(String cmd) {
        ReportIncrease result = new ReportIncrease();
        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportIncrease.NAME);
            result.loadfirst = jObj.getInt(ReportIncrease.NAME_LOADFIRST);
            result.loadsecond = jObj.getInt(ReportIncrease.NAME_LOADSECOND);
            JSONArray jArr = jObj.getJSONArray(ReportIncrease.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[i].systolic = obj.getInt(ReportIncrease.NAME_SYS);
                result.values[i].diastolic = obj.getInt(ReportIncrease.NAME_DIA);
            }

            jArr = jObj.getJSONArray(ReportIncrease.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[i] = obj.getString(ReportIncrease.NAME_FEXT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ReportIncreaseConfig(View view) {
        mView = view;

        mSbrIncreaseLoadHeader = (TextView) mView.findViewById(R.id.seekBarIncreaseLoadHeader);
        mSbrIncreaseHeader = (TextView) mView.findViewById(R.id.seekBarIncreaseHeader);

        mSbrIncreaseLoadFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseLoadFirst);
        mSbrIncreaseLoadSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseLoadSecond);
        mSbrIncreaseFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseFirst);
        mSbrIncreaseSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseSecond);
        mSbrIncreaseThird = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseThird);
        mSbrIncreaseFourth = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseFourth);

        mEditIncreaseFirstFname = (EditText) mView.findViewById(R.id.editIncreaseFirstFname);
        mEditIncreaseSecondFname = (EditText) mView.findViewById(R.id.editIncreaseSecondFname);
        mEditIncreaseThirdFname = (EditText) mView.findViewById(R.id.editIncreaseThirdFname);
        mEditIncreaseFourthFname = (EditText) mView.findViewById(R.id.editIncreaseFourthFname);

        mReport = new ReportIncrease();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrIncreaseLoadHeader.setVisibility(mode);
        mSbrIncreaseHeader.setVisibility(mode);

        mSbrIncreaseLoadFirst.setVisibility(mode);
        mSbrIncreaseLoadSecond.setVisibility(mode);
        mSbrIncreaseFirst.setVisibility(mode);
        mSbrIncreaseSecond.setVisibility(mode);
        mSbrIncreaseThird.setVisibility(mode);
        mSbrIncreaseFourth.setVisibility(mode);

        mEditIncreaseFirstFname.setVisibility(mode);
        mEditIncreaseSecondFname.setVisibility(mode);
        mEditIncreaseThirdFname.setVisibility(mode);
        mEditIncreaseFourthFname.setVisibility(mode);
        mEditIncreaseFirstFname.setHint(R.string.exp_fname_hint);
        mEditIncreaseSecondFname.setHint(R.string.exp_fname_hint);
        mEditIncreaseThirdFname.setHint(R.string.exp_fname_hint);
        mEditIncreaseFourthFname.setHint(R.string.exp_fname_hint);
        mEditIncreaseFirstFname.setTextSize(14);
        mEditIncreaseSecondFname.setTextSize(14);
        mEditIncreaseThirdFname.setTextSize(14);
        mEditIncreaseFourthFname.setTextSize(14);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.loadfirst = mSbrIncreaseLoadFirst.getSelectedMaxValue().intValue();
        mReport.loadsecond = mSbrIncreaseLoadSecond.getSelectedMaxValue().intValue();

        mReport.values[0].systolic = mSbrIncreaseFirst.getSelectedMaxValue().intValue();
        mReport.values[0].diastolic= mSbrIncreaseFirst.getSelectedMinValue().intValue();
        mReport.values[1].systolic = mSbrIncreaseSecond.getSelectedMaxValue().intValue();
        mReport.values[1].diastolic= mSbrIncreaseSecond.getSelectedMinValue().intValue();
        mReport.values[2].systolic = mSbrIncreaseThird.getSelectedMaxValue().intValue();
        mReport.values[2].diastolic= mSbrIncreaseThird.getSelectedMinValue().intValue();
        mReport.values[3].systolic = mSbrIncreaseFourth.getSelectedMaxValue().intValue();
        mReport.values[3].diastolic= mSbrIncreaseFourth.getSelectedMinValue().intValue();

        mReport.fnames[0] = mEditIncreaseFirstFname.getText().toString();
        mReport.fnames[1] = mEditIncreaseSecondFname.getText().toString();
        mReport.fnames[2] = mEditIncreaseThirdFname.getText().toString();
        mReport.fnames[3] = mEditIncreaseFourthFname.getText().toString();
        try {
            jsonObj.put(ReportIncrease.NAME_LOADFIRST, mReport.loadfirst);
            jsonObj.put(ReportIncrease.NAME_LOADSECOND, mReport.loadsecond);
            JSONArray jsonArr = new JSONArray();
            for (BloodPressure bp : mReport.values ) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportIncrease.NAME_SYS, bp.systolic);
                bpObj.put(ReportIncrease.NAME_DIA, bp.diastolic);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportIncrease.NAME_VALS, jsonArr);

            jsonArr = new JSONArray();
            for (String st : mReport.fnames) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportIncrease.NAME_FEXT, st);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportIncrease.NAME_FNAMES, jsonArr);

            jsonWrapper.put(ReportIncrease.NAME, jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonWrapper.toString();
    }

    public RadioGroup getEolGroup(){
        return null;
    }

    private void uiConfiguration(String cmd) {
        if(cmd=="") return;

        mReport = cmdJSONparse(cmd);

        mSbrIncreaseLoadFirst.setSelectedMaxValue(mReport.loadfirst);
        mSbrIncreaseLoadSecond.setSelectedMaxValue(mReport.loadsecond);

        mSbrIncreaseFirst.setSelectedMaxValue(mReport.values[0].systolic);
        mSbrIncreaseFirst.setSelectedMinValue(mReport.values[0].diastolic);
        mSbrIncreaseSecond.setSelectedMaxValue(mReport.values[1].systolic);
        mSbrIncreaseSecond.setSelectedMinValue(mReport.values[1].diastolic);
        mSbrIncreaseThird.setSelectedMaxValue(mReport.values[2].systolic);
        mSbrIncreaseThird.setSelectedMinValue(mReport.values[2].diastolic);
        mSbrIncreaseFourth.setSelectedMaxValue(mReport.values[3].systolic);
        mSbrIncreaseFourth.setSelectedMinValue(mReport.values[3].diastolic);

        mEditIncreaseFirstFname.setText(mReport.fnames[0]);
        mEditIncreaseSecondFname.setText(mReport.fnames[1]);
        mEditIncreaseThirdFname.setText(mReport.fnames[2]);
        mEditIncreaseFourthFname.setText(mReport.fnames[3]);
    }
}
