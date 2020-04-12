package fr.cleymax.clibrary;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * File <b>Dependencies</b> located on fr.cleymax.clibrary is a part of CLibrary. Created the 12/04/2020
 * <p>
 * @author Clément P. (Cleymax) Class to load multiple dependencies.
 * @since 1.0.2
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Dependencies {

	/**
	 * A table representing a list of dependencies.
	 * @return dependencies.
	 */
	Dependency[] value();
}
