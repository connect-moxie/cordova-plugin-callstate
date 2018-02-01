package cordova.plugin.callstate;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;


public class CallState extends CordovaPlugin {

    CallStateListener listener;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        prepareListener();

        listener.setCallbackContext(callbackContext);

        return true;
    }

    private void prepareListener() {
        if (listener == null) {
            listener = new CallStateListener();
            TelephonyManager TelephonyMgr = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyMgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}

class CallStateListener extends PhoneStateListener {

    private CallbackContext callbackContext;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (callbackContext == null) return;

        String status = "";

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
            status = "IDLE";
            break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
            status = "OFFHOOK";
            break;

            case TelephonyManager.CALL_STATE_RINGING:
            status = "RINGING";
            break;
        }


        try {
            JSONObject phoneState = new JSONObject();
            phoneState.put("state", status);
            phoneState.put("number", incomingNumber);
            status = phoneState.toString();
        } catch (JSONException e) {
            status = "{state: '" + status + "', number: '" + incomingNumber + "'}";
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, status);
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
    }
}
