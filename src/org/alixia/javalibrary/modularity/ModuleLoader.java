package org.alixia.javalibrary.modularity;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import branch.alixia.unnamed.Datamap;

public class ModuleLoader<M> {
	private final Class<M> moduleClass;

	private final String manifestLocation;
	private final String launchClassKey;

	public ModuleLoader(Class<M> moduleClass, String manifestLocation) {
		this(moduleClass, manifestLocation, "launch-class");
	}

	public ModuleLoader(Class<M> moduleClass, String manifestLocation, String launchClassKey) {
		this.moduleClass = moduleClass;
		this.manifestLocation = manifestLocation;
		this.launchClassKey = launchClassKey;
	}

	public ReadModule<M> read(File file) throws ModuleLoadException {

		try (ZipFile jar = new JarFile(file)) {
			ZipEntry entry = jar.getEntry(manifestLocation);
			if (entry == null)
				throw new ModuleLoadException("Invalid module; The manifest file could not be located inside the jar.");
			Datamap datamap = Datamap.read(jar.getInputStream(entry));

			String launchClass = datamap.get(launchClassKey);
			URL location = file.toURI().toURL();

			if (launchClass == null)
				throw new ModuleLoadException(
						"Invalid module manifest file. The manifest must denote a launch class for the module.");

			return new ReadModule<>(moduleClass, new URLClassLoader(new URL[] { location }), file, launchClass);

		} catch (Exception e) {
			throw new ModuleLoadException("An unexpected error occurred while loading a module.", e);
		}
	}

}
