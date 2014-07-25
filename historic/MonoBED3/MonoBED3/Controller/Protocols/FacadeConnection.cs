using System;
namespace MonoBED3
{
	public interface FacadeConnection
	{
		void connect(Object obj);
		void disconnect();
		Object getObject();
		Object getOwnerCopy();
	}
}

