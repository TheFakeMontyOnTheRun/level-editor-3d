using System;
namespace MonoBED3
{
	public class SectorFacade: GameObjectFacade
	{
		public SectorFacade ()
		{
		}

		public void setId( int id ) {

			Sector sector = (Sector)getObject();
			sector.setId( id );
		}
		
		public  float getDX()
		{
			Sector sector = (Sector)getObject();
			return sector.getDX();
		}
		
		public  float getDY()
		{
			Sector sector = (Sector)getObject();
			return sector.getDY();
		}


		
		public  float getDZ()
		{
			Sector sector = (Sector)getObject();
			return sector.getDZ();
		}
		
	}	
}

