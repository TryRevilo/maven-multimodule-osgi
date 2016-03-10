package main;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class App {

	public static void main(String[] args) throws BundleException, URISyntaxException {
		App app = new App();
		app.initialize();
	}

	private void initialize() throws BundleException, URISyntaxException {
		Map<String, String> map = new HashMap<String, String>();

		// make sure the cache is cleaned
		map.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);

		map.put("ds.showtrace", "true");
		map.put("ds.showerrors", "true");

		FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
		Framework framework = frameworkFactory.newFramework(map);

		System.out.println("Starting OSGi Framework");
		framework.init();
		
		// D:/standAloneDev/java/workingDir/eclipseProjects/maven-multimodule-osgi-master/dist/target/dist-1.0-SNAPSHOT-bin/plugins

		// String baseDir = "D:/standAloneDev/java/workingDir/eclipseProjects/maven-multimodule-osgi-master/dist/target/dist-1.0-SNAPSHOT-bin/plugins/";

		File baseDir = new File("target/dist-1.0-SNAPSHOT-bin/plugins/");
		String baseDirPath = baseDir.getAbsolutePath();

		loadScrBundle(framework);

		File provider = new File(baseDirPath, "OSGiDmHelloWorldProvider-1.0.jar");
		File consumer = new File(baseDirPath, "OSGiDmHelloWorldConsumer-1.0.jar");

		framework.getBundleContext().installBundle(provider.toURI().toString());
		framework.getBundleContext().installBundle(consumer.toURI().toString());

		for (Bundle bundle : framework.getBundleContext().getBundles()) {
			bundle.start();
			System.out.println("Bundle: " + bundle.getSymbolicName());
			if (bundle.getRegisteredServices() != null) {
				for (ServiceReference<?> serviceReference : bundle.getRegisteredServices())
					System.out.println("\tRegistered service: " + serviceReference);
			}
		}
	}

	private void loadScrBundle(Framework framework) throws URISyntaxException, BundleException {
		URL url = getClass().getClassLoader().getResource("org/apache/felix/scr/ScrService.class");
		if (url == null)
			throw new RuntimeException("Could not find the class org.apache.felix.scr.ScrService");
		String jarPath = url.toURI().getSchemeSpecificPart().replaceAll("!.*", "");
		System.out.println("Found declarative services implementation: " + jarPath);
		framework.getBundleContext().installBundle(jarPath).start();
	}
}