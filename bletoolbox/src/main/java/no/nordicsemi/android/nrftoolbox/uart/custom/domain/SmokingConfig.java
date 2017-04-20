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

class Smoking {
    public static final String NAME = "Smoking";
    public static final String NAME_LASTSMOKING = "lastsmoking";
    public static final String NAME_UNIT = "hours";
    public int lastsmoking;
}

public class SmokingConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSbrLastSmokingHeader;
    final private RangeSeekBar mSbrLastSmoking;

    private Smoking mReport;

    public SmokingConfig(View view) {
        mView = view;
        mSbrLastSmokingHeader = (TextView) mView.findViewById(R.id.seekBarLastSmokingHeader);
        mSbrLastSmoking = (RangeSeekBar) mView.findViewById(R.id.seekBarLastSmoking);

        mReport = new Smoking();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrLastSmokingHeader.setVisibility(mode);
        mSbrLastSmoking.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.lastsmoking = mSbrLastSmoking.getSelectedMaxValue().intValue();

        try {
            jsonObj.put(Smoking.NAME_LASTSMOKING, mReport.lastsmoking);

            jsonWrapper.put(Smoking.NAME, jsonObj);
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
            JSONObject jObj = jWrapper.getJSONObject(Smoking.NAME);

            mReport.lastsmoking = jObj.getInt(Smoking.NAME_LASTSMOKING);

            mSbrLastSmoking.setSelectedMaxValue(mReport.lastsmoking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
