package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.libscene.SceneNode;

/**
 * Created by monty on 12/03/16.
 */
public class NodeIdParameterDefinition extends CommandParameterDefinition<SceneNode> {
	public NodeIdParameterDefinition() {
		super("node-id", "A existing node Id");
	}

	@Override
	public SceneNode obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}
}
