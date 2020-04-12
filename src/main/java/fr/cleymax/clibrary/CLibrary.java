package fr.cleymax.clibrary;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File <b>CLibrary</b> located on fr.cleymax.clibrary is a part of CLibrary. Created the 10/04/2020
 * <p>
 * @author Clément P. (Cleymax)
 * <p>
 * Manager for load dependency.
 */

public final class CLibrary {

	private static final Logger LOGGER            = Logger.getLogger("CLibrary");
	private static final String MAVEN_CENTRAL_URL = "https://repo1.maven.org/maven2";
	private static final Method ADD_URL_METHOD;

	static
	{
		try
		{
			ADD_URL_METHOD = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			ADD_URL_METHOD.setAccessible(true);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	}

	private final Class<?> main;
	private final File     folder;

	/**
	 * Create an instance for CLibrary. It is not recommended because the main class will be null!
	 */
	public CLibrary()
	{
		this(null);
	}

	/**
	 * Create an instance for CLibrary with the main class.
	 * @param main - Class for add dependency into the classloader.
	 */
	public CLibrary(Class<?> main)
	{
		this(main, new File("libs"));
	}

	/**
	 * Create an instance for CLibrary with the main class and the lib's folder.
	 * @param main   - Class for add dependency into the classloader.
	 * @param folder - Folder where jar files, dependencies that are downloaded are saved.
	 */
	public CLibrary(Class<?> main, File folder)
	{
		this.main = main;
		this.folder = folder;
	}

	/**
	 * Load all the dependencies of the main class or of the class which is passed as a parameter in the constructor
	 */
	public void loads()
	{
		Objects.requireNonNull(main);
		load(main);
	}

	/**
	 * Load all the dependencies of the class of the object which is passed as a parameter.
	 * @param object - an object.
	 */
	public void load(@NotNull Object object)
	{
		load(object.getClass());
	}

	/**
	 * Load all the dependencies of the class which is passed as a parameter.
	 * @param classz - a class.
	 */
	public void load(Class<?> classz)
	{
		Dependencies[] dependencies = classz.getDeclaredAnnotationsByType(Dependencies.class);
		Dependency[]   libraries    = classz.getDeclaredAnnotationsByType(Dependency.class);

		if (libraries == null && dependencies == null)
			return;

		Repository repository = classz.getAnnotation(Repository.class);

		if (dependencies != null)
		{
			Arrays.stream(dependencies).forEach(dep -> {
				if (repository != null)
					Arrays.stream(dep.value()).forEach(library -> load(library, repository.url()));
				else
					Arrays.stream(dep.value()).forEach(library -> load(library, MAVEN_CENTRAL_URL));
			});
		}

		if (libraries != null)
		{
			if (repository != null)
				Arrays.stream(libraries).forEach(library -> load(library, repository.url()));
			else
				Arrays.stream(libraries).forEach(library -> load(library, MAVEN_CENTRAL_URL));
		}
	}

	/**
	 * Load a dependency. If it has already been downloaded before it will be loaded directly. Otherwise it will be downloaded to the repo which is
	 * passed as a parameter.
	 * <p>
	 * If it is not the default url which is passed as a parameter and the download does not succeed, this function will be called again with the
	 * default url.
	 * </p>
	 * @param dependency - the annotation.
	 * @param sUrl       - the url of the repository to download the jar file.
	 */
	private void load(Dependency dependency, String sUrl)
	{
		LOGGER.log(Level.INFO, "Loading dependency {0}:{1}:{2} from {3}", new Object[]{dependency.groupId(), dependency.artifactId(), dependency.version(), dependency.repository().url()});

		final String fileName     = dependency.artifactId() + "-" + dependency.version();
		final File   saveLocation = new File(folder, fileName + ".jar");

		if (!saveLocation.getParentFile().exists())
			saveLocation.getParentFile().mkdirs();

		if (!saveLocation.exists())
		{
			try
			{
				String repoUrl = sUrl.equals(dependency.repository().url()) ? sUrl : dependency.repository().url();
				if (!repoUrl.endsWith("/"))
					repoUrl += "/";

				repoUrl += "%s/%s/%s/%s-%s.jar";

				final URL url = new URL(String.format(repoUrl, dependency.groupId().replace(".", "/"), dependency.artifactId(), dependency.version(), dependency.artifactId(), dependency.version()));

				try (InputStream is = url.openStream())
				{
					Files.copy(is, saveLocation.toPath());
				}
				catch (IOException e)
				{
					LOGGER.log(Level.SEVERE, "Unable to download jar file from " + url.toString() + " !", e);

					if (!sUrl.equals(MAVEN_CENTRAL_URL))
					{
						LOGGER.info("Retry with maven central !");
						load(dependency, MAVEN_CENTRAL_URL);
						return;
					}
				}
			}
			catch (MalformedURLException e)
			{
				throw new RuntimeException("Unable to get the dependency !", e);
			}
		}

		if (!saveLocation.exists())
		{
			throw new RuntimeException("Unable to download the dependency !");
		}

		URLClassLoader classLoader = (URLClassLoader) main.getClassLoader();

		try
		{
			ADD_URL_METHOD.invoke(classLoader, saveLocation.toURI().toURL());
		}
		catch (Exception e)
		{
			throw new RuntimeException("Unable to load dependency: " + dependency.artifactId() + "in classloader !", e);
		}

		LOGGER.log(Level.INFO, "Loaded dependency {0} v{1} !", new Object[]{dependency.artifactId(), dependency.version()});
	}
}
