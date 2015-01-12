/**
 * 
 */
package br.odb.gamelib.android;

import android.content.Context;
import android.media.MediaPlayer;
import br.odb.gameapp.AbstractMediaPlayer;

/**
 * @author monty
 *
 */
public class AndroidMediaPlayer extends AbstractMediaPlayer {

	private MediaPlayer mp;

	/**
	 * 
	 */
	public AndroidMediaPlayer( Context context, int resId ) {
		mp = MediaPlayer.create( context, resId);
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.AbstractMediaPlayer#loop()
	 */
	@Override
	public void loop() {
		mp.stop();
		mp.setLooping( true );
		mp.start();
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.AbstractMediaPlayer#play()
	 */
	@Override
	public void play() {
		mp.start();
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.AbstractMediaPlayer#stop()
	 */
	@Override
	public void stop() {
		mp.stop();
	}
}
