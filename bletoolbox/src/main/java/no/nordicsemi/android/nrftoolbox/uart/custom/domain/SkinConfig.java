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

class Skin implements IReport{
    public static final String NAME = "Skin";
    public static final String NAME_SKINCOLOR = "skincolor";
    public static final String NAME_UNIT = "";
    public int skincolor;

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

        switch(sKey) {
            case NAME_SKINCOLOR:
                ret = skincolor;
                break;
            default:
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
            case NAME_SKINCOLOR:
                ret = skincolor + "";
                break;
            default:
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

public class SkinConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSbrSkinColorHeader;
    final private RangeSeekBar mSbrSkinColor;

    private Skin mReport;

    public SkinConfig(View view) {
        mView = view;
        mSbrSkinColorHeader = (TextView) mView.findViewById(R.id.seekBarSkinColorHeader);
        mSbrSkinColor = (RangeSeekBar) mView.findViewById(R.id.seekBarSkinColor);

        mReport = new Skin();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrSkinColorHeader.setVisibility(mode);
        mSbrSkinColor.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.skincolor = mSbrSkinColor.getSelectedMaxValue().intValue();

        try {
            jsonObj.put(Skin.NAME_SKINCOLOR, mReport.skincolor);

            jsonWrapper.put(Skin.NAME, jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonWrapper.toString();
    }

    static public Object cmdJSONparse(String cmd) {
        Skin result = new Skin();
        try {
            JSONObject jWrapper = null;
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(Skin.NAME);
            result.skincolor = jObj.getInt(Skin.NAME_SKINCOLOR);
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

        mReport = (Skin) cmdJSONparse(cmd);

        mSbrSkinColor.setSelectedMaxValue(mReport.skincolor);
    }
}
