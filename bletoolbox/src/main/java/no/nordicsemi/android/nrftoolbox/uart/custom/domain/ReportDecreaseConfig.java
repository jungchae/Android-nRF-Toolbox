package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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
    public static final String NAME_TEMPUNIT = "\u2109";
    public static final String NAME_UNIT = "mmHg";
    public static final int TRIAL_COUNT = 7;
    public static final int SESSION_CNT = 2;
    public static final String[] NAME_SESSION = {"s1.", "s2."};
    public int[] temperature = new int[SESSION_CNT];
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT*SESSION_CNT];
    public String[] fnames = new String[TRIAL_COUNT*SESSION_CNT];
    ReportDecrease() {
        for (int i = 0; i < TRIAL_COUNT*SESSION_CNT; i++) {
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

        int trialId = 0;
        try {
            trialId = Integer.parseInt(sKey.substring(sKey.length()-1)) - 1;
        } catch (NumberFormatException e) {

        }

        if (trialId < 1 || trialId > TRIAL_COUNT ) return ret;

        if(sKey.equals(NAME_SESSION[0]+NAME_SYS+"[1-7]")) {
            ret = values[trialId-1].systolic;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_SYS+"[1-7]")) {
            ret = values[TRIAL_COUNT+trialId-1].systolic;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_DIA+"[1-7]")) {
            ret = values[trialId-1].diastolic;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_DIA+"[1-7]")) {
            ret = values[TRIAL_COUNT+trialId-1].diastolic;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_TEMP)) {
            ret = temperature[0];
        } else if(sKey.equals(NAME_SESSION[1]+NAME_TEMP)) {
            ret = temperature[1];
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
            default:
        }

        int trialId = 0;
        try {
            trialId = Integer.parseInt(sKey.substring(sKey.length()-1)) - 1;
        } catch (NumberFormatException e) {

        }

        if (trialId < 1 || trialId > TRIAL_COUNT ) return ret;

        if(sKey.equals(NAME_SESSION[0]+NAME_SYS+"[1-7]")) {
            ret = values[trialId-1].systolic + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_DIA+"[1-7]")) {
            ret = values[trialId-1].diastolic + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_SYS+"[1-7]")) {
            ret = values[TRIAL_COUNT+trialId-1].systolic + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_DIA+"[1-7]")) {
            ret = values[TRIAL_COUNT+trialId-1].diastolic+ NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_TEMP)) {
            ret = temperature[0] + NAME_TEMPUNIT;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_TEMP)) {
            ret = temperature[1] + NAME_TEMPUNIT;
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
    public static NumberPicker NPSESSION;

    final private View mView;

    final private TextView mSbrDecreaseTemperatureHeader;
    final private TextView mSbrDecreaseHeader;

    final private SeekBar mSbrDecreaseSession;
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

        // { name :
        //      {
        //          s1 : { temperature : 0, values : [{},{},{},{},{},{},{}], fnames:[{},{},{},{},{},{},{}]] },
        //          s2:  { temperature : 0, values : [{},{},{},{},{},{},{}], fnames:[{},{},{},{},{},{},{}]] }
        //      }
        // }

        ReportDecrease result = new ReportDecrease();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportDecrease.NAME);
            JSONObject s1Obj = jObj.getJSONObject(ReportDecrease.NAME_SESSION[0]);
            JSONObject s2Obj = jObj.getJSONObject(ReportDecrease.NAME_SESSION[1]);

            result.temperature[0] = s1Obj.getInt(ReportDecrease.NAME_TEMP);
            result.temperature[1] = s2Obj.getInt(ReportDecrease.NAME_TEMP);
            // Session 1
            JSONArray jArr = s1Obj.getJSONArray(ReportDecrease.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[i].systolic = obj.getInt(ReportDecrease.NAME_SYS);
                result.values[i].diastolic = obj.getInt(ReportDecrease.NAME_DIA);
            }

            jArr = s1Obj.getJSONArray(ReportDecrease.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[i] = obj.getString(ReportDecrease.NAME_FEXT);
            }
            // Session 2
            jArr = s2Obj.getJSONArray(ReportDecrease.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[ReportDecrease.TRIAL_COUNT+i].systolic = obj.getInt(ReportDecrease.NAME_SYS);
                result.values[ReportDecrease.TRIAL_COUNT+i].diastolic = obj.getInt(ReportDecrease.NAME_DIA);
            }

            jArr = s2Obj.getJSONArray(ReportDecrease.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[ReportDecrease.TRIAL_COUNT+i] = obj.getString(ReportDecrease.NAME_FEXT);
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

        mSbrDecreaseSession = (SeekBar) mView.findViewById(R.id.seekBarDecreaseSession);
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

        if(NPSESSION != null && NPSESSION.getValue() != 0)
            mSbrDecreaseSession.setProgress(NPSESSION.getValue()-1);

        mSbrDecreaseSession.setVisibility(mode);
        mSbrDecreaseSession.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setValueIntoView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

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

        String hint = mView.getResources().getString(R.string.exp_fname_hint);
        mEditDecreaseFirstFname.setHint("1st " + hint);
        mEditDecreaseSecondFname.setHint("2nd " + hint);
        mEditDecreaseThirdFname.setHint("3rd " + hint);
        mEditDecreaseFourthFname.setHint("4th " + hint);
        mEditDecreaseFifthFname.setHint("5th " + hint);
        mEditDecreaseSixthFname.setHint("6th " + hint);
        mEditDecreaseSeventhFname.setHint("7th " + hint);
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
        // { name :
        //      {
        //          s1 : { temperature : 0, values : [{},{},{},{},{},{},{}], fnames:[{},{},{},{},{},{},{}]] }
        //          s2:  { temperature : 0, values : [{},{},{},{},{},{},{}], fnames:[{},{},{},{},{},{},{}]] }
        //      }
        // }
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        JSONObject s1Obj = new JSONObject();
        JSONObject s2Obj = new JSONObject();

        // Update value in current view
        getValueFromView();

        try {
            // Session 1
            JSONArray jsonArrVals = new JSONArray();
            JSONArray jsonArrLogs = new JSONArray();

            for (int i = 0 ; i < ReportDecrease.TRIAL_COUNT ; i++ ) {

                JSONObject valObj = new JSONObject();
                BloodPressure bp = mReport.values[i];
                valObj.put(ReportDecrease.NAME_SYS, bp.systolic);
                valObj.put(ReportDecrease.NAME_DIA, bp.diastolic);
                jsonArrVals.put(valObj);

                JSONObject logObj = new JSONObject();
                String st = mReport.fnames[i];
                logObj.put(ReportDecrease.NAME_FEXT, st);
                jsonArrLogs.put(logObj);

            }
            s1Obj.put(ReportDecrease.NAME_TEMP, mReport.temperature[0]);
            s1Obj.put(ReportDecrease.NAME_VALS, jsonArrVals);
            s1Obj.put(ReportDecrease.NAME_FNAMES, jsonArrLogs);
            jsonObj.put(ReportDecrease.NAME_SESSION[0], s1Obj);
            // Session 2
            jsonArrVals = new JSONArray();
            jsonArrLogs = new JSONArray();
            for (int i = ReportDecrease.TRIAL_COUNT ; i < ReportDecrease.TRIAL_COUNT*ReportDecrease.SESSION_CNT ; i++ ) {

                JSONObject valObj = new JSONObject();
                BloodPressure bp = mReport.values[i];
                valObj.put(ReportDecrease.NAME_SYS, bp.systolic);
                valObj.put(ReportDecrease.NAME_DIA, bp.diastolic);
                jsonArrVals.put(valObj);

                JSONObject logObj = new JSONObject();
                String st = mReport.fnames[i];
                logObj.put(ReportDecrease.NAME_FEXT, st);
                jsonArrLogs.put(logObj);

            }
            s2Obj.put(ReportDecrease.NAME_TEMP, mReport.temperature[1]);
            s2Obj.put(ReportDecrease.NAME_VALS, jsonArrVals);
            s2Obj.put(ReportDecrease.NAME_FNAMES, jsonArrLogs);
            jsonObj.put(ReportDecrease.NAME_SESSION[1], s2Obj);

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

        setValueIntoView();
    }

    private void setValueIntoView() {
        int iSbr = mSbrDecreaseSession.getProgress();
        int iStart = iSbr * ReportDecrease.TRIAL_COUNT;

        String header = mView.getResources().getString(R.string.exp_seekBar_decrease_temperature_header);
        mSbrDecreaseTemperatureHeader.setText(header + " " + (iSbr+1));
        header = mView.getResources().getString(R.string.exp_seekBar_decrease_header);
        mSbrDecreaseHeader.setText(header + " " + (iSbr+1));

        mSbrDecreaseTemperature.setSelectedMaxValue(mReport.temperature[iSbr]);
        mSbrDecreaseFirst.setSelectedMaxValue(mReport.values[iStart + 0].systolic);
        mSbrDecreaseFirst.setSelectedMinValue(mReport.values[iStart + 0].diastolic);
        mSbrDecreaseSecond.setSelectedMaxValue(mReport.values[iStart + 1].systolic);
        mSbrDecreaseSecond.setSelectedMinValue(mReport.values[iStart + 1].diastolic);
        mSbrDecreaseThird.setSelectedMaxValue(mReport.values[iStart + 2].systolic);
        mSbrDecreaseThird.setSelectedMinValue(mReport.values[iStart + 2].diastolic);
        mSbrDecreaseFourth.setSelectedMaxValue(mReport.values[iStart + 3].systolic);
        mSbrDecreaseFourth.setSelectedMinValue(mReport.values[iStart + 3].diastolic);
        mSbrDecreaseFifth.setSelectedMaxValue(mReport.values[iStart + 4].systolic);
        mSbrDecreaseFifth.setSelectedMinValue(mReport.values[iStart + 4].diastolic);
        mSbrDecreaseSixth.setSelectedMaxValue(mReport.values[iStart + 5].systolic);
        mSbrDecreaseSixth.setSelectedMinValue(mReport.values[iStart + 5].diastolic);
        mSbrDecreaseSeventh.setSelectedMaxValue(mReport.values[iStart + 6].systolic);
        mSbrDecreaseSeventh.setSelectedMinValue(mReport.values[iStart + 6].diastolic);

        mEditDecreaseFirstFname.setText(mReport.fnames[iStart + 0]);
        mEditDecreaseSecondFname.setText(mReport.fnames[iStart + 1]);
        mEditDecreaseThirdFname.setText(mReport.fnames[iStart + 2]);
        mEditDecreaseFourthFname.setText(mReport.fnames[iStart + 3]);
        mEditDecreaseFifthFname.setText(mReport.fnames[iStart + 4]);
        mEditDecreaseSixthFname.setText(mReport.fnames[iStart + 5]);
        mEditDecreaseSeventhFname.setText(mReport.fnames[iStart + 6]);
    }


    private void getValueFromView() {
        int iSbr = mSbrDecreaseSession.getProgress();
        int iStart = iSbr * ReportDecrease.TRIAL_COUNT;

        mReport.temperature[iSbr] = mSbrDecreaseTemperature.getSelectedMaxValue().intValue();
        mReport.values[iStart + 0].systolic = mSbrDecreaseFirst.getSelectedMaxValue().intValue();
        mReport.values[iStart + 0].diastolic= mSbrDecreaseFirst.getSelectedMinValue().intValue();
        mReport.values[iStart + 1].systolic = mSbrDecreaseSecond.getSelectedMaxValue().intValue();
        mReport.values[iStart + 1].diastolic= mSbrDecreaseSecond.getSelectedMinValue().intValue();
        mReport.values[iStart + 2].systolic = mSbrDecreaseThird.getSelectedMaxValue().intValue();
        mReport.values[iStart + 2].diastolic= mSbrDecreaseThird.getSelectedMinValue().intValue();
        mReport.values[iStart + 3].systolic = mSbrDecreaseFourth.getSelectedMaxValue().intValue();
        mReport.values[iStart + 3].diastolic= mSbrDecreaseFourth.getSelectedMinValue().intValue();
        mReport.values[iStart + 4].systolic = mSbrDecreaseFifth.getSelectedMaxValue().intValue();
        mReport.values[iStart + 4].diastolic= mSbrDecreaseFifth.getSelectedMinValue().intValue();
        mReport.values[iStart + 5].systolic = mSbrDecreaseSixth.getSelectedMaxValue().intValue();
        mReport.values[iStart + 5].diastolic= mSbrDecreaseSixth.getSelectedMinValue().intValue();
        mReport.values[iStart + 6].systolic = mSbrDecreaseSeventh.getSelectedMaxValue().intValue();
        mReport.values[iStart + 6].diastolic= mSbrDecreaseSeventh.getSelectedMinValue().intValue();

        mReport.fnames[iStart + 0] = mEditDecreaseFirstFname.getText().toString();
        mReport.fnames[iStart + 1] = mEditDecreaseSecondFname.getText().toString();
        mReport.fnames[iStart + 2] = mEditDecreaseThirdFname.getText().toString();
        mReport.fnames[iStart + 3] = mEditDecreaseFourthFname.getText().toString();
        mReport.fnames[iStart + 4] = mEditDecreaseFifthFname.getText().toString();
        mReport.fnames[iStart + 5] = mEditDecreaseSixthFname.getText().toString();
        mReport.fnames[iStart + 6] = mEditDecreaseSeventhFname.getText().toString();
    }
}
