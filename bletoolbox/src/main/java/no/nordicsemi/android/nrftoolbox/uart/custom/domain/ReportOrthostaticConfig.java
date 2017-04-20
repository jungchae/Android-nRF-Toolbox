package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.CheckBox;
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
class ReportOrthostatic {
    public static final String NAME = "ReportOrthostatic";
    public static final String NAME_FELLDIZZNESS = "feeldizzness";
    public static final String NAME_SYS = "systolic";
    public static final String NAME_DIA = "diastolic";
    public static final String NAME_VALS = "values";
    public static final String NAME_UNIT = "mmHg";
    private static final int TRIAL_COUNT = 3;
    public boolean feeldizzness;
    public BloodPressure[] values = new BloodPressure[TRIAL_COUNT];
    ReportOrthostatic() {
        for (int i = 0; i < TRIAL_COUNT; i++) {
            values[i] = new BloodPressure();
        }
    }
}
public class ReportOrthostaticConfig implements IExperimentProtocol {

    final private View mView;
    final private CheckBox mCbDizzness;
    final private TextView mSbrOrthoHeader;
    final private RangeSeekBar mSbrOrthoFirst;
    final private RangeSeekBar mSbrOrthoSecond;
    final private RangeSeekBar mSbrOrthoThird;;

    private ReportOrthostatic mReport;

    public ReportOrthostaticConfig(View view) {
        mView = view;
        mCbDizzness = (CheckBox) mView.findViewById(R.id.checkBoxDizzness);
        mSbrOrthoHeader = (TextView) mView.findViewById(R.id.seekBarOrthoHeader);
        mSbrOrthoFirst = (RangeSeekBar) mView.findViewById(R.id.seekBarOrthoFirst);
        mSbrOrthoSecond = (RangeSeekBar) mView.findViewById(R.id.seekBarOrthoSecond);
        mSbrOrthoThird = (RangeSeekBar) mView.findViewById(R.id.seekBarOrthoThird);

        mReport = new ReportOrthostatic();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mCbDizzness.setVisibility(mode);
        mSbrOrthoHeader.setVisibility(mode);
        mSbrOrthoFirst.setVisibility(mode);
        mSbrOrthoSecond.setVisibility(mode);
        mSbrOrthoThird.setVisibility(mode);

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
        try {
            if(cmd=="") return;
            JSONObject jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ReportOrthostatic.NAME);
            mReport.feeldizzness = jObj.getBoolean(ReportOrthostatic.NAME_FELLDIZZNESS);

            JSONArray jArr = jObj.getJSONArray(ReportOrthostatic.NAME_VALS);

            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                mReport.values[i].systolic = obj.getInt(ReportOrthostatic.NAME_SYS);
                mReport.values[i].diastolic = obj.getInt(ReportOrthostatic.NAME_DIA);
            }

            mCbDizzness.setChecked(mReport.feeldizzness);
            mSbrOrthoFirst.setSelectedMaxValue(mReport.values[0].systolic);
            mSbrOrthoFirst.setSelectedMinValue(mReport.values[0].diastolic);
            mSbrOrthoSecond.setSelectedMaxValue(mReport.values[1].systolic);
            mSbrOrthoSecond.setSelectedMinValue(mReport.values[1].diastolic);
            mSbrOrthoThird.setSelectedMaxValue(mReport.values[2].systolic);
            mSbrOrthoThird.setSelectedMinValue(mReport.values[2].diastolic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
