using System;
namespace MonoBED3
{
	public interface Spatial
	{
		float getX();
		float getY();
		float getZ();
		void setX( float X);
		void setY( float Y);
		void setZ( float Z);		
		void moveTo( float x, float y, float z );
	}
}

