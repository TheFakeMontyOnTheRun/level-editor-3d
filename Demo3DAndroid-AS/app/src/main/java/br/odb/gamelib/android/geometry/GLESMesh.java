package br.odb.gamelib.android.geometry;
 
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.GeneralTriangleMesh;

public class GLESMesh extends GeneralTriangleMesh {
	
	public GLESMesh( String name, GeneralTriangleMesh other) {
		super( name, other );
		
		for ( GeneralTriangle face : this.faces ) {
			faces.add(face.makeCopy());
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
