package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.content.Context;
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

import no.nordicsemi.android.nrftoolbox.R;

import static java.security.AccessController.getContext;

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
    public static final int TRIAL_COUNT = 3;
    public static final int SESSION_CNT = 2;
    public static final String[] NAME_SESSION = {"s1.", "s2."};
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT*SESSION_CNT];
    public String[] fnames = new String[TRIAL_COUNT*SESSION_CNT];

    ReportStatic() {
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
    public int getInt(String sKey) {
        int ret = -1;

        int trialId = 0;
        try {
            trialId = Integer.parseInt(sKey.substring(sKey.length()-1)) - 1;
        } catch (NumberFormatException e) {

        }

        if (trialId < 1 || trialId > TRIAL_COUNT ) return ret;
        
        if(sKey.equals(NAME_SESSION[0]+NAME_SYS+"[1-3]")) {
            ret = values[trialId-1].systolic;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_DIA+"[1-3]")) {
            ret = values[trialId-1].diastolic;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_SYS+"[1-3]")) {
            ret = values[TRIAL_COUNT+trialId-1].systolic;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_DIA+"[1-3]")) {
            ret = values[TRIAL_COUNT+trialId-1].diastolic;
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
        }

        int trialId = 0;
        try {
            trialId = Integer.parseInt(sKey.substring(sKey.length()-1)) - 1;
        } catch (NumberFormatException e) {

        }

        if (trialId < 1 || trialId > TRIAL_COUNT ) return ret;

        if(sKey.equals(NAME_SESSION[0]+NAME_SYS+"[1-3]")) {
            ret = values[trialId-1].systolic + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_DIA+"[1-3]")) {
            ret = values[trialId-1].diastolic + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_SYS+"[1-3]")) {
            ret = values[TRIAL_COUNT+trialId-1].systolic + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_DIA+"[1-3]")) {
            ret = values[TRIAL_COUNT+trialId-1].diastolic+ NAME_UNIT;
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
        return values;
    }
}

public class ReportStaticConfig implements IExperimentProtocol {
    public static NumberPicker NPSESSION;

    final private View mView;
    final private TextView mSbrStaticHeader;

//    final private NumberPicker mNpSession;
    final private SeekBar mSbrStaticSession;
    final private RangeSeekBar mSbrStaticFirst;
    final private RangeSeekBar mSbrStaticSecond;
    final private RangeSeekBar mSbrStaticThird;
    
    final private EditText mEditStaticFirstFname;
    final private EditText mEditStaticSecondFname;
    final private EditText mEditStaticThirdFname;

    private ReportStatic mReport;

    static public ReportStatic cmdJSONparse(String cmd) {

        // { name : 
        //      { 
        //          s1 : { values : [{},{},{}], fnames:[{},{},{}]] },
        //          s2 : { values : [{},{},{}], fnames:[{},{},{}]] } 
        //      } 
        // }

        ReportStatic result = new ReportStatic();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportStatic.NAME);
            JSONObject s1Obj = jObj.getJSONObject(ReportStatic.NAME_SESSION[0]);
            JSONObject s2Obj = jObj.getJSONObject(ReportStatic.NAME_SESSION[1]);
            // Session 1
            JSONArray jArr = s1Obj.getJSONArray(ReportStatic.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[i].systolic = obj.getInt(ReportStatic.NAME_SYS);
                result.values[i].diastolic = obj.getInt(ReportStatic.NAME_DIA);
            }
            jArr = s1Obj.getJSONArray(ReportStatic.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[i] = obj.getString(ReportStatic.NAME_FEXT);
            }
            // Session 2
            jArr = s2Obj.getJSONArray(ReportStatic.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.values[ReportStatic.TRIAL_COUNT+i].systolic = obj.getInt(ReportStatic.NAME_SYS);
                result.values[ReportStatic.TRIAL_COUNT+i].diastolic = obj.getInt(ReportStatic.NAME_DIA);
            }
            jArr = s2Obj.getJSONArray(ReportStatic.NAME_FNAMES);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                result.fnames[ReportStatic.TRIAL_COUNT+i] = obj.getString(ReportStatic.NAME_FEXT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ReportStaticConfig(View view) {
        mView = view;

        mSbrStaticHeader = (TextView) mView.findViewById(R.id.seekBarStaticHeader);

        mSbrStaticSession = (SeekBar) mView.findViewById(R.id.seekBarStaticSession);

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

        if(NPSESSION != null && NPSESSION.getValue() != 0)
            mSbrStaticSession.setProgress(NPSESSION.getValue()-1);

        mSbrStaticSession.setVisibility(mode);
        mSbrStaticSession.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setValueIntoView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        mSbrStaticHeader.setVisibility(mode);
        mSbrStaticFirst.setVisibility(mode);
        mSbrStaticSecond.setVisibility(mode);
        mSbrStaticThird.setVisibility(mode);

        mEditStaticFirstFname.setVisibility(mode);
        mEditStaticSecondFname.setVisibility(mode);
        mEditStaticThirdFname.setVisibility(mode);
        String hint = mView.getResources().getString(R.string.exp_fname_hint);
        mEditStaticFirstFname.setHint("1st " + hint);
        mEditStaticSecondFname.setHint("2nd " + hint);
        mEditStaticThirdFname.setHint("3rd " + hint);
        mEditStaticFirstFname.setTextSize(14);
        mEditStaticSecondFname.setTextSize(14);
        mEditStaticThirdFname.setTextSize(14);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        // { name : 
        //      { 
        //          s1 : { values : [{},{},{}], fnames:[{},{},{}]] },
        //          s2 : { values : [{},{},{}], fnames:[{},{},{}]] } 
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
            for (int i = 0 ; i < ReportStatic.TRIAL_COUNT ; i++ ) {

                JSONObject valObj = new JSONObject();
                BloodPressure bp = mReport.values[i];
                valObj.put(ReportStatic.NAME_SYS, bp.systolic);
                valObj.put(ReportStatic.NAME_DIA, bp.diastolic);
                jsonArrVals.put(valObj);

                JSONObject logObj = new JSONObject();
                String st = mReport.fnames[i];
                logObj.put(ReportStatic.NAME_FEXT, st);
                jsonArrLogs.put(logObj);

            }
            s1Obj.put(ReportStatic.NAME_VALS, jsonArrVals);
            s1Obj.put(ReportStatic.NAME_FNAMES, jsonArrLogs);
            jsonObj.put(ReportStatic.NAME_SESSION[0], s1Obj);
            // Session 2
            jsonArrVals = new JSONArray();
            jsonArrLogs = new JSONArray();
            for (int i = ReportStatic.TRIAL_COUNT ; i < ReportStatic.TRIAL_COUNT*ReportStatic.SESSION_CNT ; i++ ) {

                JSONObject valObj = new JSONObject();
                BloodPressure bp = mReport.values[i];
                valObj.put(ReportStatic.NAME_SYS, bp.systolic);
                valObj.put(ReportStatic.NAME_DIA, bp.diastolic);
                jsonArrVals.put(valObj);

                JSONObject logObj = new JSONObject();
                String st = mReport.fnames[i];
                logObj.put(ReportStatic.NAME_FEXT, st);
                jsonArrLogs.put(logObj);

            }
            s2Obj.put(ReportStatic.NAME_VALS, jsonArrVals);
            s2Obj.put(ReportStatic.NAME_FNAMES, jsonArrLogs);
            jsonObj.put(ReportStatic.NAME_SESSION[1], s2Obj);

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

        setValueIntoView();
    }

    private void setValueIntoView() {
        int iSbr = mSbrStaticSession.getProgress();
        int iStart = iSbr * ReportStatic.TRIAL_COUNT;

        String header = mView.getResources().getString(R.string.exp_seekBar_static_header);
        mSbrStaticHeader.setText(header + " " + (iSbr+1));

        mSbrStaticFirst.setSelectedMaxValue(mReport.values[iStart+0].systolic);
        mSbrStaticFirst.setSelectedMinValue(mReport.values[iStart+0].diastolic);
        mSbrStaticSecond.setSelectedMaxValue(mReport.values[iStart+1].systolic);
        mSbrStaticSecond.setSelectedMinValue(mReport.values[iStart+1].diastolic);
        mSbrStaticThird.setSelectedMaxValue(mReport.values[iStart+2].systolic);
        mSbrStaticThird.setSelectedMinValue(mReport.values[iStart+2].diastolic);

        mEditStaticFirstFname.setText(mReport.fnames[iStart+0]);
        mEditStaticSecondFname.setText(mReport.fnames[iStart+1]);
        mEditStaticThirdFname.setText(mReport.fnames[iStart+2]);
    }


    private void getValueFromView() {
        int iStart = mSbrStaticSession.getProgress() * ReportStatic.TRIAL_COUNT;

        mReport.values[iStart + 0].systolic = mSbrStaticFirst.getSelectedMaxValue().intValue();
        mReport.values[iStart + 0].diastolic = mSbrStaticFirst.getSelectedMinValue().intValue();
        mReport.values[iStart + 1].systolic = mSbrStaticSecond.getSelectedMaxValue().intValue();
        mReport.values[iStart + 1].diastolic = mSbrStaticSecond.getSelectedMinValue().intValue();
        mReport.values[iStart + 2].systolic = mSbrStaticThird.getSelectedMaxValue().intValue();
        mReport.values[iStart + 2].diastolic = mSbrStaticThird.getSelectedMinValue().intValue();

        mReport.fnames[iStart + 0] = mEditStaticFirstFname.getText().toString();
        mReport.fnames[iStart + 1] = mEditStaticSecondFname.getText().toString();
        mReport.fnames[iStart + 2] = mEditStaticThirdFname.getText().toString();
    }
}
