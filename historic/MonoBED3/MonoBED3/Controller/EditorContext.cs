using System;
using System.Runtime.CompilerServices;
using br.odb.liboldfart.wavefront_obj;
using br.odb.libscene;
using br.odb.utils.math;
using Gtk;

//-----------------------------------------------------------------------------
namespace MonoBED3
{
//-----------------------------------------------------------------------------	
	public class EditorContext
	{
		private bool updating;
		private bool needsCommit;
		private String filename;
		private EditorWorld world;
		private Sector currentSector;
		private Gtk.Window parentWindow;
		private Cursor cursor;
		ContextListener master;
//-----------------------------------------------------------------------------	
		public void setCurrent( Sector sector ) {
			
			currentSector = sector;
		}
//-----------------------------------------------------------------------------	
		public void addSector ( Sector sector )
		{
			world.addSector( sector );
		}
//-----------------------------------------------------------------------------	
		public EditorWorld getWorld() {			
			return world;
		}		
//-----------------------------------------------------------------------------	
		public void setBroadcaster( ContextListener cl ) {
			master = cl;
		}
//-----------------------------------------------------------------------------		
		public EditorContext (Gtk.Window parent)
		{
			updating = false;
			world = new EditorWorld();
			cursor = new Cursor();
			parentWindow = parent;
			Logger.log("------------------ NEW EDITOR CONTEXT CREATED ------------------");			
			needsCommit = false;
			filename = null;
		}		
//-----------------------------------------------------------------------------	
		public Cursor getCursor()
		{
			return cursor;
		}
//-----------------------------------------------------------------------------	
		public void setCurrentSectorFromCursor() {

			Sector s = world.getSectorAt (new Vec3( cursor.getX(), cursor.getY(), cursor.getZ() ));

			if ( s != world.getSector( 0 ) )
				setCurrent( s );
		}
//-----------------------------------------------------------------------------	
		public Sector getCurrentSector()
		{
			return currentSector as Sector;		
		}
//-----------------------------------------------------------------------------
		public Sector addNewSector()
		{
			return addNewSector( true );
		}
//-----------------------------------------------------------------------------	
		public Sector addNewSector( bool shouldNotify )
		{		
			currentSector = world.addNewSector();
		
			currentSector.setIsMaster( true );
			
			if ( shouldNotify )
				notifyChange();
			
			return currentSector as Sector;
		}
//-----------------------------------------------------------------------------		
		public bool isCommitNeeded()
		{
			return needsCommit;			
		}
//-----------------------------------------------------------------------------		
		public void commit()
		{
			needsCommit = false;
		}
//-----------------------------------------------------------------------------		
		public Sector getSector(int index)
		{
			if (world != null)
			{
				Sector sector = world.getSector(index);
				return sector;
			}
		
			return null;
		}		
//-----------------------------------------------------------------------------			
		public int getTotalSectors() {
			return world.getTotalSectors();
		}
//-----------------------------------------------------------------------------			
		public void importBZK2( String path, bool ignoreNonLeafs ) {
			
			world.eraseAll();
			filename = path;
			BZK2Helper helper = new BZK2Helper();
			helper.load( path );
			Sector s;
			Sector s2;

			Logger.log("------carregado------");
			
			for ( int c = 0; c < helper.getTotalSectors(); ++c )  {
				
				if ( c == 0 && ignoreNonLeafs )
					continue;
				
				s = helper.getSector( c );
				
				if ( s.isMaster() || !ignoreNonLeafs ) {
					s2 = (Sector)addNewSector (false);

					
					s2.moveTo( new Vec3( s.getX0(), s.getY0(), s.getZ0() ) );
					s2.setDX( s.getDX() );
					s2.setDY( s.getDY() );
					s2.setDZ( s.getDZ() );
					
					for ( int d = 0; d < 6; ++d ) {
						
						s2.setLink( d, s.getLink( d ) );
						s2.setColor( new Color( s.getColor( d ) ), d );
 					}					
				}
			}
			
			this.notifyChange();
		}
//-----------------------------------------------------------------------------	
		public void loadSnapshot( String path, bool ignoreNonLeafs ) {

			filename = path;
			br.odb.libscene.World w = new br.odb.libscene.World();
			w.internalize( path, false, MainWindow.getInstance() );
			internalize( w, ignoreNonLeafs );
			this.notifyChange();
		}
//-----------------------------------------------------------------------------	
		public void internalize( br.odb.libscene.World w, bool ignoreNonLeafs ) {

			br.odb.libscene.Sector dllSector;
			Sector sector;
			world.eraseAll();
			int added = 1;
			for ( int c = 0; c < w.getTotalSectors(); ++c)  {
				
				if ( c == 0  )
					continue;
				
				dllSector = w.getSector( c );

				if ( dllSector.isDegenerate() )
					continue;
				
				if ( !dllSector.isMaster() && ignoreNonLeafs ) 
					continue;
				
				
				
				sector = ( Sector ) addNewSector(  false );
				sector.initFrom( dllSector );
				sector.setId( added++ );
			}
			needsCommit = false;
		}
//-----------------------------------------------------------------------------	
		public void askForScreenRefresh()
		{
			parentWindow.QueueDraw();
		}
//-----------------------------------------------------------------------------			
		public void notifyChange() {
			
			if ( isLocked() )
				return;
			
			lockContext();

			if ( master != null )
				master.contextModified();			
			
			unlockContext();
		}
//-----------------------------------------------------------------------------			
		[MethodImpl(MethodImplOptions.Synchronized)]
		public void lockContext() {
			updating = true;
		}
//-----------------------------------------------------------------------------			
		[MethodImpl(MethodImplOptions.Synchronized)]
		public void unlockContext() {
			updating = false;
		}		
//-----------------------------------------------------------------------------			
		[MethodImpl(MethodImplOptions.Synchronized)]
		public bool isLocked() {
			
			return updating;
		}
//-----------------------------------------------------------------------------			
		public void saveCurrent() {
			save( filename );
		}
//-----------------------------------------------------------------------------	
		public string getFilename() {
			return filename;
		}
//-----------------------------------------------------------------------------	
		public void removeEntity( Sector s ) {
			this.world.removeSector( s );
		}
//-----------------------------------------------------------------------------	
		public void save( String path ) {
			
			if ( filename == null || filename == "" )
				filename = path;


			world.fixIds();
			world.saveToDiskAsLevel (MainWindow.getInstance().openAsOutputStream( path ));
			needsCommit = false;
		}
//-----------------------------------------------------------------------------	
	}
//-----------------------------------------------------------------------------	
}
//-----------------------------------------------------------------------------