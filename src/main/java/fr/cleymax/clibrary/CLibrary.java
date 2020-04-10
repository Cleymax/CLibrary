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

	public CLibrary()
	{
		this(null);
	}

	public CLibrary(Class<?> main)
	{
		this(main, new File("libs"));
	}

	public CLibrary(Class<?> main, File folder)
	{
		this.main = main;
		this.folder = folder;
	}

	public void loads()
	{
		Objects.requireNonNull(main);
		load(main);
	}

	public void load(@NotNull Object object)
	{
		load(object.getClass());
	}

	public void load(Class<?> classz)
	{
		Dependency[] libraries = classz.getDeclaredAnnotationsByType(Dependency.class);

		if (libraries == null)
			return;

		Repository repository = classz.getAnnotation(Repository.class);

		if (repository != null)
			Arrays.stream(libraries).forEach(library -> load(library, repository.url()));
		else
			Arrays.stream(libraries).forEach(library -> load(library, MAVEN_CENTRAL_URL));
	}

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
				String repoUrl = sUrl;
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
					LOGGER.log(Level.SEVERE, "Can't copy file from InputStream", e);

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
				throw new RuntimeException("Can't get the dependency !", e);
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
			throw new RuntimeException("Unable to load dependency: " + dependency.artifactId(), e);
		}

		LOGGER.log(Level.INFO, "Loaded dependency {0} v{1} !", new Object[]{dependency.artifactId(), dependency.version()});
	}
}
