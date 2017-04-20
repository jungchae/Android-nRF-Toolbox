package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ianpinto.androidrangeseekbar.rangeseekbar.RangeSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import no.nordicsemi.android.nrftoolbox.R;

/**
 * Created by jungchae on 17. 4. 19.
 */

class Weight {
    public static final String NAME = "Weight";
    public static final String NAME_WEIGHT= "weight";
    public static final String NAME_UNIT = "kg";
    public int weight;
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

    public RadioGroup getEolGroup(){
        return null;
    }

    private void uiConfiguration(String cmd) {
        try {
            if(cmd=="") return;
            JSONObject jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(Weight.NAME);
            mReport.weight= jObj.getInt(Weight.NAME_WEIGHT);
            mSbrWeight.setSelectedMaxValue(mReport.weight);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
