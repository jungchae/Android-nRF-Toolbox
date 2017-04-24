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
class ReportStatic implements IReport{
    public static final String NAME = "ReportStatic";
    public static final String NAME_SYS = "systolic";
    public static final String NAME_DIA = "diastolic";
    public static final String NAME_VALS = "values";
    public static final String NAME_FNAMES = "fnames";
    public static final String NAME_FEXT = "bin";
    public static final String NAME_UNIT = "mmHg";
    private static final int TRIAL_COUNT = 3;
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT];
    public String[] fnames = new String[TRIAL_COUNT];

    ReportStatic() {
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
    public int getInt(String sKey) {
        return -1;
    }

    @Override
    public String getString(String sKey) {
        String ret = "";

        switch(sKey) {
            case "NAME":
                ret = NAME;
                break;
        }
        return ret;
    }


    @Override
    public boolean getBoolean(String sKey) {
        return false;
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

public class ReportStaticConfig implements IExperimentProtocol {

    final private View mView;
    
    final private TextView mSbrStaticHeader;
    
    final private RangeSeekBar mSbrStaticFirst;
    final private RangeSeekBar mSbrStaticSecond;
    final private RangeSeekBar mSbrStaticThird;
    
    final private EditText mEditStaticFirstFname;
    final private EditText mEditStaticSecondFname;
    final private EditText mEditStaticThirdFname;

    private ReportStatic mReport;

    static public ReportStatic cmdJSONparse(String cmd) {
        ReportStatic result = new ReportStatic();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportStatic.NAME);
            JSONArray jArr = jObj.getJSONArray(ReportStatic.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[i].systolic = obj.getInt(ReportStatic.NAME_SYS);
                result.values[i].diastolic = obj.getInt(ReportStatic.NAME_DIA);
            }

            jArr = jObj.getJSONArray(ReportStatic.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[i] = obj.getString(ReportStatic.NAME_FEXT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ReportStaticConfig(View view) {
        mView = view;

        mSbrStaticHeader = (TextView) mView.findViewById(R.id.seekBarStaticHeader);
        
        mSbrStaticFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarStaticFirst);
        mSbrStaticSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarStaticSecond);
        mSbrStaticThird = (RangeSeekBar) mView.findViewById(R.id.seekBarStaticThird);
        
        mEditStaticFirstFname = (EditText) mView.findViewById(R.id.editStaticFirstFname);
        mEditStaticSecondFname = (EditText) mView.findViewById(R.id.editStaticSecondFname);
        mEditStaticThirdFname = (EditText) mView.findViewById(R.id.editStaticThirdFname);

        mReport = new ReportStatic();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
    
        mSbrStaticHeader.setVisibility(mode);
        mSbrStaticFirst.setVisibility(mode);
        mSbrStaticSecond.setVisibility(mode);
        mSbrStaticThird.setVisibility(mode);

        mEditStaticFirstFname.setVisibility(mode);
        mEditStaticSecondFname.setVisibility(mode);
        mEditStaticThirdFname.setVisibility(mode);
        mEditStaticFirstFname.setHint(R.string.exp_fname_hint);
        mEditStaticSecondFname.setHint(R.string.exp_fname_hint);
        mEditStaticThirdFname.setHint(R.string.exp_fname_hint);
        mEditStaticFirstFname.setTextSize(14);
        mEditStaticSecondFname.setTextSize(14);
        mEditStaticThirdFname.setTextSize(14);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.values[0].systolic = mSbrStaticFirst.getSelectedMaxValue().intValue();
        mReport.values[0].diastolic= mSbrStaticFirst.getSelectedMinValue().intValue();
        mReport.values[1].systolic = mSbrStaticSecond.getSelectedMaxValue().intValue();
        mReport.values[1].diastolic= mSbrStaticSecond.getSelectedMinValue().intValue();
        mReport.values[2].systolic = mSbrStaticThird.getSelectedMaxValue().intValue();
        mReport.values[2].diastolic= mSbrStaticThird.getSelectedMinValue().intValue();

        mReport.fnames[0] = mEditStaticFirstFname.getText().toString();
        mReport.fnames[1] = mEditStaticSecondFname.getText().toString();
        mReport.fnames[2] = mEditStaticThirdFname.getText().toString();
        try {
            JSONArray jsonArr = new JSONArray();
            for (BloodPressure bp : mReport.values ) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportStatic.NAME_SYS, bp.systolic);
                bpObj.put(ReportStatic.NAME_DIA, bp.diastolic);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportStatic.NAME_VALS, jsonArr);

            jsonArr = new JSONArray();
            for (String st : mReport.fnames) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportStatic.NAME_FEXT, st);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportStatic.NAME_FNAMES, jsonArr);

            jsonWrapper.put(ReportStatic.NAME, jsonObj);
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

        mSbrStaticFirst.setSelectedMaxValue(mReport.values[0].systolic);
        mSbrStaticFirst.setSelectedMinValue(mReport.values[0].diastolic);
        mSbrStaticSecond.setSelectedMaxValue(mReport.values[1].systolic);
        mSbrStaticSecond.setSelectedMinValue(mReport.values[1].diastolic);
        mSbrStaticThird.setSelectedMaxValue(mReport.values[2].systolic);
        mSbrStaticThird.setSelectedMinValue(mReport.values[2].diastolic);

        mEditStaticFirstFname.setText(mReport.fnames[0]);
        mEditStaticSecondFname.setText(mReport.fnames[1]);
        mEditStaticThirdFname.setText(mReport.fnames[2]);
    }
}
