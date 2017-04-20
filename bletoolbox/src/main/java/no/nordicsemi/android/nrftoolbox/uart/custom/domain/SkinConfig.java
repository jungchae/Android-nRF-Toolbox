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

class Skin {
    public static final String NAME = "Skin";
    public static final String NAME_SKINCOLOR = "skincolor";
    public static final String NAME_UNIT = "";
    public int skincolor;
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

    public RadioGroup getEolGroup(){
        return null;
    }

    private void uiConfiguration(String cmd) {
        try {
            if(cmd=="") return;
            JSONObject jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(Skin.NAME);

            mReport.skincolor = jObj.getInt(Skin.NAME_SKINCOLOR);

            mSbrSkinColor.setSelectedMaxValue(mReport.skincolor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
