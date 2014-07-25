using System;
using NUnit.Framework;
using br.odb.libscene;

namespace MonoBED3
{
	[TestFixture()]
	public class EditorContextTests
	{
		[Test()]
		public void TestSetSector() {

			Sector sector = new Sector();

			Assert.AreEqual( 0.0f, sector.getX0() );
			Assert.AreEqual( 0.0f, sector.getY0() );
			Assert.AreEqual( 0.0f, sector.getZ0() );
			Assert.AreEqual( 1.0f, sector.getX1() );
			Assert.AreEqual( 1.0f, sector.getY1() );
			Assert.AreEqual( 1.0f, sector.getZ1() );
			Assert.AreEqual( 1.0f, sector.getDX() );
			Assert.AreEqual( 1.0f, sector.getDY() );
			Assert.AreEqual( 1.0f, sector.getDZ() );


			sector.setX0( 1.0f );
			sector.setY0( 1.0f );
			sector.setZ0( 1.0f );
			sector.setX1( 10.0f );
			sector.setY1( 10.0f );
			sector.setZ1( 10.0f );

			Assert.AreEqual( 1.0f, sector.getX0() );
			Assert.AreEqual( 1.0f, sector.getY0() );
			Assert.AreEqual( 1.0f, sector.getZ0() );
			Assert.AreEqual( 10.0f, sector.getX1() );
			Assert.AreEqual( 10.0f, sector.getY1() );
			Assert.AreEqual( 10.0f, sector.getZ1() );
			Assert.AreEqual( 9.0f, sector.getDX() );
			Assert.AreEqual( 9.0f, sector.getDY() );
			Assert.AreEqual( 9.0f, sector.getDZ() );

			sector.setDX( 10.0f );
			sector.setDY( 10.0f );
			sector.setDZ( 10.0f );

			Assert.AreEqual( 11.0f, sector.getX1() );
			Assert.AreEqual( 11.0f, sector.getY1() );
			Assert.AreEqual( 11.0f, sector.getZ1() );
			Assert.AreEqual( 10.0f, sector.getDX() );
			Assert.AreEqual( 10.0f, sector.getDY() );
			Assert.AreEqual( 10.0f, sector.getDZ() );

		}
	}
}

