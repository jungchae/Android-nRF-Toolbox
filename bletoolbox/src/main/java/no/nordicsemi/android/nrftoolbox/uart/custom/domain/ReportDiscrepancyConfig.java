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

class ReportDiscrepancy {
    public static final String NAME = "ReportDiscrepancy";
    public static final String NAME_PASSDISCREPANCY = "passdiscrepancy";
    public static final String NAME_SYS = "systolic";
    public static final String NAME_DIA = "diastolic";
    public static final String NAME_LEFTVALS = "lvalues";
    public static final String NAME_RIGHTVALS = "rvalues";
    public static final String NAME_UNIT = "mmHg";
    private static final int TRIAL_COUNT = 3;
    public boolean passdiscrepancy;
    public BloodPressure[] lvalues = new BloodPressure[TRIAL_COUNT];
    public BloodPressure[] rvalues = new BloodPressure[TRIAL_COUNT];
    ReportDiscrepancy() {
        for (int i = 0; i < TRIAL_COUNT; i++) {
            lvalues[i] = new BloodPressure();
            rvalues[i] = new BloodPressure();
        }
    }
}

public class ReportDiscrepancyConfig implements IExperimentProtocol {

    final private View mView;
    final private CheckBox mCbPassDisc;
    final private TextView mSbrDiscFirstHeader;
    final private RangeSeekBar mSbrDiscFirstLeft;
    final private RangeSeekBar mSbrDiscFirstRight;
    final private TextView mSbrDiscSecondHeader;
    final private RangeSeekBar mSbrDiscSecondLeft;
    final private RangeSeekBar mSbrDiscSecondRight;
    final private TextView mSbrDiscThirdHeader;
    final private RangeSeekBar mSbrDiscThirdLeft;
    final private RangeSeekBar mSbrDiscThirdRight;

    private ReportDiscrepancy mReport;

    public ReportDiscrepancyConfig(View view) {
        mView = view;
        mCbPassDisc = (CheckBox) mView.findViewById(R.id.checkBoxPassDisc);
        mSbrDiscFirstHeader = (TextView) mView.findViewById(R.id.seekBarDiscFirstHeader);
        mSbrDiscFirstLeft = (RangeSeekBar) mView.findViewById(R.id.seekBarDiscFirstLeft);
        mSbrDiscFirstRight = (RangeSeekBar) mView.findViewById(R.id.seekBarDiscFirstRight);
        mSbrDiscSecondHeader = (TextView) mView.findViewById(R.id.seekBarDiscSecondHeader);
        mSbrDiscSecondLeft = (RangeSeekBar) mView.findViewById(R.id.seekBarDiscSecondLeft);
        mSbrDiscSecondRight = (RangeSeekBar) mView.findViewById(R.id.seekBarDiscSecondRight);
        mSbrDiscThirdHeader = (TextView) mView.findViewById(R.id.seekBarDiscThirdHeader);
        mSbrDiscThirdLeft = (RangeSeekBar) mView.findViewById(R.id.seekBarDiscThirdLeft);
        mSbrDiscThirdRight = (RangeSeekBar) mView.findViewById(R.id.seekBarDiscThirdRight);

        mReport = new ReportDiscrepancy();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mCbPassDisc.setVisibility(mode);
        mSbrDiscFirstHeader.setVisibility(mode);
        mSbrDiscFirstLeft.setVisibility(mode);
        mSbrDiscFirstRight.setVisibility(mode);
        mSbrDiscSecondHeader.setVisibility(mode);
        mSbrDiscSecondLeft.setVisibility(mode);
        mSbrDiscSecondRight.setVisibility(mode);
        mSbrDiscThirdHeader.setVisibility(mode);
        mSbrDiscThirdLeft.setVisibility(mode);
        mSbrDiscThirdRight.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.passdiscrepancy = mCbPassDisc.isChecked();
        mReport.lvalues[0].systolic = mSbrDiscFirstLeft.getSelectedMaxValue().intValue();
        mReport.lvalues[0].diastolic= mSbrDiscFirstLeft.getSelectedMinValue().intValue();
        mReport.lvalues[1].systolic = mSbrDiscSecondLeft.getSelectedMaxValue().intValue();
        mReport.lvalues[1].diastolic= mSbrDiscSecondLeft.getSelectedMinValue().intValue();
        mReport.lvalues[2].systolic = mSbrDiscThirdLeft.getSelectedMaxValue().intValue();
        mReport.lvalues[2].diastolic= mSbrDiscThirdLeft.getSelectedMinValue().intValue();
        mReport.rvalues[0].systolic = mSbrDiscFirstRight.getSelectedMaxValue().intValue();
        mReport.rvalues[0].diastolic= mSbrDiscFirstRight.getSelectedMinValue().intValue();
        mReport.rvalues[1].systolic = mSbrDiscSecondRight.getSelectedMaxValue().intValue();
        mReport.rvalues[1].diastolic= mSbrDiscSecondRight.getSelectedMinValue().intValue();
        mReport.rvalues[2].systolic = mSbrDiscThirdRight.getSelectedMaxValue().intValue();
        mReport.rvalues[2].diastolic= mSbrDiscThirdRight.getSelectedMinValue().intValue();

        try {
            jsonObj.put(ReportDiscrepancy.NAME_PASSDISCREPANCY, mReport.passdiscrepancy);

            JSONArray jsonArr = new JSONArray();
            for (BloodPressure bp : mReport.lvalues ) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportDiscrepancy.NAME_SYS, bp.systolic);
                bpObj.put(ReportDiscrepancy.NAME_DIA, bp.diastolic);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportDiscrepancy.NAME_LEFTVALS, jsonArr);

            jsonArr = new JSONArray();
            for (BloodPressure bp : mReport.rvalues ) {
                JSONObject bpObj = new JSONObject();
                bpObj.put(ReportDiscrepancy.NAME_SYS, bp.systolic);
                bpObj.put(ReportDiscrepancy.NAME_DIA, bp.diastolic);
                jsonArr.put(bpObj);
            }
            jsonObj.put(ReportDiscrepancy.NAME_RIGHTVALS, jsonArr);

            jsonWrapper.put(ReportDiscrepancy.NAME, jsonObj);
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
            JSONObject jObj = jWrapper.getJSONObject(ReportDiscrepancy.NAME);
            mReport.passdiscrepancy = jObj.getBoolean(ReportDiscrepancy.NAME_PASSDISCREPANCY);

            JSONArray jArr = jObj.getJSONArray(ReportDiscrepancy.NAME_LEFTVALS);

            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                mReport.lvalues[i].systolic = obj.getInt(ReportDiscrepancy.NAME_SYS);
                mReport.lvalues[i].diastolic = obj.getInt(ReportDiscrepancy.NAME_DIA);
            }

            jArr = jObj.getJSONArray(ReportDiscrepancy.NAME_RIGHTVALS);
            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                mReport.rvalues[i].systolic = obj.getInt(ReportDiscrepancy.NAME_SYS);
                mReport.rvalues[i].diastolic = obj.getInt(ReportDiscrepancy.NAME_DIA);
            }

            mCbPassDisc.setChecked(mReport.passdiscrepancy);
            mSbrDiscFirstLeft.setSelectedMaxValue(mReport.lvalues[0].systolic);
            mSbrDiscFirstLeft.setSelectedMinValue(mReport.lvalues[0].diastolic);
            mSbrDiscFirstRight.setSelectedMaxValue(mReport.rvalues[0].systolic);
            mSbrDiscFirstRight.setSelectedMinValue(mReport.rvalues[0].diastolic);
            mSbrDiscSecondLeft.setSelectedMaxValue(mReport.lvalues[1].systolic);
            mSbrDiscSecondLeft.setSelectedMinValue(mReport.lvalues[1].diastolic);
            mSbrDiscSecondRight.setSelectedMaxValue(mReport.rvalues[1].systolic);
            mSbrDiscSecondRight.setSelectedMinValue(mReport.rvalues[1].diastolic);
            mSbrDiscThirdLeft.setSelectedMaxValue(mReport.lvalues[2].systolic);
            mSbrDiscThirdLeft.setSelectedMinValue(mReport.lvalues[2].diastolic);
            mSbrDiscThirdRight.setSelectedMaxValue(mReport.rvalues[2].systolic);
            mSbrDiscThirdRight.setSelectedMinValue(mReport.rvalues[2].diastolic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
