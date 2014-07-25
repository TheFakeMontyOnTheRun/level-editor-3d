// Main.cs created with MonoDevelop
// User: daniel at 9:03 PM 7/31/2009
//
// To change standard headers go to Edit->Preferences->Coding->Standard Headers
//
using System;
using Gtk;

namespace MonoBED2 {
	/*
	 * 
	 * */
	class MainClass {
		/*
		 * 
		 * */
		public static void Main (string[] args) {
			Application.Init ();
			MainWindow win = new MainWindow ();
			win.Show ();
			Application.Run ();
		}
	}
}