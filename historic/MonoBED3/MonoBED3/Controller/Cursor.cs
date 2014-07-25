using System;
namespace MonoBED3
{
	public class Cursor: br.odb.utils.math.Vec3
	{
		public Cursor ()
		{
		}
		
		public override String toString() {			
			return "cursor at " + getX() + ", " + getY() + ", " + getZ();
		}
	}
}