package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.CheckBox;
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
class ReportOrthostatic implements IReport{
    public static final String NAME = "ReportOrthostatic";
    public static final String NAME_FELLDIZZNESS = "feeldizzness";
    public static final String NAME_SYS = "systolic";
    public static final String NAME_DIA = "diastolic";
    public static final String NAME_VALS = "values";
    public static final String NAME_FNAMES = "fnames";
    public static final String NAME_FEXT = "bin";
    public static final String NAME_UNIT = "mmHg";
    private static final int TRIAL_COUNT = 3;
    public boolean feeldizzness;
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT];
    public String[] fnames = new String[TRIAL_COUNT];
    ReportOrthostatic() {
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
        boolean ret = false;
        switch(sKey) {
            case NAME_FELLDIZZNESS:
                ret = feeldizzness;
                break;
            default:
        }
        return ret;
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
    public String[] getRegisteredFileInfo() {
        return fnames;
    }

    @Override
    public BloodPressure[] getBloodPressure() {
        return new BloodPressure[0];
    }
}
public class ReportOrthostaticConfig implements IExperimentProtocol {

    final private View mView;

    final private CheckBox mCbDizzness;
    
    final private TextView mSbrOrthoHeader;
    
    final private RangeSeekBar mSbrOrthoFirst;
    final private RangeSeekBar mSbrOrthoSecond;
    final private RangeSeekBar mSbrOrthoThird;

    final private EditText mEditOrthostaticFirstFname;
    final private EditText mEditOrthostaticSecondFname;
    final private EditText mEditOrthostaticThirdFname;

    private ReportOrthostatic mReport;

    static public ReportOrthostatic cmdJSONparse(String cmd) {

        ReportOrthostatic result = new ReportOrthostatic();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportOrthostatic.NAME);
            result.feeldizzness = jObj.getBoolean(ReportOrthostatic.NAME_FELLDIZZNESS);

            JSONArray jArr = jObj.getJSONArray(ReportOrthostatic.NAME_VALS);

            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[i].systolic = obj.getInt(ReportOrthostatic.NAME_SYS);
                result.values[i].diastolic = obj.getInt(ReportOrthostatic.NAME_DIA);
            }

            jArr = jObj.getJSONArray(ReportOrthostatic.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[i] = obj.getString(ReportOrthostatic.NAME_FEXT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ReportOrthostaticConfig(View view) {
        mView = view;
        
        mCbDizzness = (CheckBox) mView.findViewById(R.id.checkBoxDizzness);
        
        mSbrOrthoHeader = (TextView) mView.findViewById(R.id.seekBarOrthoHeader);
    
        mSbrOrthoFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarOrthoFirst);
        mSbrOrthoSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarOrthoSecond);
        mSbrOrthoThird = (RangeSeekBar) mView.findViewById(R.id.seekBarOrthoThird);

        mEditOrthostaticFirstFname = (EditText) mView.findViewById(R.id.editOrthostaticFirstFname);
        mEditOrthostaticSecondFname = (EditText) mView.findViewById(R.id.editOrthostaticSecondFname);
        mEditOrthostaticThirdFname = (EditText) mView.findViewById(R.id.editOrthostaticThirdFname);

        mReport = new ReportOrthostatic();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
    
        mCbDizzness.setVisibility(mode);
    
        mSbrOrthoHeader.setVisibility(mode);
    
        mSbrOrthoFirst.setVisibility(mode);
        mSbrOrthoSecond.setVisibility(mode);
        mSbrOrthoThird.setVisibility(mode);

        mEditOrthostaticFirstFname.setVisibility(mode);
        mEditOrthostaticSecondFname.setVisibility(mode);
        mEditOrthostaticThirdFname.setVisibility(mode);
        mEditOrthostaticFirstFname.setHint(R.string.exp_fname_hint);
        mEditOrthostaticSecondFname.setHint(R.string.exp_fname_hint);
        mEditOrthostaticThirdFname.setHint(R.string.exp_fname_hint);
        mEditOrthostaticFirstFname.setTextSize(14);
        mEditOrthostaticSecondFname.setTextSize(14);
        mEditOrthostaticThirdFname.setTextSize(14);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.feeldizzness = mCbDizzness.isChecked();
    
        mReport.values[0].systolic = mSbrOrthoFirst.getSelectedMaxValue().intValue();
        mReport.values[0].diastolic= mSbrOrthoFirst.getSelectedMinValue().intValue();
        mReport.values[1].systolic = mSbrOrthoSecond.getSelectedMaxValue().intValue();
        mReport.values[1].diastolic= mSbrOrthoSecond.getSelectedMinValue().intValue();
        mReport.values[2].systolic = mSbrOrthoThird.getSelectedMaxValue().intValue();
        mReport.values[2].diastolic= mSbrOrthoThird.getSelectedMinValue().intValue();

        mReport.fnames[0] = mEditOrthostaticFirstFname.getText().toString();
        mReport.fnames[1] = mEditOrthostaticSecondFname.getText().toString();
        mReport.fnames[2] = mEditOrthostaticThirdFname.getText().toString();
        try {
            jsonObj.put(ReportOrthostatic.NAME_FELLDIZZNESS, mReport.feeldizzness);

            JSONArray jsonArr = new JSONArray();
            for (BloodPressure bp : mReport.values) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportOrthostatic.NAME_SYS, bp.systolic);
                bpObj.put(ReportOrthostatic.NAME_DIA, bp.diastolic);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportOrthostatic.NAME_VALS, jsonArr);

            jsonArr = new JSONArray();
            for (String st : mReport.fnames) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportOrthostatic.NAME_FEXT, st);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportOrthostatic.NAME_FNAMES, jsonArr);

            jsonWrapper.put(ReportOrthostatic.NAME, jsonObj);
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

        mCbDizzness.setChecked(mReport.feeldizzness);

        mSbrOrthoFirst.setSelectedMaxValue(mReport.values[0].systolic);
        mSbrOrthoFirst.setSelectedMinValue(mReport.values[0].diastolic);
        mSbrOrthoSecond.setSelectedMaxValue(mReport.values[1].systolic);
        mSbrOrthoSecond.setSelectedMinValue(mReport.values[1].diastolic);
        mSbrOrthoThird.setSelectedMaxValue(mReport.values[2].systolic);
        mSbrOrthoThird.setSelectedMinValue(mReport.values[2].diastolic);

        mEditOrthostaticFirstFname.setText(mReport.fnames[0]);
        mEditOrthostaticSecondFname.setText(mReport.fnames[1]);
        mEditOrthostaticThirdFname.setText(mReport.fnames[2]);
    }
}
