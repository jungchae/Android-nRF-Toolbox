package no.nordicsemi.android.nrftoolbox.uart.custom.domain;

import android.widget.RadioGroup;

/**
 * Created by jungchae on 17. 4. 19.
 */

public interface IExperimentProtocol {
    RadioGroup getEolGroup();
    void reqeustTemplate(boolean flag, String cmd);
    String cmdJSONstringify();
}
