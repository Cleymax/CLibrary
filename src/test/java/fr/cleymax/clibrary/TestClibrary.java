package fr.cleymax.clibrary;

import org.junit.Test;

/**
 * File <b>TestClibrary</b> located on fr.cleymax.clibrary is a part of CLibrary. Created the 12/04/2020
 * <p>
 * @author Clément P. (Cleymax)
 * @since 1.0.3
 */

public class TestClibrary {

	@Test
	public void testSingleDependency()
	{
		final CLibrary library = new CLibrary(TestSingleDependency.class);
		library.loads();
	}

	@Test
	public void testMultipleDependencies()
	{
		final CLibrary library = new CLibrary(TestMultipleDependencies.class);
		library.loads();
	}

	@Test
	public void testWithLoadSingleDependency()
	{
		final CLibrary library = new CLibrary();
		library.load(TestSingleDependency.class);
	}

	@Test
	public void testWithLoadMultipleDependencies()
	{
		final CLibrary library = new CLibrary();
		library.load(TestMultipleDependencies.class);
	}

	@Test
	public void testWithLoadObjectSingleDependency()
	{
		final CLibrary             library          = new CLibrary();
		final TestSingleDependency singleDependency = new TestSingleDependency();
		library.load(singleDependency);
	}

	@Test
	public void testWithLoadObjectMultipleDependencies()
	{
		final CLibrary                 library              = new CLibrary();
		final TestMultipleDependencies multipleDependencies = new TestMultipleDependencies();
		library.load(multipleDependencies);
	}

	@Dependency(groupId = "org.jsoup", artifactId = "jsoup", version = "1.13.1")
	public class TestSingleDependency {}

	@Dependencies({
			@Dependency(groupId = "com.konghq", artifactId = "unirest-java", version = "3.7.02"),
			@Dependency(groupId = "com.googlecode.json-simple", artifactId = "json-simple", version = "1.1.1")
	})
	public class TestMultipleDependencies {}
}
