package fr.cleymax.clibrary;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * File <b>Dependency</b> located on fr.cleymax.clibrary is a part of CLibrary. Created the 10/04/2020
 * <p>
 * @author Clément P. (Cleymax)
 * <p>
 * Class that represents a dependency to load.
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Dependency {

	/**
	 * Get the instance of the repository to load the dependency.
	 * @return repository instance.
	 */
	@NotNull Repository repository() default @Repository(url = "https://repo1.maven.org/maven2");

	/**
	 * Get the group of the dependency.
	 * @return dependency's group.
	 */
	@NotNull String groupId();

	/**
	 * Get the artifact of the dependency.
	 * @return dependency's artifact.
	 */
	@NotNull String artifactId();

	/**
	 * Get the version of the dependency.
	 * @return dependency's version.
	 */
	@NotNull String version();

}
