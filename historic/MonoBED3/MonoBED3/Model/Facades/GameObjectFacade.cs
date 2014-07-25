using System;
namespace MonoBED3
{
	public class GameObjectFacade: FacadeConnection, Spatial
	{
		private GameObject instance;
		
		public GameObjectFacade ()
		{
			instance = null;
		}
		
		public int getId() {
			return instance.getId();
		}
		
		public void connect( Object obj)
		{
			instance = (GameObject)obj;			
		}
		
		public void disconnect()
		{
			
		}
		
		public Object getOwnerCopy()
		{
			Object obj = instance.clone();
			return obj;
		}
		
		public Object getObject()
		{
			return instance;
		}
		
		public float getX()
		{
			return instance.getX();
		}
		public float getY()
		{
			return instance.getY();
		}
		public float getZ()
		{
			return instance.getZ();
		}
		
		public void moveTo( float x, float y, float z ) {
			instance.moveTo( x, y, z );	
		}
		
		public void setX( float X)
		{
			instance.setX(X);
		}
		
		public void setY( float Y)
		{
			instance.setY(Y);
		}
		
		public void setZ( float Z)
		{
			instance.setZ(Z);
		}
	}
}

