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
    public static final String NAME_LOADUNIT = "kg";
    public static final String NAME_UNIT = "mmHg";
    public static final int TRIAL_COUNT = 4;
    public static final int SESSION_CNT = 2;
    public static final String[] NAME_SESSION = {"s1.", "s2."};
    public int[] loadfirst = new int[SESSION_CNT];
    public int[] loadsecond = new int[SESSION_CNT];
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT*SESSION_CNT];
    public String[] fnames = new String[TRIAL_COUNT*SESSION_CNT];
    ReportIncrease() {
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

        if(sKey.equals(NAME_LOADFIRST+NAME_SESSION[0])) {
            ret = loadfirst[0];
        } else if(sKey.equals(NAME_LOADFIRST+NAME_SESSION[1])) {
            ret = loadfirst[1];
        } else if(sKey.equals(NAME_LOADSECOND+NAME_SESSION[0])) {
            ret = loadsecond[0];
        } else if(sKey.equals(NAME_LOADSECOND+NAME_SESSION[1])) {
            ret = loadsecond[1];
        } else if(sKey.equals(NAME_SESSION[0]+NAME_SYS+"[1-4]")) {
            ret = values[0+trialId].systolic;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_SYS+"[1-4]")) {
            ret = values[TRIAL_COUNT+trialId].systolic;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_DIA+"[1-4]")) {
            ret = values[0+trialId].diastolic;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_DIA+"[1-4]")) {
            ret = values[TRIAL_COUNT+trialId].diastolic;
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

        if(sKey.equals(NAME_LOADFIRST+NAME_SESSION[0])) {
            ret = loadfirst[0] + NAME_LOADUNIT;
        } else if(sKey.equals(NAME_LOADFIRST+NAME_SESSION[1])) {
            ret = loadfirst[1] + NAME_LOADUNIT;
        } else if(sKey.equals(NAME_LOADSECOND+NAME_SESSION[0])) {
            ret = loadsecond[0] + NAME_LOADUNIT;
        } else if(sKey.equals(NAME_LOADSECOND+NAME_SESSION[1])) {
            ret = loadsecond[1] + NAME_LOADUNIT;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_SYS+"[1-4]")) {
            ret = values[trialId-1].systolic + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_DIA+"[1-4]")) {
            ret = values[trialId-1].diastolic + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_SYS+"[1-4]")) {
            ret = values[TRIAL_COUNT+trialId-1].systolic + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_DIA+"[1-4]")) {
            ret = values[TRIAL_COUNT+trialId-1].diastolic + NAME_UNIT;
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
    public static NumberPicker NPSESSION;

    final private View mView;

    final private TextView mSbrIncreaseLoadHeader;
    final private TextView mSbrIncreaseHeader;

    final private SeekBar mSbrIncreaseSession;
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

        // { name :
        //      {
        //          s1 : { loadfirst : 0, loadsecond : 0, values : [{},{},{},{}], fnames:[{},{},{},{}]] }
        //          s2:  { loadfirst : 0, loadsecond : 0, values : [{},{},{},{}], fnames:[{},{},{},{}]] }
        //      }
        // }

        ReportIncrease result = new ReportIncrease();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportIncrease.NAME);

            JSONObject s1Obj = jObj.getJSONObject(ReportIncrease.NAME_SESSION[0]);
            JSONObject s2Obj = jObj.getJSONObject(ReportIncrease.NAME_SESSION[1]);

            result.loadfirst[0] = s1Obj.getInt(ReportIncrease.NAME_LOADFIRST);
            result.loadsecond[0] = s1Obj.getInt(ReportIncrease.NAME_LOADSECOND);
            result.loadfirst[1] = s2Obj.getInt(ReportIncrease.NAME_LOADFIRST);
            result.loadsecond[1] = s2Obj.getInt(ReportIncrease.NAME_LOADSECOND);

            // Session 1
            JSONArray jArr = s1Obj.getJSONArray(ReportIncrease.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[i].systolic = obj.getInt(ReportIncrease.NAME_SYS);
                result.values[i].diastolic = obj.getInt(ReportIncrease.NAME_DIA);
            }

            jArr = s1Obj.getJSONArray(ReportIncrease.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[i] = obj.getString(ReportIncrease.NAME_FEXT);
            }
            // Session 2
            jArr = s2Obj.getJSONArray(ReportIncrease.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[ReportIncrease.TRIAL_COUNT+i].systolic = obj.getInt(ReportIncrease.NAME_SYS);
                result.values[ReportIncrease.TRIAL_COUNT+i].diastolic = obj.getInt(ReportIncrease.NAME_DIA);
            }

            jArr = s2Obj.getJSONArray(ReportIncrease.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[ReportIncrease.TRIAL_COUNT+i] = obj.getString(ReportIncrease.NAME_FEXT);
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

        mSbrIncreaseSession = (SeekBar) mView.findViewById(R.id.seekBarStaticSession);
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

        if(NPSESSION != null && NPSESSION.getValue() != 0)
            mSbrIncreaseSession.setProgress(NPSESSION.getValue()-1);

        mSbrIncreaseSession.setVisibility(mode);
        mSbrIncreaseSession.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setValueIntoView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

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

        String hint = mView.getResources().getString(R.string.exp_fname_hint);
        mEditIncreaseFirstFname.setHint("1st " + hint);
        mEditIncreaseSecondFname.setHint("2nd " + hint);
        mEditIncreaseThirdFname.setHint("3rd " + hint);
        mEditIncreaseFourthFname.setHint("4rd " + hint);
        mEditIncreaseFirstFname.setTextSize(14);
        mEditIncreaseSecondFname.setTextSize(14);
        mEditIncreaseThirdFname.setTextSize(14);
        mEditIncreaseFourthFname.setTextSize(14);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        // { name :
        //      {
        //          s1 : { loadfirst : 0, loadsecond : 0, values : [{},{},{},{}], fnames:[{},{},{},{}]] }
        //          s2:  { loadfirst : 0, loadsecond : 0, values : [{},{},{},{}], fnames:[{},{},{},{}]] }
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
            for (int i = 0 ; i < ReportIncrease.TRIAL_COUNT ; i++ ) {

                JSONObject valObj = new JSONObject();
                BloodPressure bp = mReport.values[i];
                valObj.put(ReportIncrease.NAME_SYS, bp.systolic);
                valObj.put(ReportIncrease.NAME_DIA, bp.diastolic);
                jsonArrVals.put(valObj);

                JSONObject logObj = new JSONObject();
                String st = mReport.fnames[i];
                logObj.put(ReportIncrease.NAME_FEXT, st);
                jsonArrLogs.put(logObj);

            }
            s1Obj.put(ReportIncrease.NAME_LOADFIRST, mReport.loadfirst[0]);
            s1Obj.put(ReportIncrease.NAME_LOADSECOND, mReport.loadsecond[0]);
            s1Obj.put(ReportIncrease.NAME_VALS, jsonArrVals);
            s1Obj.put(ReportIncrease.NAME_FNAMES, jsonArrLogs);
            jsonObj.put(ReportIncrease.NAME_SESSION[0], s1Obj);

            // Session 2
            jsonArrVals = new JSONArray();
            jsonArrLogs = new JSONArray();
            for (int i = ReportIncrease.TRIAL_COUNT ; i < ReportIncrease.TRIAL_COUNT*ReportIncrease.SESSION_CNT ; i++ ) {

                JSONObject valObj = new JSONObject();
                BloodPressure bp = mReport.values[i];
                valObj.put(ReportIncrease.NAME_SYS, bp.systolic);
                valObj.put(ReportIncrease.NAME_DIA, bp.diastolic);
                jsonArrVals.put(valObj);

                JSONObject logObj = new JSONObject();
                String st = mReport.fnames[i];
                logObj.put(ReportIncrease.NAME_FEXT, st);
                jsonArrLogs.put(logObj);

            }
            s2Obj.put(ReportIncrease.NAME_LOADFIRST, mReport.loadfirst[1]);
            s2Obj.put(ReportIncrease.NAME_LOADSECOND, mReport.loadsecond[1]);
            s2Obj.put(ReportIncrease.NAME_VALS, jsonArrVals);
            s2Obj.put(ReportIncrease.NAME_FNAMES, jsonArrLogs);
            jsonObj.put(ReportIncrease.NAME_SESSION[1], s2Obj);

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

        setValueIntoView();
    }

    private void setValueIntoView() {
        int iSbr = mSbrIncreaseSession.getProgress();
        int iStart = iSbr * ReportIncrease.TRIAL_COUNT;

        String header = mView.getResources().getString(R.string.exp_seekBar_increase_load_header);
        mSbrIncreaseLoadHeader.setText(header + " " + (iSbr+1));
        header = mView.getResources().getString(R.string.exp_seekBar_increase_header);
        mSbrIncreaseHeader.setText(header + " " + (iSbr+1));

        mSbrIncreaseLoadFirst.setSelectedMaxValue(mReport.loadfirst[iSbr]);
        mSbrIncreaseLoadSecond.setSelectedMaxValue(mReport.loadsecond[iSbr]);

        mSbrIncreaseFirst.setSelectedMaxValue(mReport.values[iStart + 0].systolic);
        mSbrIncreaseFirst.setSelectedMinValue(mReport.values[iStart + 0].diastolic);
        mSbrIncreaseSecond.setSelectedMaxValue(mReport.values[iStart + 1].systolic);
        mSbrIncreaseSecond.setSelectedMinValue(mReport.values[iStart + 1].diastolic);
        mSbrIncreaseThird.setSelectedMaxValue(mReport.values[iStart + 2].systolic);
        mSbrIncreaseThird.setSelectedMinValue(mReport.values[iStart + 2].diastolic);
        mSbrIncreaseFourth.setSelectedMaxValue(mReport.values[iStart + 3].systolic);
        mSbrIncreaseFourth.setSelectedMinValue(mReport.values[iStart + 3].diastolic);

        mEditIncreaseFirstFname.setText(mReport.fnames[iStart + 0]);
        mEditIncreaseSecondFname.setText(mReport.fnames[iStart + 1]);
        mEditIncreaseThirdFname.setText(mReport.fnames[iStart + 2]);
        mEditIncreaseFourthFname.setText(mReport.fnames[iStart + 3]);
    }


    private void getValueFromView() {
        int iSbr = mSbrIncreaseSession.getProgress();
        int iStart = iSbr * ReportIncrease.TRIAL_COUNT;

        mReport.loadfirst[iSbr] = mSbrIncreaseLoadFirst.getSelectedMaxValue().intValue();
        mReport.loadsecond[iSbr] = mSbrIncreaseLoadSecond.getSelectedMaxValue().intValue();

        mReport.values[iStart + 0].systolic = mSbrIncreaseFirst.getSelectedMaxValue().intValue();
        mReport.values[iStart + 0].diastolic= mSbrIncreaseFirst.getSelectedMinValue().intValue();
        mReport.values[iStart + 1].systolic = mSbrIncreaseSecond.getSelectedMaxValue().intValue();
        mReport.values[iStart + 1].diastolic= mSbrIncreaseSecond.getSelectedMinValue().intValue();
        mReport.values[iStart + 2].systolic = mSbrIncreaseThird.getSelectedMaxValue().intValue();
        mReport.values[iStart + 2].diastolic= mSbrIncreaseThird.getSelectedMinValue().intValue();
        mReport.values[iStart + 3].systolic = mSbrIncreaseFourth.getSelectedMaxValue().intValue();
        mReport.values[iStart + 3].diastolic= mSbrIncreaseFourth.getSelectedMinValue().intValue();

        mReport.fnames[iStart + 0] = mEditIncreaseFirstFname.getText().toString();
        mReport.fnames[iStart + 1] = mEditIncreaseSecondFname.getText().toString();
        mReport.fnames[iStart + 2] = mEditIncreaseThirdFname.getText().toString();
        mReport.fnames[iStart + 3] = mEditIncreaseFourthFname.getText().toString();
    }
}
