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
class ReportStatic {
    public static final String NAME = "ReportStatic";
    public static final String NAME_SYS = "systolic";
    public static final String NAME_DIA = "diastolic";
    public static final String NAME_VALS = "values";
    public static final String NAME_UNIT = "mmHg";
    private static final int TRIAL_COUNT = 3;
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT];
    ReportStatic() {
        for (int i = 0; i < TRIAL_COUNT; i++) {
            values[i] = new BloodPressure();
        }
    }
}

public class ReportStaticConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSbrStaticHeader;
    final private RangeSeekBar mSbrStaticFirst;
    final private RangeSeekBar mSbrStaticSecond;
    final private RangeSeekBar mSbrStaticThird;

    private ReportStatic mReport;

    public ReportStaticConfig(View view) {
        mView = view;
        mSbrStaticHeader = (TextView) mView.findViewById(R.id.seekBarStaticHeader);
        mSbrStaticFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarStaticFirst);
        mSbrStaticSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarStaticSecond);
        mSbrStaticThird = (RangeSeekBar) mView.findViewById(R.id.seekBarStaticThird);

        mReport = new ReportStatic();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrStaticHeader.setVisibility(mode);
        mSbrStaticFirst.setVisibility(mode);
        mSbrStaticSecond.setVisibility(mode);
        mSbrStaticThird.setVisibility(mode);

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

        try {
            JSONArray jsonArr = new JSONArray();
            for (BloodPressure bp : mReport.values ) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportStatic.NAME_SYS, bp.systolic);
                bpObj.put(ReportStatic.NAME_DIA, bp.diastolic);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportStatic.NAME_VALS, jsonArr);

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
        try {
            if(cmd=="") return;
            JSONObject jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportStatic.NAME);
            JSONArray jArr = jObj.getJSONArray(ReportStatic.NAME_VALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                mReport.values[i].systolic = obj.getInt(ReportStatic.NAME_SYS);
                mReport.values[i].diastolic = obj.getInt(ReportStatic.NAME_DIA);
            }

            mSbrStaticFirst.setSelectedMaxValue(mReport.values[0].systolic);
            mSbrStaticFirst.setSelectedMinValue(mReport.values[0].diastolic);
            mSbrStaticSecond.setSelectedMaxValue(mReport.values[1].systolic);
            mSbrStaticSecond.setSelectedMinValue(mReport.values[1].diastolic);
            mSbrStaticThird.setSelectedMaxValue(mReport.values[2].systolic);
            mSbrStaticThird.setSelectedMinValue(mReport.values[2].diastolic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
