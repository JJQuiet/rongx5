package com.example.rongx5.util.video;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.linphone.core.*;


import android.util.Log;

import com.example.rongx5.MainActivity;
import com.example.rongx5.R;
import com.example.rongx5.util.GlobalConfig;
import com.example.rongx5.util.ScreenUnit;
import com.example.rongx5.util.js.AppConfig;
import com.example.rongx5.util.js.JSunit;
import com.example.rongx5.util.js.NativeUnit;

import java.util.Timer;
import java.util.TimerTask;

import cn.hutool.core.util.StrUtil;

public class VideoCall {
    private static String TAG = "VideoCall";
    private Core core;

    private TextureView remoteView;
    private TextureView localView;

    private View videoGp;

    public static VideoCall _ins = null;

    private CoreListenerStub coreListener = new CoreListenerStub() {
        @Override
        public void onAccountRegistrationStateChanged(Core core, Account account, RegistrationState state, String message) {
            if (state == RegistrationState.Failed) {
                Toast.makeText(MainActivity.instance, "登录失败：" + message, Toast.LENGTH_SHORT).show();
            } else if (state == RegistrationState.Ok) {
                Log.i(TAG, "注册成功》》》》哈哈哈");
            }
        }

        @Override
        public void onCallStateChanged(
                Core core,
                Call call,
                Call.State state,
                String message
        ) {
            if (state.equals(Call.State.IncomingReceived)) {

                int len = core.getCalls().length;
                if (len > 1) {
                    call.terminate();
                    return;
                }

                Boolean isVideo = false;
                if (call.getVideoStats() != null) {
                    isVideo = true;
                }
                CallLog log = call.getCallLog();
                String fromAddress = log.getFromAddress().asString();
                String[] arr = fromAddress.split("sip:");

                fromAddress = fromAddress.split("sip:")[1];
                fromAddress = fromAddress.split("@")[0];

                ScreenUnit.wark(MainActivity.instance);
                //是头盔，注释下一行
                if (GlobalConfig.isHelmet == false)
                    JSunit.IncomingReceived(isVideo ? "1" : "0", fromAddress);

                //如果是头盔,解开下面一行注释
                if (GlobalConfig.isHelmet)
                    answer();
                return;
            }
            if (state.equals(Call.State.OutgoingInit)) {
                // First state an outgoing call will go through
                return;
            }
            if (state.equals(Call.State.OutgoingProgress)) {
                // Right after outgoing init
                return;
            }
            if (state.equals(Call.State.OutgoingRinging)) {
//                CommandExecution.callingLight(true);
                // This state will be reached upon reception of the 180 RINGING
                return;
            }
            if (state.equals(Call.State.Connected)) {
                // When the 200 OK has been received
                return;
            }
            if (state.equals(Call.State.StreamsRunning)) {
                Boolean isVideo = false;
                if (call.getVideoStats() != null) {
                    isVideo = true;
                }
                String remoteAddr = call.getRemoteAddress().getDisplayName();
                Log.i(TAG, "StreamsRunning " + remoteAddr);
                JSunit.StreamsRunning();
                if (isVideo) {
                    videoDisplay();
                }
                if (remoteAddr != null && remoteAddr.equals("conference")) {
                    //什么都不做
                } else {
                    if (isVideo) {
                        videoDisplay();
                        videoGp.setVisibility(View.VISIBLE);
                    }
                }

                return;
            }
            if (state.equals(Call.State.Paused)) {
                // When you put a call in pause, it will became Paused
//                    findViewById<Button>(R.id.pause).text = "Resume"
//                    findViewById<Button>(R.id.toggle_video).isEnabled = false
                return;
            }
            if (state.equals(Call.State.PausedByRemote)) {
                // When the remote end of the call pauses it, it will be PausedByRemote
                return;
            }
            if (state.equals(Call.State.Updating)) {
                // When we request a call update, for example when toggling video
                return;
            }
            if (state.equals(Call.State.UpdatedByRemote)) {
                // When the remote requests a call update
                return;
            }
            if (state.equals(Call.State.Released)) {
//                CommandExecution.callingLight(false);
                int len = core.getCalls().length;
                if (len > 0) {
                    return;
                }
                JSunit.Released();
                videoHide();
                videoGp.setVisibility(View.GONE);

                return;
            }
            if (state.equals(Call.State.Error)) {
                return;
            }

        }
    };

    public void outFullScreen() {
        this.videoGp.setVisibility(View.GONE);
    }

    public void showFullScreen() {
        this.videoGp.setVisibility(View.VISIBLE);
    }

    public VideoCall onCreateInit(TextureView remoteVideo, TextureView localVideo, View videoGp) {
        VideoCall._ins = this;
        this.videoGp = videoGp;
        this.remoteView = remoteVideo;
        this.localView = localVideo;
        this.videoGp.setVisibility(View.GONE);


        Factory factory = Factory.instance();
        factory.setDebugMode(GlobalConfig.isDebug, "HelloLinphone");
        core = factory.createCore(null, null, MainActivity.instance);
        PayloadType[] audioArr = core.getAudioPayloadTypes();
        PayloadType[] videoArr = core.getVideoPayloadTypes();
        for (PayloadType payloadType : audioArr) {
            if (payloadType.getMimeType().equals("PCMA")) {
                payloadType.enable(true);
                continue;
            }
            payloadType.enable(false);
        }
        for (PayloadType payloadType : videoArr) {
            if (payloadType.getMimeType().equals("H264")) {
                payloadType.enable(true);
                continue;
            }
            payloadType.enable(false);
        }

        // For video to work, we need two TextureViews:
        // one for the remote video and one for the local preview
        core.setNativeVideoWindowId(remoteVideo);

        ;
        // The local preview is a org.linphone.mediastream.video.capture.CaptureTextureView
        // which inherits from TextureView and contains code to keep the ratio of the capture video
        core.setNativePreviewWindowId(localVideo);

//
//        // Here we enable the video capture & display at Core level
//        // It doesn't mean calls will be made with video automatically,
//        // But it allows to use it later
//        core.enableVideoCapture(true);
//        core.enableVideoDisplay(true);
        core.setVideoCaptureEnabled(true);
        core.setVideoDisplayEnabled(true);
//
//        // When enabling the video, the remote will either automatically answer the update request
//        // or it will ask it's user depending on it's policy.
//        // Here we have configured the policy to always automatically accept video requests
        core.getVideoActivationPolicy().setAutomaticallyAccept(true);

        login();

        return this;

    }

    private void login() {

//        core.ensureRegistered();
//        core.enterForeground();
        // Asks the CaptureTextureView to resize to match the captured video's size ratio
        core.getConfig().setBool("video", "auto_resize_preview_to_keep_ratio", true);

        core.addListener(coreListener);
        core.start();


        core.clearProxyConfig();
        core.clearAllAuthInfo();
        core.clearAccounts();
        String username = AppConfig.leftHand;
        String password = AppConfig.handPwd;
        String domain = AppConfig.fspbx;
        TransportType transportType = TransportType.Udp;
        AuthInfo authInfo = Factory.instance().createAuthInfo(username, null, password, null, null, domain, null);

        AccountParams params = core.createAccountParams();
        Address identity = Factory.instance().createAddress("sip:" + AppConfig.leftHand + "@" + AppConfig.fspbx);
        params.setIdentityAddress(identity);

        Address address = Factory.instance().createAddress("sip:" + AppConfig.fspbx);
        address.setTransport(transportType);
        params.setServerAddress(address);
        params.setRegisterEnabled(true);
        Account account = core.createAccount(params);


        core.addAuthInfo(authInfo);
        core.addAccount(account);

        core.setDefaultAccount(account);
        // We will need the RECORD_AUDIO permission for video call

    }

    public void outgoingCall(Boolean isVideo, String phone) {
        if (core.getCalls().length > 1) {
            return;
        }

        this.initCamera();
        //        // As for everything we need to get the SIP URI of the remote and convert it to an Address
        String remoteSipUri = "sip:" + phone + "@" + AppConfig.fspbx;
        Address remoteAddress = Factory.instance().createAddress(remoteSipUri);
        if (remoteAddress == null) {
            // If address parsing fails, we can't continue with outgoing call process
            return;
        }
//                // We also need a CallParams object
//                // Create call params expects a Call object for incoming calls, but for outgoing we must use null safely
        CallParams params = core.createCallParams(null);
        if (params == null) {
            // Same for params
            return;
        }
        // We can now configure it
        // Here we ask for no encryption but we could ask for ZRTP/SRTP/DTLS
//        params.setMediaEncryption(MediaEncryption.None);
//        // If we wanted to start the call with video directly
//        params.enableVideo(isVideo);
        params.setVideoEnabled(true);
//        params.setVideoDirection();
//
//        // Finally we start the call
        core.inviteAddressWithParams(remoteAddress, params);
//        // Call process can be followed in onCallStateChanged callback from core listener
    }

    public void answer() {
        if (core.getCalls().length > 1) {
            return;
        }
        if (core.getCurrentCall() != null) {

            this.initCamera();

            Call current = core.getCurrentCall();
            CallStats stats = current.getVideoStats();
            if (stats != null) {
//                this.videoGp.setVisibility(View.VISIBLE);
            }

            CallParams params = core.createCallParams(current);
//            params.enableVideo(true);
            params.setVideoEnabled(true);
            params.setMediaEncryption(MediaEncryption.None);
            current.acceptWithParams(params);
//            current.accept();
        }
    }

    public void setMute(boolean isMute) {
        if (core.getCallsNb() == 0) return;
        Call currentCall = core.getCurrentCall();
        if (currentCall == null) {
            return;
        }
        currentCall.setMicrophoneMuted(isMute);


    }

    public void hangUp() {
//        CommandExecution.callingLight(false);
        videoHide();
        if (core.getCallsNb() == 0) {
            JSunit.Released();
            return;
        }
        Call currentCall = core.getCurrentCall();
        if (currentCall != null) {
            currentCall.terminate();
            JSunit.Released();
            return;
        }

        Call[] otherCalls = core.getCalls();
        for (Call other :
                otherCalls) {
            other.terminate();
        }
        JSunit.Released();

        // If the call state isn't paused, we can get it using core.currentCall

//                val call = if (core.currentCall != null) core.currentCall else core.calls[0]
//        call ?: return
//
//                // Terminating a call is quite simple
//                call.terminate()
    }

    private void videoHide() {
        this.localView.setVisibility(View.GONE);
        this.remoteView.setVisibility(View.GONE);
    }

    private void videoDisplay() {
        this.localView.setVisibility(View.VISIBLE);
        this.remoteView.setVisibility(View.VISIBLE);
    }

    public void toggleCamera() {
        if (core.getCallsNb() == 0) return;
        Call call = null;
        if (core.getCurrentCall() != null) {
            call = core.getCurrentCall();
        } else {
            call = core.getCalls()[0];
        }
        if (call == null) return;
        // Currently used camera
        String currentDevice = core.getVideoDevice();
        // Let's iterate over all camera available and choose another one
        // All devices will have a "Static picture" fake camera, and we don't want to use it
        for (String camera : core.getVideoDevicesList()) {
            if (!camera.equals(currentDevice) && !camera.equals("StaticImage: Static picture")) {
                core.setVideoDevice(camera);
                break;
            }
        }
    }


    private void initCamera() {
//        String currentDevice = core.getVideoDevice();
//        //小调度台
//        for (String camera : core.getVideoDevicesList()) {
//            // All devices will have a "Static picture" fake camera, and we don't want to use it
//            if (camera != currentDevice && camera != "BackFacingCamera0") {
//                core.setVideoDevice(camera);
//            }
//        }
        //景德镇天波的
//        if(currentDevice.equals("FrontFacingCamera1") == false)
//        {
//            core.setVideoDevice("FrontFacingCamera1");
//        }
//        for (String camera : core.getVideoDevicesList()) {
//            // All devices will have a "Static picture" fake camera, and we don't want to use it
//            if (camera != currentDevice && camera != "BackFacingCamera0") {
//
//            }
//        }
//        if(currentDevice.equals("FrontFacingCamera1") == false)
//        {
//            for (String device:
//                    core.getVideoDevicesList()) {
//                if(device.equals("FrontFacingCamera1"))
//                {
//                    core.setVideoDevice(device);
//                    break;
//                }
//                if(device.equals("BackFacingCamera0"))
//                {
//                    core.setVideoDevice(device);
//                    break;
//                }
//            }
        // Let's iterate over all camera available and choose another one
//            for (camera in core.getVideoDevicesList()) {
//                // All devices will have a "Static picture" fake camera, and we don't want to use it
//                if (camera != currentDevice && camera != "StaticImage: Static picture") {
//                    core.videoDevice = camera
//                    break
//                }
//            }
//        }


    }


    private void toggleVideo() {
//        if (core.callsNb == 0) return
//                val call = if (core.currentCall != null) core.currentCall else core.calls[0]
//        call ?: return
//
//        // We will need the CAMERA permission for video call
//        if (packageManager.checkPermission(Manifest.permission.CAMERA, packageName) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
//            return
//        }
//
//        // To update the call, we need to create a new call params, from the call object this time
//        val params = core.createCallParams(call)
//        // Here we toggle the video state (disable it if enabled, enable it if disabled)
//        // Note that we are using currentParams and not params or remoteParams
//        // params is the object you configured when the call was started
//        // remote params is the same but for the remote
//        // current params is the real params of the call, resulting of the mix of local & remote params
//        params?.enableVideo(!call.currentParams.videoEnabled())
//        // Finally we request the call update
//        call.update(params)
//
//        // Note that when toggling off the video, TextureViews will keep showing the latest frame displayed
    }


//    private void pauseOrResume() {
//        if (core.getCallsNb() == 0) return;
//        Call call = null;
//        if (core.getCurrentCall() != null) {
//            call = core.getCurrentCall();
//        } else
//        {
//            call = core.getCalls()[0];
//        }
//
//        if (call.getState() != Call.State.Paused && call.getState() != Call.State.Pausing) {
//            // If our call isn't paused, let's pause it
//            call.pause();
//        } else if (call.getState() != Call.State.Resuming) {
//            // Otherwise let's resume it
//            call.resume();
//        }
//    }
}
