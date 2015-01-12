/**
 * 
 */
package br.odb.gamelib.android;

import android.media.MediaPlayer;
import br.odb.gameapp.MediaPlayerWrapper;

/**
 * @author monty
 *
 */
public class MediaPlayerAndroidWrapper extends MediaPlayerWrapper {

	MediaPlayer mp;
	
	
	public MediaPlayerAndroidWrapper( MediaPlayer mp ) {
		this.mp = mp;
	}
	
	@Override
	public void setLooping(boolean b) {
		mp.setLooping( b );
	}


	@Override
	public boolean isPlaying() {
		return mp.isPlaying();
	}

	@Override
	public void stop() {
		mp.stop();
	}

	@Override
	public void start(float l, float r) {
		start();
		setVolume( l, r );		
	}

	@Override
	public void start() {

		mp.start();
	}

	@Override
	public void setVolume(float l, float r) {
		mp.start();
		mp.setVolume( l, r );
	}
}
