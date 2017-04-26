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

class Smoking implements IReport{
    public static final String NAME = "Smoking";
    public static final String NAME_LASTSMOKING = "lastsmoking";
    public static final String NAME_UNIT = "hours";
    public static final String[] NAME_SESSION = {"s1.", "s2."};
    public static final int SESSION_CNT = 2;
    public static final int TRIAL_COUNT = 1;
    public int[] lastsmoking = new int[SESSION_CNT];

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

        if(sKey.equals(NAME_LASTSMOKING+NAME_SESSION[0])) {
            ret = lastsmoking[0];
        } else if(sKey.equals(NAME_LASTSMOKING+NAME_SESSION[1])) {
            ret = lastsmoking[1];
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

        if(sKey.equals(NAME_LASTSMOKING+NAME_SESSION[0])) {
            ret = lastsmoking[0] + " " + NAME_UNIT;
        } else if(sKey.equals(NAME_LASTSMOKING+NAME_SESSION[1])) {
            ret = lastsmoking[1] + " " + NAME_UNIT;
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

public class SmokingConfig implements IExperimentProtocol {

    final private View mView;

    final private TextView mSmokingS1Header;
    final private TextView mSbrLastSmokingS1Header;
    final private RangeSeekBar mSbrLastSmokingS1;
    final private TextView mSmokingS2Header;
    final private TextView mSbrLastSmokingS2Header;
    final private RangeSeekBar mSbrLastSmokingS2;

    private Smoking mReport;

    public SmokingConfig(View view) {
        mView = view;

        mSmokingS1Header = (TextView) mView.findViewById(R.id.smokingS1Header);
        mSbrLastSmokingS1Header = (TextView) mView.findViewById(R.id.seekBarLastSmokingS1Header);
        mSbrLastSmokingS1 = (RangeSeekBar) mView.findViewById(R.id.seekBarLastSmokingS1);

        mSmokingS2Header = (TextView) mView.findViewById(R.id.smokingS2Header);
        mSbrLastSmokingS2Header = (TextView) mView.findViewById(R.id.seekBarLastSmokingS2Header);
        mSbrLastSmokingS2 = (RangeSeekBar) mView.findViewById(R.id.seekBarLastSmokingS2);

        mReport = new Smoking();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;

        mSmokingS1Header.setVisibility(mode);
        mSbrLastSmokingS1Header.setVisibility(mode);
        mSbrLastSmokingS1.setVisibility(mode);

        mSmokingS2Header.setVisibility(mode);
        mSbrLastSmokingS2Header.setVisibility(mode);
        mSbrLastSmokingS2.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.lastsmoking[0] = mSbrLastSmokingS1.getSelectedMaxValue().intValue();
        mReport.lastsmoking[1] = mSbrLastSmokingS2.getSelectedMaxValue().intValue();

        try {
            JSONObject slObj = new JSONObject();
            slObj.put(Smoking.NAME_SESSION[0], mReport.lastsmoking[0]);
            slObj.put(Smoking.NAME_SESSION[1], mReport.lastsmoking[1]);
            jsonObj.put(Smoking.NAME_LASTSMOKING, slObj);

            jsonWrapper.put(Smoking.NAME, jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonWrapper.toString();
    }

    static public Object cmdJSONparse(String cmd) {
        Smoking result = new Smoking();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(Smoking.NAME);
            JSONObject slObj = jObj.getJSONObject(Smoking.NAME_LASTSMOKING);

            result.lastsmoking[0] = slObj.getInt(Smoking.NAME_SESSION[0]);
            result.lastsmoking[1] = slObj.getInt(Smoking.NAME_SESSION[1]);

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
        mReport = (Smoking) cmdJSONparse(cmd);

        mSbrLastSmokingS1.setSelectedMaxValue(mReport.lastsmoking[0]);
        mSbrLastSmokingS2.setSelectedMaxValue(mReport.lastsmoking[1]);
    }
}
