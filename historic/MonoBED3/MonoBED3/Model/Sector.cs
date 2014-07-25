//using System;
//using br.odb.utils.math;
////-----------------------------------------------------------------------------
//namespace MonoBED3
//{
////-----------------------------------------------------------------------------	
//	public class Sector: GameObject
//	{
//		br.odb.libscene.Sector sector;
//				
//		public br.odb.libscene.Sector makeBZK3Sector() {
//			
//			br.odb.libscene.Sector toReturn = new br.odb.libscene.Sector( sector );
//			toReturn.setIsMaster( true );
//			return toReturn;
//		}
//
//		public br.odb.libscene.Sector getBZK3Sector() {
//			return sector;
//		}
//
//		public void setMapLink( String newMapLink ) {
//
//			sector.setMapLink (newMapLink);
//		}
//
//		public String getMapLink() {
//
//			return sector.getMapLink();
//		}
//		
//		public Sector duplicate ()
//		{
//			Sector toReturn = new Sector( this );
//
//			return toReturn;
//		}
////-----------------------------------------------------------------------------		
//		public Sector () {			
//			sector = new br.odb.libscene.Sector();
//			setDX( 10.0f );
//			setDY( 10.0f );
//			setDZ( 10.0f );
//		}
//
//
//		public Sector( Sector other ) {
//
//			sector = new br.odb.libscene.Sector( other.sector );
//			setIsParent( other.isParent() );
//			setX0( other.getX0() );
//			setY0( other.getY0() );
//			setZ0( other.getZ0() );
//			setDX( other.getDX() );
//			setDY( other.getDY() );
//			setDZ( other.getDZ() );
//			setInternalLight( other.getInternalLight() );
//		}
//		
//		
//		public override bool contains (float x, float y, float z) {
//			br.odb.utils.math.Vec3 vec = new br.odb.utils.math.Vec3( x, y, z );
//			return sector.touches( vec );
//		}
//
//		
//		public override void setX (float X)
//		{
//			setX0( X );
//		}
//		
//		public override void setY (float Y)
//		{
//			setY0( Y );
//		}
//		
//		public override void setZ( float Z ) {
//			setZ0( Z );
//		}
//
//		public override float getX() {
//			return getX0();
//		}
//		
//		public override float getY() {
//			return getY0();
//		}
//		
//		public override float getZ() {
//			return getZ0();
//		}
//
//
////-----------------------------------------------------------------------------		
//		public void setLink( int slot, int link ) {
//			
//			sector.setLink( slot, link );
//			
//			if ( sector.getDoor( slot ) != null ) {
//				sector.setDoorAt( slot, link );	
//			}
//		}
////-----------------------------------------------------------------------------				
//		public int getLink( int slot ) {
//			return sector.getLink( slot );
//		}
////-----------------------------------------------------------------------------		
//		public void setColor( int c, Color color ) {
//			sector.setColor( color.getODBColor(), c );
//		}
////-----------------------------------------------------------------------------		
//		public Color getColor( int c ) {
//			return new Color( sector.getColor( c ) );
//		}
//
//		public Gdk.Color getGtkColor( int slot ) {
//
//			Color c = getColor( slot );
//			int light = getInternalLight();
//			return new Gdk.Color( ( byte )( c.getR() + light ), ( byte )( c.getG() + light ), ( byte )( c.getB() + light ) );
//		}
//
////-----------------------------------------------------------------------------		
//		public override void moveTo( float x, float y, float z ) {
//			sector.moveTo( new Vec3( x, y, z ) );
//		}
//		
//		
//		public void setX0( float v ) {
//			sector.setX0( v );
//		}
//		
//		public void setY0( float v ) {
//			sector.setY0( v );
//		}
//		
//		public void setZ0( float v ) {
//			sector.setZ0( v );
//		}
//
//		
//		public void setX1( float v ) {
//			sector.setX1( v );
//		}
//		
//		public void setY1( float v ) {
//			sector.setY1( v );
//		}
//		
//		public void setZ1( float v ) {
//			sector.setZ1( v );
//		}		
//		
//		public float getX0() {
//			return sector.getX0();
//		}
//		
//		public float getY0() {
//			return sector.getY0();
//		}
//
//		public float getZ0() {
//			return sector.getZ0();
//		}
//
//		public float getX1() {
//			return sector.getX1();
//		}
//		
//		public float getY1() {
//			return sector.getY1();
//		}
//
//		public float getZ1() {
//			return sector.getZ1();
//		}
//		
//		public void setDY( float Y)
//		{
//			sector.setDY( Y );
//		}
//		
//		public void setDZ( float Z)
//		{
//			sector.setDZ( Z );
//		}
//		
//		
//		public void setDX( float X)
//		{
//			sector.setDX( X );
//		}
//		
//		public float getDY()
//		{
//			return sector.getDY();
//		}
//		
//		public float getDZ()
//		{
//			return sector.getDZ();
//		}
//		
//		
//		public float getDX()
//		{
//			return sector.getDX();
//		}
//		
//		public override string ToString ()
//		{
//			String toReturn = sector.ToString();	
//
//			return toReturn;
//		}
//		
//		public void setIsDoor( int slot, bool state, int to ) {
//			if ( state )
//				this.sector.setDoorAt( slot, to );
//			else
//				this.sector.removeDoorAt( slot );
//		}
//		
//		public void setParent( int parent ) {
//			sector.setParent( parent );
//			sector.setIsMaster( false );
//		}
//		
//		public bool isParent() {
//			return sector.isMaster();
//		}
//		
//		public String getDecal( int face ) {
//			
//			return sector.getDecalAt (face);
//		}
//		
//		public void setDecal( int face, String decalUri ) {
//			
//			sector.setDecalAt( face, decalUri );
//		}
//		
//		public void setIsParent( bool isParent ) {
//			sector.setIsMaster( isParent );
//		}
//		
//		public bool IsDoorAt( int slot ) {
//			return this.sector.getDoor( slot ) != null;
//		}
//		
//		public void initFrom( br.odb.libscene.Sector s ) {
//			this.sector.initFrom( s );
//			setId ( s.getId() );
//		}
//	}
////-----------------------------------------------------------------------------	
//}
////-----------------------------------------------------------------------------
