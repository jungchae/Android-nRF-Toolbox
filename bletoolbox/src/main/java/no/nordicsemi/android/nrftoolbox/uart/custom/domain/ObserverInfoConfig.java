package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.BloodPressure;
import no.nordicsemi.android.nrftoolbox.uart.custom.type.IReport;

/**
 * Created by jungchae on 17. 4. 19.
 */

class ObserverInfo implements IReport {
    public static final String NAME = "ObserverInfo";
    public static final String NAME_UID = "oid";
    public static final String NAME_GENDER = "gender";
    public static final String NAME_BIRTHDATE = "birthdate";
    public static final String NAME_EMAIL = "email";
    public static final String NAME_UNIT = "";
    public String oid;
    public String gender;
    public String birthdate;
    public String email;

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
            case NAME_UID:
                ret = Integer.parseInt(oid);
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
            case NAME_UID:
                ret = oid;
                break;
            case NAME_GENDER:
                ret = gender;
                break;
            case NAME_BIRTHDATE:
                ret = birthdate;
                break;
            case NAME_EMAIL:
                ret = email;
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

public class ObserverInfoConfig implements IExperimentProtocol {

    final private View mView;
    final private EditText mName;
    final private EditText mEmail;
    final private TextView mEolGroupHeader;
    final private RadioGroup mEolGroup;
    final private RadioButton mEolGroup_lf;
    final private RadioButton mEolGroup_cr;
    final private RadioButton mEolGroup_cr_lf;
    final private TextView mDatePickerHeader;
    final private DatePicker mDatePicker;

    private ObserverInfo mReport;

    public ObserverInfoConfig(View view) {
        mView = view;
        mName = (EditText) mView.findViewById(R.id.editOname);
        mEmail = (EditText) mView.findViewById(R.id.editEmail);

        mEolGroupHeader = (TextView) mView.findViewById(R.id.uart_eol_header);
        mEolGroup = (RadioGroup) mView.findViewById(R.id.uart_eol);
        mEolGroup_lf = (RadioButton) mView.findViewById(R.id.uart_eol_lf);
        mEolGroup_cr = (RadioButton) mView.findViewById(R.id.uart_eol_cr);
        mEolGroup_cr_lf = (RadioButton) mView.findViewById(R.id.uart_eol_cr_lf);

        mDatePickerHeader = (TextView) mView.findViewById(R.id.datePickerHeader);
        mDatePicker = (DatePicker) mView.findViewById(R.id.datePicker);

        mReport = new ObserverInfo();
    }

    public void reqeustTemplate(boolean bVisible, String cmd) {
        int mode = (bVisible) ? View.VISIBLE : View.GONE;
        mName.setVisibility(mode);
        mName.setHint(R.string.exp_oname_hint);
        mEmail.setVisibility(mode);

        mEolGroup.setVisibility(mode);
        mEolGroupHeader.setText(R.string.exp_eol_header_gender);
        mEolGroup_lf.setText(R.string.exp_eol_lf_header_gender_1);
        mEolGroup_cr.setText(R.string.exp_eol_cr_header_gender_1);
        mEolGroup_cr_lf.setText(R.string.exp_eol_cr_lf_header_gender_1);
        mEolGroup.check(R.id.uart_eol_cr_lf);

        mDatePickerHeader.setVisibility(mode);
        mDatePicker.setVisibility(mode);

        uiConfiguration(cmd);
    }

    public String cmdJSONstringify() {
        JSONObject jsonWrapper = new JSONObject();
        JSONObject jsonObj = new JSONObject();

        mReport.oid = mName.getText().toString();
        mReport.email = mEmail.getText().toString();
        if(mEolGroup != null) {
            int i = mEolGroup.getCheckedRadioButtonId();
            if (i == R.id.uart_eol_lf) {
                mReport.gender = mEolGroup_lf.getText().toString();
            } else if (i == R.id.uart_eol_cr) {
                mReport.gender = mEolGroup_cr.getText().toString();
            } else {
                mReport.gender = mEolGroup_cr_lf.getText().toString();
            }
        }
        mReport.birthdate = mDatePicker.getDayOfMonth() + "/"
                + mDatePicker.getMonth() + "/"
                + mDatePicker.getYear();

        try {
            jsonObj.put(ObserverInfo.NAME_UID, mReport.oid);
            jsonObj.put(ObserverInfo.NAME_EMAIL, mReport.email);
            jsonObj.put(ObserverInfo.NAME_GENDER, mReport.gender);
            jsonObj.put(ObserverInfo.NAME_BIRTHDATE, mReport.birthdate);

            jsonWrapper.put(ObserverInfo.NAME, jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonWrapper.toString();
    }

    static public Object cmdJSONparse(String cmd) {
        ObserverInfo result = new ObserverInfo();

        JSONObject jWrapper = null;
        try {
            jWrapper = new JSONObject(cmd);
            JSONObject jObj = jWrapper.getJSONObject(ObserverInfo.NAME);
            result.oid= jObj.getString(ObserverInfo.NAME_UID);
            result.email= jObj.getString(ObserverInfo.NAME_EMAIL);
            result.gender= jObj.getString(ObserverInfo.NAME_GENDER);
            result.birthdate= jObj.getString(ObserverInfo.NAME_BIRTHDATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public RadioGroup getEolGroup(){
        return mEolGroup;
    }

    private void uiConfiguration(String cmd) {
        if(cmd=="") return;

        mReport = (ObserverInfo) cmdJSONparse(cmd);

        mName.setText(mReport.oid);
        mEmail.setText(mReport.email);
        if( mReport.gender != null && mReport.gender.equals(mEolGroup_lf.getText().toString()) ) {
            mEolGroup.check(R.id.uart_eol_lf);
        } else if( mReport.gender != null && mReport.gender.equals(mEolGroup_cr.getText().toString()) ) {
            mEolGroup.check(R.id.uart_eol_cr);
        } else {
            mEolGroup.check(R.id.uart_eol_cr_lf);
        }
        if(mReport.birthdate !=null) {
            String[] date = mReport.birthdate.split("/");
            mDatePicker.updateDate( Integer.parseInt(date[2])
                    , Integer.parseInt(date[1])
                    , Integer.parseInt(date[0]) );
        }
    }
}
