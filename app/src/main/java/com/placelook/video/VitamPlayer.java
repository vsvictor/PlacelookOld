package com.placelook.video;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.placelook.R;

import java.util.HashMap;
import java.util.Map;

public class VitamPlayer extends Activity{
	
	private VideoView videoView;
	private String streamURL;// = "rtmp://mn-l.mncdn.com/showtv/showtv2";
	private Bundle extras;
	private int idSession = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vitam);
		if(!LibsChecker.checkVitamioLibs(this))
			return;
		extras = getIntent().getExtras();
		idSession = extras.getInt("idSession");
		streamURL = extras.getString("url");
		Log.i("PlayActivity", streamURL);

		videoView = (VideoView)findViewById(R.id.videoView);
		String[] keys={"tune","cpuflags","cpuflags"};
		String[] args={"zerolatency","vfpv3", "neon"};

		Map<String, String> opt = new HashMap<String, String>();
		opt.put("tune","zerolatency");
		opt.put("cpuflags","vfpv3");
		opt.put("cpuflags","neon");
		videoView.setVideoURI(Uri.parse(streamURL), opt);
		videoView.requestFocus();
		MediaController controller = new MediaController(this);
		//controller.setRotation(90);
		videoView.setHardwareDecoder(true);
		videoView.setMediaController(controller);
		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer arg0) {
				videoView.start();
				
			}
		});
	}
	
}
