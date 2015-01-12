/**
 * 
 */
package br.odb.gamelib.android;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import br.odb.gamerendering.rendering.RasterImage;

/**
 * @author monty
 *
 */
public class AndroidResourceManager {

	private static HashMap< Resources, AndroidResourceManager > instances = new HashMap<Resources, AndroidResourceManager>();
	Resources res;
	
	public AndroidResourceManager(Resources res ) {
		this.res = res;
	}

	public RasterImage[] loadRasterImages(int[] resIds) {
		
		RasterImage[] toReturn = new RasterImage[ resIds.length ];
		
		for ( int c = 0; c < resIds.length; ++c ) {
			toReturn[ c ] = loadRasterImage( resIds[ c ] );
		}
		return toReturn;
	}

	public static AndroidResourceManager getInstance(Resources res ) {
		if ( instances.containsKey( res ) ) {
			
			return instances.get( res );
		} else {
			AndroidResourceManager arm = new AndroidResourceManager( res );
			instances.put( res, arm );
			return arm;
		}
	}

	public RasterImage loadRasterImage(int resId) {

		return new AndroidRasterImage( BitmapFactory.decodeResource( res, resId ) );
	}

	public void cleanupUnused() {
		// TODO Auto-generated method stub
		
	}

	public void preloadAssets() {
		// TODO Auto-generated method stub
		
	}

	public void unloadAll() {
		// TODO Auto-generated method stub
		
	}

}
