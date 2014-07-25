// Sector.cs created with MonoDevelop
// User: daniel at 12:36 AM 8/1/2009
//
// To change standard headers go to Edit->Preferences->Coding->Standard Headers
//

using System;

namespace MonoBED2
{
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */	
	public class Sector {		
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */		
		private class Door {
			public int iTimeout;
			public int iLinksTo;
			public GameLogicConstants.BASE_TRIGGER iOpener;
		}
//-----------------------------------------------------------------------------		
		private System.Collections.ArrayList Triggers;
		private Gdk.Color[] iColor;
		private int iID;
		private int[] iLink;
		private String iMapLink;
		private int[] iX;
		private int[] iY;
		private Door[] iDoors;
		private int[] iHeight;		
	    private int iParent;
		public bool solid;
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetDoor( int aDir ) {
			iDoors[ aDir ] = new Door();
			iDoors[ aDir ].iLinksTo = this.GetLink( aDir );			
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
	 	public bool GetDoor( int aDir ) {
			return iDoors[ aDir ] != null;				
		}		
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public string GetMapLink() {
			return iMapLink;		
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetMapLink( string aLink ) {
			iMapLink = aLink;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public Gdk.Color GetColor( int aSlot ) {
			return iColor[ aSlot ];		
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetColor( int aSlot, Gdk.Color aColor ) {
			System.Console.WriteLine( "cor:" + aColor.ToString() );
			iColor[ aSlot ] = aColor;
		}		
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int GetLink( int aLink ) {
			return iLink[ aLink ];		
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetLink( int aSlot, int aLink ) {
			iLink[ aSlot ] = aLink;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */		
		public int GetX0() {
			return iX[ 0 ];
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int GetX1() {
			return iX[ 1 ];
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int GetY0() {
			return iY[ 0 ];
		}		
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int GetY1() {
			return iY[1];
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int GetHeight0() {
			return iHeight[ 0 ];
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int GetHeight1() {
			return iHeight[ 1 ];
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetX0( int aX0 ) {
			iX[ 0 ] = aX0;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetX1( int aX1 ) {
			iX[ 1 ] = aX1;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetY0( int aY0) {
			iY[ 0 ] = aY0;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetY1( int aY1 ) {
			iY[ 1 ] = aY1;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetHeight0( int aH0 ) {
			iHeight[ 0 ] = aH0;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetHeight1( int aH1 ) {
			iHeight[ 1 ] = aH1;
		}		
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void MoveBy( int x, int y ) {
			iX[ 0 ] += x;
			iY[ 0 ] += y;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void StretchBy( int x, int y ) {
			iX[ 1 ] += x - iX[ 0 ];
			iY[ 1 ] += y - iY[ 0 ];
		}		
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void MoveXZTo(int x,int y) {
			iX[ 0 ] = x;
			iY[ 0 ] = y;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void StretchXZTo( int x, int y ) {
			iX[ 1 ] = x - iX[ 0 ];
			iY[ 1 ] = y - iY[ 0 ];
		}	
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void MoveXYTo( int x, int y ) {
			iY[ 0 ] = x;
			iHeight[ 0 ] = 128 - y;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void StretchXYTo( int x, int y ) { 
			iY[ 1 ] = x - iY[ 0 ];
			iHeight[ 1 ] = 128 - y; //-iHeight[0];
		}	
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void MoveYZTo(int x,int y) {
			iHeight[ 0 ]= 128 - y;
			iX[ 0 ] = x;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void StretchYZTo(int x,int y) {
			iHeight[ 1 ] = 128 - y;
			iX[ 1 ] = x - iX[ 0 ];
		}	
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int GetID() {
			return iID;		
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetID(int aID) {
			iID = aID;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int GetParent() {
			return iParent;		
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetParent(int aParent) {
			iParent = aParent;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public Sector() {
			solid = false;
			SetParent( SectorConstants.NO_PARENT );				
			SetMapLink( SectorConstants.NO_MAP_LINK );		   
			iX = new int[ 2 ];
			iY = new int[ 2 ];
			iHeight = new int[ 2 ];
			iHeight[ 0 ] = 0;
			iHeight[ 1 ] = 5;
			iLink = new int[ 6 ];
			iDoors = new Door[ 6 ];
			iColor = new Gdk.Color[ 6 ];

			for ( int c = 0; c < 6; ++c )		
					iLink[ c ] = SectorConstants.NO_SECTOR_LINK;				
		
			iColor[ 0 ] = new Gdk.Color( 0x00, 0x00, 0xFF );
			iColor[ 1 ] = new Gdk.Color( 0xFF, 0x00, 0x00 );
			iColor[ 2 ] = new Gdk.Color( 0x00, 0xFF, 0x00 );
			iColor[ 3 ] = new Gdk.Color( 0x00, 0xFF, 0xFF );
			iColor[ 4 ] = new Gdk.Color( 0x80, 0x80, 0x80 );
			iColor[ 5 ] = new Gdk.Color( 0x50, 0x50, 0x50 );
			
			iX[ 0 ] = 10;
			iY[ 0 ] = 10;
			iX[ 1 ] = 10;
			iY[ 1 ] = 10;
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * ( works in BZK coordinate System? )
		 * */		
		public SectorConstants.DIRECTIONS WhereBSC (int ax,int ay,int az) {
			
			int x = ax;
			int y = ay;
			int z = az;
			int h1 = GetHeight0();
			int h2 = GetHeight1();
			if ( h1 > z ) return SectorConstants.DIRECTIONS.FLOOR;
			if ( h2 < z ) return SectorConstants.DIRECTIONS.CEILING;
			if ( getY0() > y ) return SectorConstants.DIRECTIONS.N;
			if ( getX1() < x ) return SectorConstants.DIRECTIONS.E;
			if ( getX0() > x ) return SectorConstants.DIRECTIONS.W;
			if ( getY1() < y ) return SectorConstants.DIRECTIONS.S;
			
			return SectorConstants.DIRECTIONS.INSIDE;
	    } 
//-----------------------------------------------------------------------------
		public br.odb.libscene.Sector buildBZKSector() {
			br.odb.libscene.Sector sectorToMake;
			
			sectorToMake = new br.odb.libscene.Sector();		
			
			sectorToMake.set( GetX0(), GetHeight0(), GetY0(), GetX1() + GetX0(), GetHeight1(), GetY1() + GetY0() );
			
			for ( int c = 0; c < 6; ++c ) {
				sectorToMake.setColor( new br.odb.libscene.Color( ( ( ( GetColor( c ).Red * 128 ) / Int16.MaxValue ) - 1 ), ( ( ( GetColor( c ).Green * 128 ) / Int16.MaxValue ) - 1 ), ( ( ( GetColor( c ).Blue * 128 ) / Int16.MaxValue ) - 1 ) ), c );
			}
			
			sectorToMake.setSolid( solid );
			
			return sectorToMake;
		}
		
		public void buildFromBZKSector( br.odb.libscene.Sector sector ) {
			
			SetX0( ( int ) sector.getX0() );
			SetX1( ( int ) ( sector.getX1() - sector.getX0() ) );
			
			SetY0( ( int ) sector.getZ0() );
			SetY1( ( int ) ( sector.getZ1() - sector.getZ0() ) );
			
			SetHeight0( ( int ) sector.getY0() );
			SetHeight1( ( int ) sector.getY1() );			
			
			solid = sector.isSolid();
			
			Gdk.Color color;
			for ( int c = 0; c < 6; ++c ) {
				this.SetLink( c, sector.getLink( c ) );
				color = new Gdk.Color();
				
				color.Red = ( ushort ) sector.getColor( c ).getR();
				color.Green = ( ushort ) sector.getColor( c ).getG();
				color.Blue = ( ushort ) sector.getColor( c ).getB();
				SetColor( c, color );
			}
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int getDX() {
			return iX[ 1 ] - iX[ 0 ];
		}	
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int getDY() {
			return iY[ 1 ] - iY[ 0 ];
		}		
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int getDZ() {
			return iHeight[ 1 ] - iHeight[ 0 ];
		}	
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int getY0() {
			return iY[ 0 ];
		}		
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int getX0() {
			return iX[ 0 ];
		}	
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int getY1() {
			return getY0() + getDY();
		}		
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int getX1() {
			return getX0() + getDX();
		}			
	}
}
