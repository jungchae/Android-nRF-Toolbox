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

class Intake {
    public static final String NAME = "Intake";
    public static final String NAME_LASTCAFFEINE = "lastcaffeine";
    public static final String NAME_LASTALCOHOL = "lastalcohol";
    public static final String NAME_UNIT = "hour";
    public int lastcaffeine;
    public int lastalcohol;
}

public class IntakeConfig implements IExperimentProtocol {

    final private View mView;
    final private TextView mSbrLastCaffeineHeader;
    final private RangeSeekBar mSbrLastCaffeine;
    final private TextView mSbrLastAlcoholHeader;
    final private RangeSeekBar mSbrLastAlcohol;

    private Intake mReport;

    public IntakeConfig(View view) {
        mView = view;
        mSbrLastCaffeineHeader = (TextView) mView.findViewById(R.id.seekBarLastCaffeineHeader);
        mSbrLastCaffeine = (RangeSeekBar) mView.findViewById(R.id.seekBarLastCaffeine);
        mSbrLastAlcoholHeader = (TextView) mView.findViewById(R.id.seekBarLastAlcoholHeader);
        mSbrLastAlcohol = (RangeSeekBar) mView.findViewById(R.id.seekBarLastAlcohol);

        mReport = new Intake();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSbrLastCaffeineHeader.setVisibility(mode);
        mSbrLastCaffeine.setVisibility(mode);
        mSbrLastAlcoholHeader.setVisibility(mode);
        mSbrLastAlcohol.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.lastcaffeine = mSbrLastCaffeine.getSelectedMaxValue().intValue();
        mReport.lastalcohol = mSbrLastAlcohol.getSelectedMaxValue().intValue();

        try {
            jsonObj.put(Intake.NAME_LASTCAFFEINE, mReport.lastcaffeine);
            jsonObj.put(Intake.NAME_LASTALCOHOL, mReport.lastalcohol);

            jsonWrapper.put(Intake.NAME, jsonObj);
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
            JSONObject jObj = jWrapper.getJSONObject(Intake.NAME);

            mReport.lastcaffeine = jObj.getInt(Intake.NAME_LASTCAFFEINE);
            mReport.lastalcohol = jObj.getInt(Intake.NAME_LASTALCOHOL);

            mSbrLastCaffeine.setSelectedMaxValue(mReport.lastcaffeine);
            mSbrLastAlcohol.setSelectedMaxValue(mReport.lastalcohol);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
