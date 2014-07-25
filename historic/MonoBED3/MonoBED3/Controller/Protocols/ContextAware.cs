using System;
namespace MonoBED3
{
	public interface ContextAware
	{
		void setContext( EditorContext context);
		EditorContext getContext();		
	}
}

