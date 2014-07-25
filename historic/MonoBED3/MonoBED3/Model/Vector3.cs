using System;
namespace MonoBED3
{
	public class Vector3: Spatial
	{
		br.odb.utils.math.Vec3 vec;
		
		public Vector3 () {
			vec = new br.odb.utils.math.Vec3();
		}

		public Vector3 ( float x, float y, float z ) {
			vec = new br.odb.utils.math.Vec3( x, y, z );
		}
		
		public void setVec ( float x, float y, float z ) {
			setX( x );
			setY( y );
			setZ( z );
		}
		
		public virtual void moveTo( float x, float y, float z ) {
			vec.setX( x );
			vec.setY( y );
			vec.setZ( z );
		}
		
	
		public float getX() {
			return vec.getX();
		}
		
		public float getY() {
			return vec.getY();
		}
		
		public float getZ() {
			return vec.getZ();
		}
		
		public void setX( float X) {
			vec.setX( X );
		}
		
		public void setY( float Y) {
			vec.setY( Y );
		}
		
		public void setZ( float Z) {
			vec.setZ( Z );
		}		
		
		public br.odb.utils.math.Vec3 toODBVec3() {
			return new br.odb.utils.math.Vec3( vec );
		}
	}
}

