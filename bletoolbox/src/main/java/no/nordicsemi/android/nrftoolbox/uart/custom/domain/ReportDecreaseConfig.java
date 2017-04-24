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

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by jungchae on 17. 4. 19.
 */

class ReportDecrease implements IReport{
    public static final String NAME = "ReportDecrease";
    public static final String NAME_TEMP = "temperature";
    public static final String NAME_SYS = "systolic";
    public static final String NAME_DIA = "diastolic";
    public static final String NAME_VALS = "values";
    public static final String NAME_FNAMES = "fnames";
    public static final String NAME_FEXT = "bin";
    public static final String NAME_UNIT = "mmHg";
    private static final int TRIAL_COUNT = 7;
    public int temperature;
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT];
    public String[] fnames = new String[TRIAL_COUNT];
    ReportDecrease() {
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
            case NAME_TEMP:
                return temperature;
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
            case NAME_TEMP:
                ret = temperature + "\u2109";
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

public class ReportDecreaseConfig implements IExperimentProtocol {

    final private View mView;

    final private TextView mSbrDecreaseTemperatureHeader;
    final private TextView mSbrDecreaseHeader;
    
    final private RangeSeekBar mSbrDecreaseTemperature;
    final private RangeSeekBar mSbrDecreaseFirst;
    final private RangeSeekBar mSbrDecreaseSecond;
    final private RangeSeekBar mSbrDecreaseThird;
    final private RangeSeekBar mSbrDecreaseFourth;
    final private RangeSeekBar mSbrDecreaseFifth;
    final private RangeSeekBar mSbrDecreaseSixth;
    final private RangeSeekBar mSbrDecreaseSeventh;

    final private EditText mEditDecreaseFirstFname;
    final private EditText mEditDecreaseSecondFname;
    final private EditText mEditDecreaseThirdFname;
    final private EditText mEditDecreaseFourthFname;
    final private EditText mEditDecreaseFifthFname;
    final private EditText mEditDecreaseSixthFname;
    final private EditText mEditDecreaseSeventhFname;

    private ReportDecrease mReport;

    static public ReportDecrease cmdJSONparse(String cmd) {
        ReportDecrease result = new ReportDecrease();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportDecrease.NAME);
            result.temperature = jObj.getInt(ReportDecrease.NAME_TEMP);
            JSONArray jArr = jObj.getJSONArray(ReportDecrease.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[i].systolic = obj.getInt(ReportDecrease.NAME_SYS);
                result.values[i].diastolic = obj.getInt(ReportDecrease.NAME_DIA);
            }

            jArr = jObj.getJSONArray(ReportDecrease.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[i] = obj.getString(ReportDecrease.NAME_FEXT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ReportDecreaseConfig(View view) {
        mView = view;
        
        mSbrDecreaseTemperatureHeader = (TextView) mView.findViewById(R.id.seekBarDecreaseTemperatureHeader);
        mSbrDecreaseHeader = (TextView) mView.findViewById(R.id.seekBarDecreaseHeader);
        
        mSbrDecreaseTemperature = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseTemperature);
        mSbrDecreaseFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseFirst);
        mSbrDecreaseSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseSecond);
        mSbrDecreaseThird = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseThird);
        mSbrDecreaseFourth = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseFourth);
        mSbrDecreaseFifth = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseFifth);
        mSbrDecreaseSixth = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseSixth);
        mSbrDecreaseSeventh = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseSeventh);

        mEditDecreaseFirstFname = (EditText) mView.findViewById(R.id.editDecreaseFirstFname);
        mEditDecreaseSecondFname = (EditText) mView.findViewById(R.id.editDecreaseSecondFname);
        mEditDecreaseThirdFname = (EditText) mView.findViewById(R.id.editDecreaseThirdFname);
        mEditDecreaseFourthFname = (EditText) mView.findViewById(R.id.editDecreaseFourthFname);
        mEditDecreaseFifthFname = (EditText) mView.findViewById(R.id.editDecreaseFifthFname);
        mEditDecreaseSixthFname = (EditText) mView.findViewById(R.id.editDecreaseSixthFname);
        mEditDecreaseSeventhFname = (EditText) mView.findViewById(R.id.editDecreaseSeventhFname);

        mReport = new ReportDecrease();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrDecreaseTemperatureHeader.setVisibility(mode);
        mSbrDecreaseTemperature.setVisibility(mode);
        mSbrDecreaseHeader.setVisibility(mode);
        mSbrDecreaseFirst.setVisibility(mode);
        mSbrDecreaseSecond.setVisibility(mode);
        mSbrDecreaseThird.setVisibility(mode);
        mSbrDecreaseFourth.setVisibility(mode);
        mSbrDecreaseFifth.setVisibility(mode);
        mSbrDecreaseSixth.setVisibility(mode);
        mSbrDecreaseSeventh.setVisibility(mode);

        mEditDecreaseFirstFname.setVisibility(mode);
        mEditDecreaseSecondFname.setVisibility(mode);
        mEditDecreaseThirdFname.setVisibility(mode);
        mEditDecreaseFourthFname.setVisibility(mode);
        mEditDecreaseFifthFname.setVisibility(mode);
        mEditDecreaseSixthFname.setVisibility(mode);
        mEditDecreaseSeventhFname.setVisibility(mode);
        mEditDecreaseFirstFname.setHint(R.string.exp_fname_hint);
        mEditDecreaseSecondFname.setHint(R.string.exp_fname_hint);
        mEditDecreaseThirdFname.setHint(R.string.exp_fname_hint);
        mEditDecreaseFourthFname.setHint(R.string.exp_fname_hint);
        mEditDecreaseFifthFname.setHint(R.string.exp_fname_hint);
        mEditDecreaseSixthFname.setHint(R.string.exp_fname_hint);
        mEditDecreaseSeventhFname.setHint(R.string.exp_fname_hint);
        mEditDecreaseFirstFname.setTextSize(14);
        mEditDecreaseSecondFname.setTextSize(14);
        mEditDecreaseThirdFname.setTextSize(14);
        mEditDecreaseFourthFname.setTextSize(14);
        mEditDecreaseFifthFname.setTextSize(14);
        mEditDecreaseSixthFname.setTextSize(14);
        mEditDecreaseSeventhFname.setTextSize(14);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.temperature = mSbrDecreaseTemperature.getSelectedMaxValue().intValue();
        mReport.values[0].systolic = mSbrDecreaseFirst.getSelectedMaxValue().intValue();
        mReport.values[0].diastolic= mSbrDecreaseFirst.getSelectedMinValue().intValue();
        mReport.values[1].systolic = mSbrDecreaseSecond.getSelectedMaxValue().intValue();
        mReport.values[1].diastolic= mSbrDecreaseSecond.getSelectedMinValue().intValue();
        mReport.values[2].systolic = mSbrDecreaseThird.getSelectedMaxValue().intValue();
        mReport.values[2].diastolic= mSbrDecreaseThird.getSelectedMinValue().intValue();
        mReport.values[3].systolic = mSbrDecreaseFourth.getSelectedMaxValue().intValue();
        mReport.values[3].diastolic= mSbrDecreaseFourth.getSelectedMinValue().intValue();
        mReport.values[4].systolic = mSbrDecreaseFifth.getSelectedMaxValue().intValue();
        mReport.values[4].diastolic= mSbrDecreaseFifth.getSelectedMinValue().intValue();
        mReport.values[5].systolic = mSbrDecreaseSixth.getSelectedMaxValue().intValue();
        mReport.values[5].diastolic= mSbrDecreaseSixth.getSelectedMinValue().intValue();
        mReport.values[6].systolic = mSbrDecreaseSeventh.getSelectedMaxValue().intValue();
        mReport.values[6].diastolic= mSbrDecreaseSeventh.getSelectedMinValue().intValue();

        mReport.fnames[0] = mEditDecreaseFirstFname.getText().toString();
        mReport.fnames[1] = mEditDecreaseSecondFname.getText().toString();
        mReport.fnames[2] = mEditDecreaseThirdFname.getText().toString();
        mReport.fnames[3] = mEditDecreaseFourthFname.getText().toString();
        mReport.fnames[4] = mEditDecreaseFifthFname.getText().toString();
        mReport.fnames[5] = mEditDecreaseSixthFname.getText().toString();
        mReport.fnames[6] = mEditDecreaseSeventhFname.getText().toString();

        try {
            jsonObj.put(ReportDecrease.NAME_TEMP, mReport.temperature);

            JSONArray jsonArr = new JSONArray();
            for (BloodPressure bp : mReport.values ) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportDecrease.NAME_SYS, bp.systolic);
                bpObj.put(ReportDecrease.NAME_DIA, bp.diastolic);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportStatic.NAME_VALS, jsonArr);

            jsonArr = new JSONArray();
            for (String st : mReport.fnames) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportDecrease.NAME_FEXT, st);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportDecrease.NAME_FNAMES, jsonArr);

            jsonWrapper.put(ReportDecrease.NAME, jsonObj);
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

        mSbrDecreaseTemperature.setSelectedMaxValue(mReport.temperature);
        mSbrDecreaseFirst.setSelectedMaxValue(mReport.values[0].systolic);
        mSbrDecreaseFirst.setSelectedMinValue(mReport.values[0].diastolic);
        mSbrDecreaseSecond.setSelectedMaxValue(mReport.values[1].systolic);
        mSbrDecreaseSecond.setSelectedMinValue(mReport.values[1].diastolic);
        mSbrDecreaseThird.setSelectedMaxValue(mReport.values[2].systolic);
        mSbrDecreaseThird.setSelectedMinValue(mReport.values[2].diastolic);
        mSbrDecreaseFourth.setSelectedMaxValue(mReport.values[3].systolic);
        mSbrDecreaseFourth.setSelectedMinValue(mReport.values[3].diastolic);
        mSbrDecreaseFifth.setSelectedMaxValue(mReport.values[4].systolic);
        mSbrDecreaseFifth.setSelectedMinValue(mReport.values[4].diastolic);
        mSbrDecreaseSixth.setSelectedMaxValue(mReport.values[5].systolic);
        mSbrDecreaseSixth.setSelectedMinValue(mReport.values[5].diastolic);
        mSbrDecreaseSeventh.setSelectedMaxValue(mReport.values[6].systolic);
        mSbrDecreaseSeventh.setSelectedMinValue(mReport.values[6].diastolic);

        mEditDecreaseFirstFname.setText(mReport.fnames[0]);
        mEditDecreaseSecondFname.setText(mReport.fnames[1]);
        mEditDecreaseThirdFname.setText(mReport.fnames[2]);
        mEditDecreaseFourthFname.setText(mReport.fnames[3]);
        mEditDecreaseFifthFname.setText(mReport.fnames[4]);
        mEditDecreaseSixthFname.setText(mReport.fnames[5]);
        mEditDecreaseSeventhFname.setText(mReport.fnames[6]);
    }
}
