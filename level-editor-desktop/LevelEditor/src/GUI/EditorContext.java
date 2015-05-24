/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.LevelEditorContextListener;
import br.odb.libscene.Color;
import br.odb.libscene.Sector;
import br.odb.libscene.World;
import br.odb.utils.FileServerDelegate;
import br.odb.utils.Utils;
import br.odb.utils.math.Vec3;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author monty
 */
public class EditorContext implements FileServerDelegate {
    Sector currentSector;
    String currentFilename;
    boolean needsCommit;
    World world;
    LevelEditorContextListener listener;
    private Vec3 cursor = new Vec3();
    
    public EditorContext( LevelEditorContextListener listener ) {
        this.listener = listener;
    }
    
    public void loadMap( String path ) {
        world = new World();
        world.internalize( path, false, this );
        listener.contextModified();
        needsCommit = false;
    }
    
    public void saveMap( String path ) {
        try {    
            currentFilename = path;
            world.saveToDiskAsLevel( this.openAsOutputStream( path ) );
            this.needsCommit = false;
        } catch (IOException ex) {
            Logger.getLogger(EditorContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    boolean isCommitNeeded() {
        return needsCommit;
    }

    @Override
    public InputStream openAsInputStream(String filename) throws IOException {
        return new FileInputStream( filename );
    }

    @Override
    public InputStream openAsset(String filename) throws IOException {
        return new FileInputStream( filename );
    }

    @Override
    public InputStream openAsset(int resId) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OutputStream openAsOutputStream(String filename) throws IOException {
        return new FileOutputStream( filename );
    }

    World getWorld() {
        return world;
    }

    String[] getSectorNamesList() {
        
        String[] toReturn;
        
        ArrayList<String> temporary = new ArrayList<>();
        
        for ( Sector s : world ) {
            
            if ( !s.isMaster() )
                continue;
            
            if ( temporary.isEmpty() ) {
                temporary.add( " - " );
            } else {
                temporary.add( "Sector " + s.getId() );
            }
        }
        
        toReturn = new String[ temporary.size() ];
        toReturn = temporary.toArray( toReturn );
        
        return toReturn;
    }

    Sector getCurrentSector() {
        return currentSector;
    }

    void setCurrentSectorFromId(int selectedIndex) {
        
        if ( selectedIndex >= 0 )
            currentSector = world.getSector(selectedIndex);
        else
            currentSector = null;
    }

    void setLinkForCurrentSector(int face, int sector) {
        
        if ( getCurrentSector() != null) {
            getCurrentSector().setLink(  face, sector );
            needsCommit = true;
        }
    }

    void toggleCurrentSectorDoorAtFace(int face) {

        if ( getCurrentSector().getDoor( face ) == null ) {
            getCurrentSector().setDoorAt( face );
            getCurrentSector().getDoor( face ).setOpen( true );
        } else {
            getCurrentSector().removeDoorAt( face );
        }
        needsCommit = true;
    }

    /**
     * @return the cursor
     */
    public Vec3 getCursor() {
        return cursor;
    }

    /**
     * @param cursor the cursor to set
     */
    public void setCursor(Vec3 cursor) {
        this.cursor = cursor;
    }

    void trySelectingSectorFromCursor() {
        
        
        for ( Sector s : world ) {
            
            if ( s.getId() == 0 )
                continue;
            
            if ( s.contains(cursor ) ) {
                this.currentSector = s;
                return;
            }
        }
        
        currentSector = world.getSector( 0 );
    }

    void moveCurrentSectorTo() {
        currentSector.moveTo( cursor );
        needsCommit = true;
    }

    void resizeCurrentSectorTo() {
        currentSector.resizeTo( cursor );
        needsCommit = true;
    }

    Sector addNewSector() {
        Sector s = new Sector( 0, 10, 0, 10, 0 ,10 );
        s.moveTo( cursor );
        s.setIsMaster( true );
        world.addSector( s );
        s.setId( world.getTotalMasterSectors() - 1 );
        needsCommit = true;
        this.listener.contextModified();
        return s;
    }

    void createNew() {
        world = new World();
        currentFilename = null;
        this.addNewSector();
        world.getSector( 0 ).set( 0, 0, 0, 255, 255, 255 );
        this.needsCommit = false;
    }

    void setCurrentSector(Sector s) {
       currentSector = s;
    }

    void deleteCurrentSector() {
        world.removeSector( currentSector, true );
        needsCommit = true;
        this.listener.contextModified();
    }

    Sector duplicateCurrentSector() {
        Sector s = currentSector;
        Sector newSector = this.addNewSector();
        int oldId = newSector.getId();
        newSector.initFrom( s );
        newSector.setId( oldId );
        this.listener.contextModified();
               
        return newSector;
    }

    void setColorForCurrentSectorFace(int index, int valueR, int valueG, int valueB) {
        currentSector.setColor( new Color( valueR, valueG, valueB ), index );
    }

    void importFromObj(String filePath) {
        try {
            java.util.ArrayList mesh = null;				
            br.odb.liboldfart.parser.FileFormatParser loader = null;		
            world = new br.odb.libscene.World();
            loader = new br.odb.liboldfart.wavefront_obj.WavefrontOBJLoader();
            ((br.odb.liboldfart.wavefront_obj.WavefrontOBJLoader)loader).currentPath = Utils.extractPathFrom( filePath );
            loader.preBuffer( this.openAsInputStream( filePath ) );
            loader.parseDocument( this );		
            mesh = loader.getGeometry();	
            br.odb.libscene.WorldUtils.buildConvexHulls( world, mesh );            
            listener.contextModified();
       } catch (IOException ex) {
            Logger.getLogger(EditorContext.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    String getCurrentFilename() {
        return this.currentFilename;
    }

    void commit() {
        this.saveMap( currentFilename );                
    }

    void loadMapLeafSectors(String filePath) {
        
        world = new World();
        world.internalize( filePath, false, this );
        listener.contextModified();
        needsCommit = false;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
