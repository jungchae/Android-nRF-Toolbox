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

class ReportDecrease {
    public static final String NAME = "ReportDecrease";
    public static final String NAME_TEMP = "temperature";
    public static final String NAME_SYS = "systolic";
    public static final String NAME_DIA = "diastolic";
    public static final String NAME_VALS = "values";
    public static final String NAME_UNIT = "mmHg";
    private static final int TRIAL_COUNT = 7;
    public int temperature;
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT];
    ReportDecrease() {
        for (int i = 0; i < TRIAL_COUNT; i++) {
            values[i] = new BloodPressure();
        }
    }
}

public class ReportDecreaseConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSbrDecreaseTemperatureHeader;
    final private RangeSeekBar mSbrDecreaseTemperature;
    final private TextView mSbrDecreaseHeader;
    final private RangeSeekBar mSbrDecreaseFirst;
    final private RangeSeekBar mSbrDecreaseSecond;
    final private RangeSeekBar mSbrDecreaseThird;
    final private RangeSeekBar mSbrDecreaseFourth;
    final private RangeSeekBar mSbrDecreaseFifth;
    final private RangeSeekBar mSbrDecreaseSixth;
    final private RangeSeekBar mSbrDecreaseSeventh;

    private ReportDecrease mReport;

    public ReportDecreaseConfig(View view) {
        mView = view;
        mSbrDecreaseTemperatureHeader = (TextView) mView.findViewById(R.id.seekBarDecreaseTemperatureHeader);
        mSbrDecreaseTemperature = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseTemperature);
        mSbrDecreaseHeader = (TextView) mView.findViewById(R.id.seekBarDecreaseHeader);
        mSbrDecreaseFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseFirst);
        mSbrDecreaseSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseSecond);
        mSbrDecreaseThird = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseThird);
        mSbrDecreaseFourth = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseFourth);
        mSbrDecreaseFifth = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseFifth);
        mSbrDecreaseSixth = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseSixth);
        mSbrDecreaseSeventh = (RangeSeekBar) mView.findViewById(R.id.seekBarDecreaseSeventh);

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

        try {
            jsonObj.put(ReportDecrease.NAME_TEMP, mReport.temperature);
            JSONArray jsonArr = new JSONArray();
            for (BloodPressure bp : mReport.values ) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportDecrease.NAME_SYS, bp.systolic);
                bpObj.put(ReportDecrease.NAME_DIA, bp.diastolic);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportDecrease.NAME_VALS, jsonArr);

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
        try {
            if(cmd=="") return;
            JSONObject jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportDecrease.NAME);
            mReport.temperature = jObj.getInt(ReportDecrease.NAME_TEMP);
            JSONArray jArr = jObj.getJSONArray(ReportDecrease.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                mReport.values[i].systolic = obj.getInt(ReportDecrease.NAME_SYS);
                mReport.values[i].diastolic = obj.getInt(ReportDecrease.NAME_DIA);
            }
            
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
