// Actor.cs created with MonoDevelop
// User: daniel at 12:36 AM 8/1/2009
//
// To change standard headers go to Edit->Preferences->Coding->Standard Headers
//

using System;

namespace MonoBED2 {	
	//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public class Actor {
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */	
		int iLocation;
		/*
		 * 
		 * */		
		int iLight;
		/*
		 * 
		 * */		
		int iKind;
		/*
		 * 
		 * */		
		float iTurnStep;
		/*
		 * 
		 * */		
		String iApearance;
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */	
		public Actor() {
			iLocation = 1;
			iKind = 0;
			iLight = 0;
			iTurnStep = 22.5f;
			iApearance = "";
		}
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */		
		public int GetLocation() {
			return iLocation;			
		}
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */		
		public int GetLight() {
			return iLight;			
		}
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */	
		public int GetKind() {
			return iKind;			
		}
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */		
		public float GetTurnStep() {
			return iTurnStep;			
		}
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public String GetApearance() {
			return iApearance;			
		}		
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetLocation( int location ) {
			iLocation = location;			
		}
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */		
		public void SetLight( int light ) {
			 iLight = light;			
		}
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */	
		public void SetKind( int kind ) {
			iKind = kind;			
		}
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */		
		public void SetTurnStep( float turnstep ) {
			iTurnStep = turnstep;			
		}
		//-----------------------------------------------------------------------------
		/*
		 * 
		 * */
		public void SetApearance( String apearance ) {
			iApearance=apearance;			
		}
		//-----------------------------------------------------------------------------	
	}
}
