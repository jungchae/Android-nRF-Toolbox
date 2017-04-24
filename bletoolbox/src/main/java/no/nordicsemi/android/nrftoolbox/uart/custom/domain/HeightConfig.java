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

class Height implements IReport{
    public static final String NAME = "Height";
    public static final String NAME_HEIGHT = "height";
    public static final String NAME_UNIT = "cm";
    public int height;

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
            case NAME_HEIGHT:
                ret = height;
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
            case NAME_HEIGHT:
                ret = height + " " + NAME_UNIT;
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

    static public Object cmdJSONparse(String cmd) {
        Height result = new Height();
        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);

            JSONObject jObj = jWrapper.getJSONObject(Height.NAME);

            result.height = jObj.getInt(Height.NAME_HEIGHT);
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

        mReport = (Height) cmdJSONparse(cmd);

        mSbrHeight.setSelectedMaxValue(mReport.height);
    }
}
