using System;

namespace MonoBED3
{
	public interface Drawable
	{
		void setDrawGrid( bool shouldDrawGrid );
		void setDrawExtendingLines( bool shouldDrawLines );
		void setDrawAsSolids( bool shouldDrawAsSolids );
	}
}

