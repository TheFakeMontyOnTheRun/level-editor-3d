using System;
using br.odb.libscene;
//-----------------------------------------------------------------------------
namespace MonoBED3
{
//-----------------------------------------------------------------------------
	public class Utils
	{
//-----------------------------------------------------------------------------
		public static byte clamp( byte x, byte min, byte max ) {

			if ( x < min )
				return min;

			if ( x > max )
				return max;

			return x;
		}
//-----------------------------------------------------------------------------
		public static Gdk.Color getGtkColor( Color c, int light ) {

			return new Gdk.Color( ( byte )( c.getR() + light ), ( byte )( c.getG() + light ), ( byte )( c.getB() + light ) );
		}
//-----------------------------------------------------------------------------
		public static Color getColor( Gdk.Color c ) {
			Color r = new Color();

			r.setR( ( int ) ( 255 * ((float)c.Red) / ((float)ushort.MaxValue) ) );
			r.setG( ( int ) ( 255 * ((float)c.Green) / ((float)ushort.MaxValue) ) );
			r.setB( ( int ) ( 255 * ((float)c.Blue) / ((float)ushort.MaxValue) ) );	

			return r;
		}
//-----------------------------------------------------------------------------
	}
//-----------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
