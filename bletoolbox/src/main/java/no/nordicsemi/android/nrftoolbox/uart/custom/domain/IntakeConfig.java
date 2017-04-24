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

class Intake implements IReport {
    public static final String NAME = "Intake";
    public static final String NAME_LASTCAFFEINE = "lastcaffeine";
    public static final String NAME_LASTALCOHOL = "lastalcohol";
    public static final String NAME_UNIT = "hour";
    public static final String[] NAME_SESSION = {"s1", "s2"};
    public static final int SESSION_CNT = 2;
    public int[] lastcaffeine = new int[SESSION_CNT];
    public int[] lastalcohol = new int[SESSION_CNT];

    @Override
    public int getMaxTrialCount() {
        return SESSION_CNT; // Session Cnt
    }

    @Override
    public boolean getBoolean(String sKey) {
        return false;
    }

    @Override
    public int getInt(String sKey) {
        int ret = -1;

        if(sKey.equals(NAME_LASTCAFFEINE+NAME_SESSION[0])) {
            ret = lastcaffeine[0];
        } else if(sKey.equals(NAME_LASTCAFFEINE+NAME_SESSION[1])) {
            ret = lastcaffeine[1];
        } else if(sKey.equals(NAME_LASTALCOHOL+NAME_SESSION[0])) {
            ret = lastalcohol[0];
        } else if(sKey.equals(NAME_LASTALCOHOL+NAME_SESSION[1])) {
            ret = lastalcohol[1];
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

        if(sKey.equals(NAME_LASTCAFFEINE+NAME_SESSION[0])) {
            ret = lastcaffeine[0] + " " + NAME_UNIT;
        } else if(sKey.equals(NAME_LASTCAFFEINE+NAME_SESSION[1])) {
            ret = lastcaffeine[1] + " " + NAME_UNIT;
        } else if(sKey.equals(NAME_LASTALCOHOL+NAME_SESSION[0])) {
            ret = lastalcohol[0] + " " + NAME_UNIT;
        } else if(sKey.equals(NAME_LASTALCOHOL+NAME_SESSION[1])) {
            ret = lastalcohol[1] + " " + NAME_UNIT;
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

public class IntakeConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mIntakeS1Header;
    final private TextView mSbrLastCaffeineS1Header;
    final private RangeSeekBar mSbrLastCaffeineS1;
    final private TextView mSbrLastAlcoholS1Header;
    final private RangeSeekBar mSbrLastAlcoholS1;

    final private TextView mIntakeS2Header;
    final private TextView mSbrLastCaffeineS2Header;
    final private RangeSeekBar mSbrLastCaffeineS2;
    final private TextView mSbrLastAlcoholS2Header;
    final private RangeSeekBar mSbrLastAlcoholS2;

    private Intake mReport;

    public IntakeConfig(View view) {
        mView = view;

        mIntakeS1Header = (TextView) mView.findViewById(R.id.intakeS1Header);
        mSbrLastCaffeineS1Header = (TextView) mView.findViewById(R.id.seekBarLastCaffeineS1Header);
        mSbrLastCaffeineS1 = (RangeSeekBar) mView.findViewById(R.id.seekBarLastCaffeineS1);
        mSbrLastAlcoholS1Header = (TextView) mView.findViewById(R.id.seekBarLastAlcoholS1Header);
        mSbrLastAlcoholS1 = (RangeSeekBar) mView.findViewById(R.id.seekBarLastAlcoholS1);

        mIntakeS2Header = (TextView) mView.findViewById(R.id.intakeS2Header);
        mSbrLastCaffeineS2Header = (TextView) mView.findViewById(R.id.seekBarLastCaffeineS2Header);
        mSbrLastCaffeineS2 = (RangeSeekBar) mView.findViewById(R.id.seekBarLastCaffeineS2);
        mSbrLastAlcoholS2Header = (TextView) mView.findViewById(R.id.seekBarLastAlcoholS2Header);
        mSbrLastAlcoholS2 = (RangeSeekBar) mView.findViewById(R.id.seekBarLastAlcoholS2);

        mReport = new Intake();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;

        mIntakeS1Header.setVisibility(mode);
        mSbrLastCaffeineS1Header.setVisibility(mode);
        mSbrLastCaffeineS1.setVisibility(mode);
        mSbrLastAlcoholS1Header.setVisibility(mode);
        mSbrLastAlcoholS1.setVisibility(mode);

        mIntakeS2Header.setVisibility(mode);
        mSbrLastCaffeineS2Header.setVisibility(mode);
        mSbrLastCaffeineS2.setVisibility(mode);
        mSbrLastAlcoholS2Header.setVisibility(mode);
        mSbrLastAlcoholS2.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.lastcaffeine[0] = mSbrLastCaffeineS1.getSelectedMaxValue().intValue();
        mReport.lastcaffeine[1] = mSbrLastCaffeineS2.getSelectedMaxValue().intValue();
        mReport.lastalcohol[0] = mSbrLastAlcoholS1.getSelectedMaxValue().intValue();
        mReport.lastalcohol[1] = mSbrLastAlcoholS2.getSelectedMaxValue().intValue();

        try {
            JSONObject slObj = new JSONObject();
            slObj.put(Intake.NAME_SESSION[0], mReport.lastcaffeine[0]);
            slObj.put(Intake.NAME_SESSION[1], mReport.lastcaffeine[1]);
            jsonObj.put(Intake.NAME_LASTCAFFEINE, slObj);

            slObj = new JSONObject();
            slObj.put(Intake.NAME_SESSION[0], mReport.lastalcohol[0]);
            slObj.put(Intake.NAME_SESSION[1], mReport.lastalcohol[1]);
            jsonObj.put(Intake.NAME_LASTALCOHOL, slObj);

            jsonWrapper.put(Intake.NAME, jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonWrapper.toString();
    }

    static public Object cmdJSONparse(String cmd) {
        Intake result = new Intake();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(Intake.NAME);

            JSONObject slObj = jObj.getJSONObject(Intake.NAME_LASTCAFFEINE);
            result.lastcaffeine[0] = slObj.getInt(Sleep.NAME_SESSION[0]);
            result.lastcaffeine[1] = slObj.getInt(Sleep.NAME_SESSION[1]);

            slObj = jObj.getJSONObject(Intake.NAME_LASTALCOHOL);
            result.lastalcohol[0] = slObj.getInt(Sleep.NAME_SESSION[0]);
            result.lastalcohol[1] = slObj.getInt(Sleep.NAME_SESSION[1]);
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

        mReport = (Intake)cmdJSONparse(cmd);

        mSbrLastCaffeineS1.setSelectedMaxValue(mReport.lastcaffeine[0]);
        mSbrLastCaffeineS2.setSelectedMaxValue(mReport.lastcaffeine[1]);
        mSbrLastAlcoholS1.setSelectedMaxValue(mReport.lastalcohol[0]);
        mSbrLastAlcoholS2.setSelectedMaxValue(mReport.lastalcohol[1]);
    }
}
