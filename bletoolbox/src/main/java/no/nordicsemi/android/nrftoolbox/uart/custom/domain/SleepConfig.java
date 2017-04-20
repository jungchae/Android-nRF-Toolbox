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

class Sleep {
    public static final String NAME = "Sleep";
    public static final String NAME_AVGSLEEP = "avgsleep";
    public static final String NAME_LASTSLEEP = "lastsleep";
    public static final String NAME_UNIT = "hour";
    public int avgsleep;
    public int lastsleep;
}

public class SleepConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSbrAvgSleepHeader;
    final private RangeSeekBar mSbrAvgSleep;
    final private TextView mSbrLastSleepHeader;
    final private RangeSeekBar mSbrLastSleep;

    private Sleep mReport;

    public SleepConfig(View view) {
        mView = view;
        mSbrAvgSleepHeader = (TextView) mView.findViewById(R.id.seekBarAvgSleepHeader);
        mSbrAvgSleep = (RangeSeekBar) mView.findViewById(R.id.seekBarAvgSleep);
        mSbrLastSleepHeader = (TextView) mView.findViewById(R.id.seekBarLastSleepHeader);
        mSbrLastSleep = (RangeSeekBar) mView.findViewById(R.id.seekBarLastSleep);

        mReport = new Sleep();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrAvgSleepHeader.setVisibility(mode);
        mSbrAvgSleep.setVisibility(mode);
        mSbrLastSleepHeader.setVisibility(mode);
        mSbrLastSleep.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.avgsleep = mSbrAvgSleep.getSelectedMaxValue().intValue();
        mReport.lastsleep = mSbrLastSleep.getSelectedMaxValue().intValue();

        try {
            jsonObj.put(Sleep.NAME_AVGSLEEP, mReport.avgsleep);
            jsonObj.put(Sleep.NAME_LASTSLEEP, mReport.lastsleep);

            jsonWrapper.put(Sleep.NAME, jsonObj);
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
            JSONObject jObj = jWrapper.getJSONObject(Sleep.NAME);

            mReport.avgsleep = jObj.getInt(Sleep.NAME_AVGSLEEP);
            mReport.lastsleep = jObj.getInt(Sleep.NAME_LASTSLEEP);

            mSbrAvgSleep.setSelectedMaxValue(mReport.avgsleep);
            mSbrLastSleep.setSelectedMaxValue(mReport.lastsleep);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
