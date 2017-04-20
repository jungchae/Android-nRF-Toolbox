package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import no.nordicsemi.android.nrftoolbox.R;

/**
 * Created by jungchae on 17. 4. 19.
 */

class Disease {
    public static final String NAME = "Disease";
    public static final String NAME_ARRYTHMIAS = "arrythmias";
    public static final String NAME_ANTIHYPERTENSIVE = "antihypertensive";
    public static final String NAME_DIABETES = "diabetes";
    public static final String NAME_PREGNANT = "pregnant";
    public static final String NAME_UNIT = "";
    public boolean arrythmias;
    public boolean antihypertensive;
    public boolean diabetes;
    public boolean pregnant;
}

public class DiseaseConfig implements IExperimentProtocol {

    final private View mView;
    final private Switch mSwArrhythmias;
    final private Switch mSwAntihypertensive;
    final private Switch mSwDiabetes;
    final private Switch mSwPregnant;

    private Disease mReport;

    public DiseaseConfig(View view) {
        mView = view;
        mSwArrhythmias = (Switch) mView.findViewById(R.id.switchArrhythmias);
        mSwAntihypertensive = (Switch) mView.findViewById(R.id.switchAntihypertensive);
        mSwDiabetes = (Switch) mView.findViewById(R.id.switchDiabetes);
        mSwPregnant = (Switch) mView.findViewById(R.id.switchPregnant);

        mReport = new Disease();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mSwArrhythmias.setVisibility(mode);
        mSwAntihypertensive.setVisibility(mode);
        mSwDiabetes.setVisibility(mode);
        mSwPregnant.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.arrythmias = mSwArrhythmias.isChecked();
        mReport.antihypertensive = mSwAntihypertensive.isChecked();
        mReport.diabetes = mSwDiabetes.isChecked();
        mReport.pregnant = mSwPregnant.isChecked();

        try {
            jsonObj.put(Disease.NAME_ARRYTHMIAS, mReport.arrythmias);
            jsonObj.put(Disease.NAME_ANTIHYPERTENSIVE, mReport.antihypertensive);
            jsonObj.put(Disease.NAME_DIABETES, mReport.diabetes);
            jsonObj.put(Disease.NAME_PREGNANT, mReport.pregnant);
            jsonWrapper.put(Disease.NAME, jsonObj);
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
            JSONObject jObj = jWrapper.getJSONObject(Disease.NAME);

            mReport.arrythmias = jObj.getBoolean(Disease.NAME_ARRYTHMIAS);
            mReport.antihypertensive = jObj.getBoolean(Disease.NAME_ANTIHYPERTENSIVE);
            mReport.diabetes = jObj.getBoolean(Disease.NAME_DIABETES);
            mReport.pregnant = jObj.getBoolean(Disease.NAME_PREGNANT);

            mSwArrhythmias.setChecked(mReport.arrythmias);
            mSwAntihypertensive.setChecked(mReport.antihypertensive);
            mSwDiabetes.setChecked(mReport.diabetes);
            mSwPregnant.setChecked(mReport.pregnant);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
