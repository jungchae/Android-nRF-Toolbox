package no.nordicsemi.android.nrftoolbox.uart.custom.type;

/**
 * Created by jungchae on 17. 4. 23.
 */

public interface IReport {
    /** Get Maximum Trial Count
     *  If there is no BP results, returns -1 */
    int getMaxTrialCount();

    /** returns boolean value, requested using sKey */
    boolean getBoolean(String sKey);

    /** returns integer value, requested using sKey */
    int getInt(String sKey);

    /** returns string value, requested using sKey */
    String getString(String sKey);

    /** Get Registered file informations,
     *  There is TRIAL_COUNT objects in String[]
     *  If there is no BP results, returns null objects*/
    String[] getRegisteredFileInfo();

    /** Get Registered BP measured values,
     *  There is TRIAL_COUNT objects in BloodPressure[]
     *  If there is no BP results, returns null objects*/
    BloodPressure[] getBloodPressure();
}
