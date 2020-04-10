package fr.cleymax.clibrary;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * File <b>Repository</b> located on fr.cleymax.clibrary is a part of CLibrary. Created the 10/04/2020
 * <p>
 * @author Clément P. (Cleymax)
 * <p>
 * A maven repository.
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Repository {

	/**
	 * Get the repository url.
	 * @return repository's url.
	 */
	@NotNull String url();
}
