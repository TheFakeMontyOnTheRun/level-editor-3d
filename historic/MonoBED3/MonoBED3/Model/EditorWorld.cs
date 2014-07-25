using System;
using System.Collections;
using br.odb.libscene;
using br.odb.utils.math;

//-----------------------------------------------------------------------------
namespace MonoBED3
{
//-----------------------------------------------------------------------------	
	public class EditorWorld : br.odb.libscene.World
	{
//-----------------------------------------------------------------------------
		public EditorWorld() {
			addNewSector();
		}
//-----------------------------------------------------------------------------		
		public void copyFrom( EditorWorld other ) {

			foreach ( Sector s in other ) {
				addSector( new Sector( s ) );				
			}			
		}
//-----------------------------------------------------------------------------		
		public override Sector getSector (int index)
		{
			if (index >= getTotalSectors ())
				return null;

			return base.getSector (index);
		}
//-----------------------------------------------------------------------------
		public override void addSector( Sector s )
		{
			s.setId( getTotalSectors() );
			base.addSector( s );	
		}		
//-----------------------------------------------------------------------------
		public Sector addNewSector()
		{
			Sector sector = new Sector();
			addSector( sector );
			return sector;
		}
//-----------------------------------------------------------------------------
		public void eraseAll() {
			int total = getTotalSectors ();

			for (int c = 0; c < total; ++c)
				removeSector (0);

			addNewSector ();
		}
//-----------------------------------------------------------------------------		
		public Sector getSectorAt( Vec3 pos ) {


			foreach ( Sector s in this ) {
				if ( s.contains( pos ) ) {
					
					return s;
				}
			}

			return getSector (0);
		}
//-----------------------------------------------------------------------------		
		public override void removeSector( Sector s )  {
			base.removeSector( s );
			fixIds();				      
		}
//-----------------------------------------------------------------------------
		public void fixIds() {

			for ( int c = 0; c < getTotalSectors(); ++c )
				getSector( c ).setId( c );
		}
//-----------------------------------------------------------------------------
	}
//-----------------------------------------------------------------------------	
}
//-----------------------------------------------------------------------------