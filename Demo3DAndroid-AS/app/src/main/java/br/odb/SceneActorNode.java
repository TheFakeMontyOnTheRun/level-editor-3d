package br.odb;

import br.odb.libscene.SceneNode;

/**
 * Created by monty on 6/28/15.
 */
public class SceneActorNode extends SceneNode {
    public float angleXZ;

    public SceneActorNode(String id) {
        super(id);
    }

    public SceneActorNode(SceneNode other) {
        super(other);
    }
}
