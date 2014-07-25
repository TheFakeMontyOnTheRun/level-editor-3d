using System.Collections;
using System.Xml;
using System;
using br.odb.libscene;

namespace MonoBED3
{


	public class BZK2Helper
	{
		ArrayList sectors;
		
		public int getTotalSectors() {
			return sectors.Count;
		}
		
		public Sector getSector( int c ) {
			return ( Sector )sectors[ c ];
		}
		
		public ArrayList getXMLNodeListFromNode( XmlNode node, String name ) {
			
			ArrayList list = new ArrayList();
			
			for ( int c = 0; c < node.ChildNodes.Count; ++c ) {
				if ( node.ChildNodes[ c ].Name == name ) {
					list.Add( node.ChildNodes[ c ] );
				}
			}
					         
			return list;
		}
		
		int convert( int link ) {
			
			if ( link == 1 )
				return 3;
			
			if ( link == 3 )
				return 1;

			return link;
		}
		
		public void load( String path ) {
			
			sectors = new ArrayList();
			XmlDocument xDoc = new XmlDocument();
			xDoc.Load( path );
			XmlNodeList sectorNodes = xDoc.GetElementsByTagName( "Sector" );
			Sector s;
			Color c;
			float f;
			String[] vec;
			String[] rgba;
			String vec3Node;
			String colorNode;
			String node;
			
			int id = 0;
			
			foreach ( XmlNode sector in sectorNodes ) {

				s = new Sector();
				
				if ( id++ == 0 )
					continue;


				ArrayList parent = getXMLNodeListFromNode( sector, "parent" );
				
				if ( parent.Count > 0 ) {
					s.setParent( Int32.Parse( ( ( XmlNode ) parent[ 0 ] ).InnerText ) );					
				} else {
					s.setParent( 0 );
				}
				
				if ( s.getParent() == 0 ) {
					s.setIsMaster( true );
					sectors.Add( s );
					
					for ( int d = 0; d < 6; ++d ) {
						s.setColor ( new Color( d * ( 255 ) ,0 ,0, 255 ), d );
						s.setLink (0, d );
					}
				} else {
					continue;
				}
				
				ArrayList volume = getXMLNodeListFromNode( sector, "Volume" );
				
				if ( volume[ 0 ] != null ) {
					
					ArrayList vec3 = getXMLNodeListFromNode( ( ( XmlNode ) volume[ 0 ] ), "Vec3f" );
					
					node = ( ( XmlNode ) volume[ 0 ] ).InnerText.Trim();
					vec3Node = ( ( XmlNode ) vec3[ 0 ] ).InnerText.Trim();
					
					vec = vec3Node.Split('\n');
					f = float.Parse( vec[ 0 ] );
					s.setX0( f  );
					f = float.Parse( vec[ 1 ] );
					s.setZ0( f );
					f = float.Parse( vec[ 2 ] );
					s.setY0( f );
					
					try {
						vec = node.Split('\n');
						f = float.Parse( vec[ vec.Length - 3 ] );						
						s.setDX( f );
						f = float.Parse( vec[ vec.Length - 2 ] );
						s.setDZ( f );
						f = float.Parse( vec[ vec.Length - 1 ] );
						s.setDY( f );
					} catch ( Exception e ) {
						System.Console.WriteLine( "Exception " + e );
						throw new InvalidBZK2FileException();
					}
					                        
				}

				
				ArrayList facecolors = getXMLNodeListFromNode( sector, "facecolor" );
				
				foreach ( XmlNode facecolor in facecolors ) {
					c = new Color();

					ArrayList colors = getXMLNodeListFromNode( ( ( XmlNode ) facecolor ), "RGBA" );				
					colorNode = ( ( XmlNode ) colors[ 0 ] ).InnerText.Trim();					
					rgba = colorNode.Split('\n');
					c.setR( Int32.Parse( rgba[ 0 ] ) );
					c.setG( Int32.Parse( rgba[ 1 ] ) );
					c.setB( Int32.Parse( rgba[ 2 ] ) );
					c.setA( Int32.Parse( rgba[ 3 ] ) );
					s.setColor( c, convert( Int32.Parse( facecolor.InnerText.Trim().Split('\n')[ 0 ] ) ) );

					if ( s.getParent() > 0 && s.getParent() < sectors.Count )
						( ( Sector ) sectors[ s.getParent() ] ).setColor( c, convert( Int32.Parse( facecolor.InnerText.Trim().Split('\n')[ 0 ] ) ) );
				}
			}
		} //function	
	} //class
} //package
