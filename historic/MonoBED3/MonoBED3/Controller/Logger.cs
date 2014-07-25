using System;

namespace MonoBED3
{
	public class Logger
	{
		public Logger ()
		{
		}
		
		public static void log( String str ) {
			Console.WriteLine( "log: " + str );
		}
	}
}