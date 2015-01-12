package br.odb.gamelib.android.geometry;
 
import br.odb.libstrip.IndexedSetFace;
import br.odb.libstrip.Mesh;

public class GLESMesh extends Mesh {
	
	public GLESMesh( String name, Mesh other) {
		super( name, other );
		
		for ( IndexedSetFace face : this.faces ) {
			addFace(face.makeCopy());
		}
	}


	public GLESMesh(String name) {
		super( name );
	}

//	public void preBuffer() {
//		
//		if ( manager != null )
//			return;
//		
//		manager = new GLESVertexArrayManager();
//		manager.init( faces.size() );
//		
//		for ( IndexedSetFace trig : faces ) {
//			
//			( ( GeneralTriangle ) trig ).flush();
//			
//			if ( trig instanceof GLES1Triangle )
//				manager.pushIntoFrameAsStatic( ( ( GLES1Triangle ) trig ).verticesBits, ( ( GLES1Triangle ) trig ).colorBits );
//			else if ( trig instanceof GLES1Square )
//					manager.pushIntoFrameAsStatic( ( ( GLES1Square ) trig ).verticesBits, ( ( GLES1Square ) trig ).colorBits );
//			else
//				manager.pushIntoFrameAsStatic( ( ( GeneralTriangle ) trig ).getVertexData(), ( ( GeneralTriangle ) trig ).singleColorData() );
//		}
//	}
}
