// World.cs created with MonoDevelop
// User: daniel at 12:35 AM 8/1/2009
//
// To change standard headers go to Edit->Preferences->Coding->Standard Headers
//

using System;
using System.Collections;

namespace MonoBED2 {
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public class World {
		public System.Collections.ArrayList Sectors;		
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
		public World() {
			Sectors=new System.Collections.ArrayList();			
		}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
		public Sector GetSector(int aID) {
			return (Sector)Sectors[aID];
		}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
		public bool isParent( int aSector ) {	
			
			for ( int c = 0; c < Sectors.Count; c++ ) {	
						
					if ( GetSector( c ).GetParent() == aSector )
						return true;
				}
			
			return false;
		}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
		public void Add( Sector aSector ) {
			aSector.SetID( Sectors.Count );
			Sectors.Add( aSector );			
		}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */	
		public int Count() {
			return Sectors.Count;
		}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
		public void Clear() {
			Sectors.Clear();			
		}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
		public void LoadMap(string aFilename) {
			
			Sector sector;     
			String token;
			byte index,aux,r,g,b,a;
			
		    System.IO.TextReader tr = new System.IO.StreamReader( aFilename );
			
			Clear();     
			
			token=tr.ReadLine();     	
	
		    while ( token != "</BZK2>" ) {   
				
		     		token = tr.ReadLine();     		
		     		
		     		if ( token == "<Sector>" ) {
					
		     			sector = new Sector();
		     			
		     			while ( token != "</Sector>" ) {
						
		     				if ( token == "<parent>" ) {
							
		     					token = tr.ReadLine();
		     					sector.SetParent( int.Parse( token ) );
							}
		
		     				if ( token == "<Volume>" ) {
			     			
								tr.ReadLine();	     				
		     					token = tr.ReadLine();
		     					sector.SetX0( int.Parse( token ) );
		     					token = tr.ReadLine();
		     					sector.SetY0( int.Parse( token ) );
		     					token = tr.ReadLine();
		     					sector.SetHeight0( int.Parse( token ) );
		     					token = tr.ReadLine();
		     					token = tr.ReadLine();
		     					sector.SetX1( int.Parse( token ) );
		     					token = tr.ReadLine();
		     					sector.SetY1( int.Parse( token ) );
		     					token = tr.ReadLine();
		     					sector.SetHeight1( sector.GetHeight0() + int.Parse( token ) );
		     					token = tr.ReadLine();
							
		     				}    
														
		     				if ( token == "<Connections>" )
		     					for ( index = 0; index < 6; ++index ) {
		     						token = tr.ReadLine();     						
		     						sector.SetLink( index, int.Parse( token ) );
		     						}  
								
							if ( token == "<Color>" ) {
		     					for ( index = 0; index < 6; ++index ) {
								
		     						bool alpha = false;
		     						token = tr.ReadLine();
		     						alpha = ( token == "<RGBA>" );
		     						token = tr.ReadLine();     						
		     						r = ( byte ) Convert.ToUInt32( token );
		     						token=tr.ReadLine();
		     						g = ( byte ) Convert.ToUInt32( token );
		     						token=tr.ReadLine();
		     						b = ( byte ) Convert.ToUInt32( token );
		     						token = tr.ReadLine();
		     						
									if ( alpha ) {
										a=(byte)Convert.ToUInt32(token);
		     							token=tr.ReadLine();
		     						}
								
		     						sector.SetColor( index, new Gdk.Color( r, g, b ) );
		     					}     						
		     				}
								
							if ( token == "<facecolor>" ) {
							
								token = tr.ReadLine();     						
		     					index = ( byte ) Convert.ToUInt32( token );
							
		     					{
		     						bool alpha = false;
		     						token = tr.ReadLine();
		     						alpha = ( token=="<RGBA>" );
		     						token = tr.ReadLine();     						
		     						r = ( byte ) Convert.ToUInt32( token );
		     						token = tr.ReadLine();
		     						g = ( byte ) Convert.ToUInt32( token );
		     						token = tr.ReadLine();
		     						b = ( byte ) Convert.ToUInt32( token );
		     						token = tr.ReadLine();
								
		     						if ( alpha ) {
		     							a = ( byte ) Convert.ToUInt32( token );
		     							token = tr.ReadLine();
		     						}
								
		     						sector.SetColor( index, new Gdk.Color( r, g, b ) );
									sector.SetLink( index, 0 );
		     						}     						
		     				}
		
		     				token=tr.ReadLine();		
		     			}						
		     			Add(sector);
		     		}
		     	}
		}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
		public void SaveMap(string aFilename, ActorManager Actors) {
			
			int c;
			int d;
			int f;
			
	 		System.IO.TextWriter tw=new System.IO.StreamWriter(aFilename);	 
			
		 	tw.WriteLine( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" );
		 	tw.WriteLine( "<?xml-stylesheet type=\"text/xsl\" href=\"bzkmap.xsl\" ?>" );
		 	tw.WriteLine( "<BZK2>" );
		 	tw.WriteLine( "<Version>" );
		 	tw.WriteLine( "0.5" );
		 	tw.WriteLine( "</Version>" );		 	
		 	tw.WriteLine( "<Number.of.Sectors>" );
		 	tw.WriteLine( Count() -1 );
			tw.WriteLine( "</Number.of.Sectors>" );
				/*
			tw.WriteLine("<Sector>\n<Volume>\n<Vec3f>\n0\n0\n0\n</Vec3f>\n255\n255\n255\n</Volume>\n<Name>\nlocation\n</Name>");
			tw.WriteLine("<Connections>\n0\n0\n0\n0\n0\n0\n</Connections>\n<Color>\n<RGBA>\n255\n0\n0\n0\n</RGBA>\n<RGBA>\n255");
			tw.WriteLine("255\n255\n0\n</RGBA>\n<RGBA>\n0\n255\n0\n0\n</RGBA>\n<RGBA>\n0\n0\n255\n0\n</RGBA>\n<RGBA>\n64");
			tw.WriteLine("64\n64\n0\n</RGBA>\n<RGBA>\n128\n128\n128\n0\n</RGBA>\n</Color>\n</Sector>");
		 	*/	        
			
	        for ( c = 0; c < Count(); ++c ) {
	        	tw.Write( "<!--setor " );
	        	tw.Write( c );
	        	tw.WriteLine( "-->" );
	        	tw.WriteLine( "<Sector>" );
	        	tw.WriteLine( "<Name>" );
	        	tw.Write( "setor " );
	        	tw.WriteLine( c );
	        	tw.WriteLine( "</Name>" );	        		        		
	        	tw.WriteLine( "<Volume>" );
				tw.WriteLine( "<Vec3f>" );
				f = GetSector( c ).GetX0();
				tw.WriteLine( f.ToString() );
				tw.WriteLine( GetSector( c ).GetY0() );
				tw.WriteLine( GetSector( c ).GetHeight0() );
				tw.WriteLine( "</Vec3f>" );
				tw.WriteLine( GetSector( c ).GetX1() );
				tw.WriteLine( GetSector( c ).GetY1() );
				tw.WriteLine( GetSector( c ).GetHeight1() );
				tw.WriteLine( "</Volume>" );
				
				if ( GetSector( c ).GetMapLink() != "" ) {
					tw.WriteLine( "<Link>" );
					tw.WriteLine( GetSector( c ).GetMapLink() );
					tw.WriteLine( "</Link>" );
				}
	        		
	        	tw.WriteLine("<Connections>");
	        	
				for ( d = 0; d < 6; ++d )		
						tw.WriteLine( GetSector( c ).GetLink( d ) );
	        	
				tw.WriteLine("</Connections>");
	        		
        		tw.WriteLine("<Color>");
        		Gdk.Color cor;

				for ( d = 0; d < 6; ++d ) {
					
	        			tw.WriteLine( "<RGBA>" );
	        			cor=GetSector( c ).GetColor( d );
	        			tw.WriteLine( ( byte ) cor.Red );
	        			tw.WriteLine( ( byte ) cor.Green );
	        			tw.WriteLine( ( byte ) cor.Blue );
	        			tw.WriteLine( 255 );
	        			tw.WriteLine( "</RGBA>" );
	        	}
	        	
				tw.WriteLine( "</Color>" );	        	
				tw.WriteLine( "</Sector>" );
	        }	        
				
			if ( Actors.Count() > 0 ) {
				
				tw.WriteLine( "<Actors>" );
				
				for ( int g = 0; g < Actors.Count(); ++g )
					tw.WriteLine( Actors.Serialize( g ) );
				
				tw.WriteLine( "</Actors>\n<Camera>\n0\n</Camera>" );
			} else
				tw.WriteLine( "<Actors>\n<Actor>\n<Name>\nplayer\n</Name>\n<Location>\n1\n</Location>\n<Class>\ntest\n</Class>\n</Actor>\n</Actors>\n<Camera>\n0\n</Camera>" );
				
			tw.WriteLine( "</BZK2>" );			
			tw.Close();	
		}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
		public void ExportToMobile( string aFilename, ActorManager actors ) {
			
			System.IO.TextWriter tw = new System.IO.StreamWriter( aFilename );
			
			for ( int c = 0; c < Count(); ++c ) {
				
			//if (this.isParent(c)) continue;
			if (
				GetSector( c ).GetLink( 0 ) == 0 &&	    
				GetSector( c ).GetLink( 1 ) == 0 &&	    
				GetSector( c ).GetLink( 2 ) == 0 &&	    
				GetSector( c ).GetLink( 3 ) == 0 &&	    
				GetSector( c ).GetLink( 4 ) == 0 &&	    
				GetSector( c ).GetLink( 5 ) == 0
				)
				continue;
					
					
			tw.Write("s ");
			tw.Write(GetSector(c).GetX0());
			tw.Write(" ");			
			tw.Write(GetSector(c).GetX1());
			tw.Write(" ");			
			tw.Write(GetSector(c).GetHeight0());
			tw.Write(" ");
			tw.Write(GetSector(c).GetHeight1());
			tw.Write(" ");			
			tw.Write(GetSector(c).GetY0());
			tw.Write(" ");			
			tw.Write(GetSector(c).GetY1());
			tw.Write("\n");
			tw.Write("l");
				
			for ( int d = 0; d < 6; ++d ) {
				tw.Write( " " );
				tw.Write( GetSector( c ).GetLink( d ) );
			}
					
			tw.Write( "\n" );					
					
			for (int d=0;d<6;d++)
				if ( GetSector( c ).GetLink( d ) == 0 )	{
					
					tw.Write( "c " );
					tw.Write( d );
					tw.Write( " " );				
					tw.Write( 255 * GetSector( c ).GetColor( d ).Red / 0xFFFF );
					tw.Write( " " );
					tw.Write( 255 * GetSector( c ).GetColor( d ).Green / 0xFFFF );
					tw.Write( " " );
					tw.Write( 255 * GetSector( c ).GetColor( d ).Blue / 0xFFFF );
					tw.Write( "\n" );									
				}
				
				for ( int e = 0; e < actors.Actors.Count; ++e ) {
					tw.Write( "a" );
					tw.Write( " " );				
					tw.Write( actors.GetActor( e ).GetLocation() );
					tw.Write( " " );				
					tw.Write( actors.GetActor( e ).GetLight() );
					tw.Write( "\n" );					
				}
			}
			tw.Flush();
			tw.Close();	
		}
//-----------------------------------------------------------------------------
	public void importLEVEL( string aFilename, ActorManager actors ) {
			br.odb.libscene.Sector BZKSector;
			Sector sector;
			br.odb.libscene.Actor BZKActor;
			Actor actor;
			java.util.ArrayList bzkActors = new java.util.ArrayList();
			
			java.util.ArrayList mesh = null;		
			br.odb.liboldfart.parser.level.GEOLoader loader = new br.odb.liboldfart.parser.level.GEOLoader();
			loader.preBuffer( "/home/monty/prison.level" );
			
			loader.parseDocument();
			mesh = loader.getGeometry();
			//GeometryUtils.Mesh.dumpToViewSVG( mesh, "/home/monty/svg/geometry.svg");		
			
			
			br.odb.libscene.World world = new br.odb.libscene.World();
			br.odb.portalizer.BZKWorldPartitioner.buildConvexHulls( world, mesh );
			loader.assignColors( world );					
			                                      
			for ( int c = 0; c < world.getTotalSectors(); ++c ) {
				br.odb.libscene.Sector BZKsector = world.getSector( c );
				sector = new Sector();
				sector.buildFromBZKSector( BZKsector );
				Add( sector );
			}			
			
	}

//-----------------------------------------------------------------------------
	public void exportMap( string aFilename, ActorManager actors ) {
			
			br.odb.libscene.Sector sector;
			br.odb.libscene.Actor bzkActor;
			java.util.ArrayList bzkActors = new java.util.ArrayList();
			br.odb.utils.math.Mesh.snapBoundsToVertexes = true;
			Actor actor;
			br.odb.libscene.World world = new br.odb.libscene.World();
			
			for ( int c = 1; c < Count(); ++c ) {					

				sector = GetSector( c ).buildBZKSector();
				world.addSector( sector );
			}
			
			for ( int c = 0; c < actors.Count(); ++c ) {
				actor = actors.GetActor( c );
				bzkActor = new br.odb.libscene.Actor( /*actor.GetKind(), actor.GetLight(), null, actor.GetLocation()*/ );
				bzkActors.add( bzkActor );
			}
			 
			br.odb.portalizer.BZKWorldPartitioner.dumpTopViewToSVG( world, "/home/monty/svg/before.svg");
			world = br.odb.portalizer.BZKWorldPartitioner.optimize( world );
			br.odb.portalizer.BZKWorldPartitioner.guessLinks( world );
			br.odb.portalizer.BZKWorldPartitioner.dumpTopViewToSVG( world, "/home/monty/svg/after.svg");
			br.odb.portalizer.BZKWorldPartitioner.saveToDiskAsLevel( world, "/home/monty/prison.level", bzkActors );			
		}
	}
}
