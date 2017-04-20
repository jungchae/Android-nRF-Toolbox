package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ianpinto.androidrangeseekbar.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.BloodPressure;

/**
 * Created by jungchae on 17. 4. 19.
 */

class ReportIncrease {
    public static final String NAME = "ReportIncrease";
    public static final String NAME_LOADFIRST = "loadfirst";
    public static final String NAME_LOADSECOND = "loadsecond";
    public static final String NAME_SYS = "systolic";
    public static final String NAME_DIA = "diastolic";
    public static final String NAME_VALS = "values";
    public static final String NAME_UNIT = "mmHg";
    private static final int TRIAL_COUNT = 4;
    public int loadfirst;
    public int loadsecond;
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT];
    ReportIncrease() {
        for (int i = 0; i < TRIAL_COUNT; i++) {
            values[i] = new BloodPressure();
        }
    }
}

public class ReportIncreaseConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSbrIncreaseLoadHeader;
    final private RangeSeekBar mSbrIncreaseLoadFirst;
    final private RangeSeekBar mSbrIncreaseLoadSecond;
    final private TextView mSbrIncreaseHeader;
    final private RangeSeekBar mSbrIncreaseFirst;
    final private RangeSeekBar mSbrIncreaseSecond;
    final private RangeSeekBar mSbrIncreaseThird;
    final private RangeSeekBar mSbrIncreaseFourth;

    private ReportIncrease mReport;

    public ReportIncreaseConfig(View view) {
        mView = view;
        mSbrIncreaseLoadHeader = (TextView) mView.findViewById(R.id.seekBarIncreaseLoadHeader);
        mSbrIncreaseLoadFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseLoadFirst);
        mSbrIncreaseLoadSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseLoadSecond);
        mSbrIncreaseHeader = (TextView) mView.findViewById(R.id.seekBarIncreaseHeader);
        mSbrIncreaseFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseFirst);
        mSbrIncreaseSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseSecond);
        mSbrIncreaseThird = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseThird);
        mSbrIncreaseFourth = (RangeSeekBar) mView.findViewById(R.id.seekBarIncreaseFourth);

        mReport = new ReportIncrease();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrIncreaseLoadHeader.setVisibility(mode);
        mSbrIncreaseLoadFirst.setVisibility(mode);
        mSbrIncreaseLoadSecond.setVisibility(mode);
        mSbrIncreaseHeader.setVisibility(mode);
        mSbrIncreaseFirst.setVisibility(mode);
        mSbrIncreaseSecond.setVisibility(mode);
        mSbrIncreaseThird.setVisibility(mode);
        mSbrIncreaseFourth.setVisibility(mode);

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
        try {
            if(cmd=="") return;
            JSONObject jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportIncrease.NAME);
            mReport.loadfirst = jObj.getInt(ReportIncrease.NAME_LOADFIRST);
            mReport.loadsecond = jObj.getInt(ReportIncrease.NAME_LOADSECOND);
            JSONArray jArr = jObj.getJSONArray(ReportIncrease.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                mReport.values[i].systolic = obj.getInt(ReportIncrease.NAME_SYS);
                mReport.values[i].diastolic = obj.getInt(ReportIncrease.NAME_DIA);
            }

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
