// ActorManager.cs created with MonoDevelop
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
	public class ActorManager
	{
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */	
	    public System.Collections.ArrayList Actors;			
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */	
		public ActorManager() {
			Actors = new System.Collections.ArrayList();
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public int Count() {
			return Actors.Count;			
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public Actor GetActor( int c ) {
			return ( Actor ) Actors[ c ];
			
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void AddActor( Actor a ) {
			Actors.Add( a );			
		}
//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public String Serialize(int c) {
//			tw.WriteLine("<Actors>\n<Actor>\n<Name>\nplayer\n</Name>\n<Location>\n1\n</Location>\n<Class>\ntest\n</Class>\n</Actor>\n</Actors>\n<Camera>\n0\n</Camera>");
			String ToReturn = "";
			Actor actor = GetActor( c );
			ToReturn += "<Actor>\n";
			ToReturn += "<Location>\n";
			ToReturn += actor.GetLocation();
			ToReturn += "\n</Location>\n";
			ToReturn += "<Kind>\n";
			ToReturn += actor.GetKind();
			ToReturn += "\n</Kind>\n";
			ToReturn += "<TurnStep>\n";
			ToReturn += actor.GetTurnStep();
			ToReturn += "\n</TurnStep>\n";
		//	ToReturn += "<Apearance>\n";
	//		ToReturn += actor.GetApearance();
	//		ToReturn += "\n</Apearance>\n";
			ToReturn += "<light>\n";
			ToReturn += actor.GetLight();
			ToReturn += "\n</light>\n";
			ToReturn += "</Actor>\n";
			return ToReturn;
		}
//-----------------------------------------------------------------------------		
	}
}
