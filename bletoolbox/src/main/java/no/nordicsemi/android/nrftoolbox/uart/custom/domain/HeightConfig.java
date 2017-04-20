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

class Height {
    public static final String NAME = "Height";
    public static final String NAME_HEIGHT = "height";
    public static final String NAME_UNIT = "cm";
    public int height;
}

public class HeightConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSbrHeightHeader;
    final private RangeSeekBar mSbrHeight;

    private Height mReport;

    public HeightConfig(View view) {
        mView = view;
        mSbrHeightHeader = (TextView) mView.findViewById(R.id.seekBarHeightHeader);
        mSbrHeight = (RangeSeekBar) mView.findViewById(R.id.seekBarHeight);

        mReport = new Height();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrHeightHeader.setVisibility(mode);
        mSbrHeight.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.height = mSbrHeight.getSelectedMaxValue().intValue();

        try {
            jsonObj.put(Height.NAME_HEIGHT, mReport.height);
            jsonWrapper.put(Height.NAME, jsonObj);
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
            JSONObject jObj = jWrapper.getJSONObject(Height.NAME);

            mReport.height = jObj.getInt(Height.NAME_HEIGHT);

            mSbrHeight.setSelectedMaxValue(mReport.height);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
