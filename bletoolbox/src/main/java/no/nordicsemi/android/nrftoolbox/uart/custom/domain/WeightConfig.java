package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ianpinto.androidrangeseekbar.rangeseekbar.RangeSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.BloodPressure;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.IReport;

/**
 * Created by jungchae on 17. 4. 19.
 */

class Weight implements IReport{
    public static final String NAME = "Weight";
    public static final String NAME_WEIGHT= "weight";
    public static final String NAME_UNIT = "kg";
    public int weight;

    @Override
    public int getMaxTrialCount() {
        return -1;
    }

    @Override
    public boolean getBoolean(String sKey) {
        return false;
    }

    @Override
    public int getInt(String sKey) {
        int ret = -1;
        switch (sKey) {
            case NAME_WEIGHT:
                ret = weight;
                break;
            default:
        }
        return ret;
    }

    @Override
    public String getString(String sKey) {
        String ret = "";

        switch (sKey) {
            case "NAME":
                ret = NAME;
            case NAME_WEIGHT:
                ret = weight + " " + NAME_UNIT;
                break;
        }
        return ret;
    }

    @Override
    public String[] getRegisteredFileInfo() {
        return new String[0];
    }

    @Override
    public BloodPressure[] getBloodPressure() {
        return new BloodPressure[0];
    }
}

public class WeightConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSbrWeightHeader;
    final private RangeSeekBar mSbrWeight;

    private Weight mReport;

    public WeightConfig(View view) {
        mView = view;
        mSbrWeightHeader = (TextView) mView.findViewById(R.id.seekBarWeightHeader);
        mSbrWeight = (RangeSeekBar) mView.findViewById(R.id.seekBarWeight);

        mReport = new Weight();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrWeightHeader.setVisibility(mode);
        mSbrWeight.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.weight = mSbrWeight.getSelectedMaxValue().intValue();

        try {
            jsonObj.put(Weight.NAME_WEIGHT, mReport.weight);
            jsonWrapper.put(Weight.NAME, jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonWrapper.toString();
    }

    static public Object cmdJSONparse(String cmd) {
        Weight result = new Weight();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(Weight.NAME);
            result.weight = jObj.getInt(Weight.NAME_WEIGHT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public RadioGroup getEolGroup(){
        return null;
    }

    private void uiConfiguration(String cmd) {
        if(cmd=="") return;
        mReport = (Weight) cmdJSONparse(cmd);
        mSbrWeight.setSelectedMaxValue(mReport.weight);
    }
}
