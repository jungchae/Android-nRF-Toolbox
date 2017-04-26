package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ianpinto.androidrangeseekbar.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.BloodPressure;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.IReport;

/**
 * Created by jungchae on 17. 4. 19.
 */

class Sleep implements IReport{
    public static final String NAME = "Sleep";
    public static final String NAME_AVGSLEEP = "avgsleep";
    public static final String NAME_LASTSLEEP = "lastsleep";
    public static final String NAME_UNIT = "hour";
    public static final String[] NAME_SESSION = {"s1.", "s2."};
    public static final int SESSION_CNT = 2;
    public static final int TRIAL_COUNT = 1;
    public int[] avgsleep = new int[SESSION_CNT];
    public int[] lastsleep = new int[SESSION_CNT];

    @Override
    public int getMaxTrialCount() {
        return TRIAL_COUNT;
    }

    @Override
    public boolean getBoolean(String sKey) {
        return false;
    }

    @Override
    public int getInt(String sKey) {
        int ret = -1;

        if(sKey.equals(NAME_SESSION[0]+NAME_AVGSLEEP)) {
            ret = avgsleep[0];
        } else if(sKey.equals(NAME_SESSION[1]+NAME_AVGSLEEP)) {
            ret = avgsleep[1];
        } else if(sKey.equals(NAME_SESSION[0]+NAME_LASTSLEEP)) {
            ret = lastsleep[0];
        } else if(sKey.equals(NAME_SESSION[1]+NAME_LASTSLEEP)) {
            ret = lastsleep[1];
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
            default:
        }

        if(sKey.equals(NAME_SESSION[0]+NAME_AVGSLEEP)) {
            ret = avgsleep[0] + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_AVGSLEEP)) {
            ret = avgsleep[1] + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[0]+NAME_LASTSLEEP)) {
            ret = lastsleep[0] + NAME_UNIT;
        } else if(sKey.equals(NAME_SESSION[1]+NAME_LASTSLEEP)) {
            ret = lastsleep[1] + NAME_UNIT;
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

public class SleepConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSleepS1Header;
    final private TextView mSbrAvgSleepS1Header;
    final private RangeSeekBar mSbrAvgSleepS1;
    final private TextView mSbrLastSleepS1Header;
    final private RangeSeekBar mSbrLastSleepS1;
    final private TextView mSleepS2Header;
    final private TextView mSbrAvgSleepS2Header;
    final private RangeSeekBar mSbrAvgSleepS2;
    final private TextView mSbrLastSleepS2Header;
    final private RangeSeekBar mSbrLastSleepS2;

    private Sleep mReport;

    public SleepConfig(View view) {
        mView = view;

        mSleepS1Header = (TextView) mView.findViewById(R.id.sleepS1Header);
        mSbrAvgSleepS1Header = (TextView) mView.findViewById(R.id.seekBarAvgSleepS1Header);
        mSbrAvgSleepS1 = (RangeSeekBar) mView.findViewById(R.id.seekBarAvgSleepS1);
        mSbrLastSleepS1Header = (TextView) mView.findViewById(R.id.seekBarLastSleepS1Header);
        mSbrLastSleepS1 = (RangeSeekBar) mView.findViewById(R.id.seekBarLastSleepS1);

        mSleepS2Header = (TextView) mView.findViewById(R.id.sleepS2Header);
        mSbrAvgSleepS2Header = (TextView) mView.findViewById(R.id.seekBarAvgSleepS2Header);
        mSbrAvgSleepS2 = (RangeSeekBar) mView.findViewById(R.id.seekBarAvgSleepS2);
        mSbrLastSleepS2Header = (TextView) mView.findViewById(R.id.seekBarLastSleepS2Header);
        mSbrLastSleepS2 = (RangeSeekBar) mView.findViewById(R.id.seekBarLastSleepS2);

        mReport = new Sleep();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;

        mSleepS1Header.setVisibility(mode);
        mSbrAvgSleepS1Header.setVisibility(mode);
        mSbrAvgSleepS1.setVisibility(mode);
        mSbrLastSleepS1Header.setVisibility(mode);
        mSbrLastSleepS1.setVisibility(mode);

        mSleepS2Header.setVisibility(mode);
        mSbrAvgSleepS2Header.setVisibility(mode);
        mSbrAvgSleepS2.setVisibility(mode);
        mSbrLastSleepS2Header.setVisibility(mode);
        mSbrLastSleepS2.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.avgsleep[0] = mSbrAvgSleepS1.getSelectedMaxValue().intValue();
        mReport.avgsleep[1] = mSbrAvgSleepS2.getSelectedMaxValue().intValue();
        mReport.lastsleep[0] = mSbrLastSleepS1.getSelectedMaxValue().intValue();
        mReport.lastsleep[1] = mSbrLastSleepS2.getSelectedMaxValue().intValue();

        try {
            JSONObject slObj = new JSONObject();
            slObj.put(Sleep.NAME_SESSION[0], mReport.avgsleep[0]);
            slObj.put(Sleep.NAME_SESSION[1], mReport.avgsleep[1]);
            jsonObj.put(Sleep.NAME_AVGSLEEP, slObj);

            slObj = new JSONObject();
            slObj.put(Sleep.NAME_SESSION[0], mReport.lastsleep[0]);
            slObj.put(Sleep.NAME_SESSION[1], mReport.lastsleep[1]);
            jsonObj.put(Sleep.NAME_LASTSLEEP, slObj);

            jsonWrapper.put(Sleep.NAME, jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonWrapper.toString();
    }

    static public Object cmdJSONparse(String cmd) {
        Sleep result = new Sleep();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(Sleep.NAME);

            JSONObject slObj = jObj.getJSONObject(Sleep.NAME_AVGSLEEP);
            result.avgsleep[0] = slObj.getInt(Sleep.NAME_SESSION[0]);
            result.avgsleep[1] = slObj.getInt(Sleep.NAME_SESSION[1]);

            slObj = jObj.getJSONObject(Sleep.NAME_LASTSLEEP);
            result.lastsleep[0] = slObj.getInt(Sleep.NAME_SESSION[0]);
            result.lastsleep[1] = slObj.getInt(Sleep.NAME_SESSION[1]);
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

        mReport = (Sleep) cmdJSONparse(cmd);

        mSbrAvgSleepS1.setSelectedMaxValue(mReport.avgsleep[0]);
        mSbrAvgSleepS2.setSelectedMaxValue(mReport.avgsleep[1]);
        mSbrLastSleepS1.setSelectedMaxValue(mReport.lastsleep[0]);
        mSbrLastSleepS2.setSelectedMaxValue(mReport.lastsleep[1]);
    }
}
