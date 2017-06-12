package net.coderbee.rpc.core.extension;

import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.URLParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缁勪欢瀹炵幇鐨勫姞杞藉櫒銆傚彧鍔犺浇绫诲瀷涓� T 鐨勬墍鏈夌粍浠跺疄鐜般��
 *
 * @author coderbee on 2017/6/6.
 */
public class ExtensionLoader<T> {
	private static final String SPI_PATH_PREFIX = "META-INF/services/";
	private static Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
	private static ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoaders = new ConcurrentHashMap<>();
	private ClassLoader classLoader;
	private Class<T> type;

	private ConcurrentMap<String, T> singletonInstances = new ConcurrentHashMap<>();
	private ConcurrentMap<String, Class<T>> extensionClasses = null;
	private volatile boolean init = false;

	private ExtensionLoader(Class<T> type) {
		this(type, null);
	}

	private ExtensionLoader(Class<T> type, ClassLoader classLoader) {
		this.type = type;
		this.classLoader = classLoader;
	}

	public static <T> T getSpi(Class<T> type, net.coderbee.rpc.core.URL url, URLParamType paramType) {
		ExtensionLoader<T> extensionLoader = getExtensionLoader(type);
		String spiName = url.getParameter(paramType.getName());
		return extensionLoader.getExtension(spiName);
	}

	public static <T> T getSpi(Class<T> type, String spiName) {
		ExtensionLoader<T> extensionLoader = getExtensionLoader(type);
		return extensionLoader.getExtension(spiName);
	}

	public static synchronized <T> ExtensionLoader<T> initExtensionLoader(Class<T> type) {
		ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(type);
		if (loader == null) {
			loader = new ExtensionLoader<T>(type);
			extensionLoaders.putIfAbsent(type, loader);
		}
		return loader;
	}

	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
		checkInterfaceType(type);

		ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(type);
		if (loader == null) {
			loader = initExtensionLoader(type);
		}

		return loader;
	}

	private static <T> void checkInterfaceType(Class<T> type) {
		if (type == null) {
			throw new RpcException("extension class type is null");
		}

		if (!type.isInterface()) {
			throw new RpcException("type is not interface");
		}

		if (!type.isAnnotationPresent(Spi.class)) {
			throw new RpcException("type is not Spi annotation present");
		}
	}

	private void insureInit() {
		if (!init) {
			init();
		}
	}

	private synchronized void init() {
		List<String> classNames = loadClassNames();
		extensionClasses = loadClass(classNames);
		init = true;
	}

	private List<String> loadClassNames() {
		String fullPath = SPI_PATH_PREFIX + type.getName();
		List<String> classNames = new ArrayList<String>();
		try {

			Enumeration<URL> resources;
			if (classLoader == null) {
				resources = ClassLoader.getSystemResources(fullPath);
			} else {
				resources = classLoader.getResources(fullPath);
			}
			if (resources == null || !resources.hasMoreElements()) {
				return Collections.emptyList();
			}

			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				parseUrl(type, url, classNames);
			}
		} catch (IOException e) {
			throw new RpcException("load SPI[" + type.getName() + "] implements error", e);
		}
		return classNames;
	}

	private ConcurrentMap<String, Class<T>> loadClass(List<String> classNames) {
		ConcurrentMap<String, Class<T>> spiName2classMap = new ConcurrentHashMap<>();
		for (String className : classNames) {
			try {
				Class<T> clazz;
				if (classLoader == null) {
					clazz = (Class<T>) Class.forName(className);
				} else {
					clazz = (Class<T>) Class.forName(className, true, classLoader);
				}

				String spiName = getSpiName(clazz);
				if (spiName2classMap.containsKey(spiName)) {
					logger.error("spiName[" + spiName + "] already exists");
					throw new RpcException("spiName[" + spiName + "] already exists");
				}

				spiName2classMap.putIfAbsent(spiName, clazz);

			} catch (ClassNotFoundException e) {
				logger.error("load SPI class [" + className + "] error", e);
			}
		}
		return spiName2classMap;
	}

	private String getSpiName(Class<T> clazz) {
		SpiMeta spi = clazz.getAnnotation(SpiMeta.class);
		if (spi != null) {
			return spi.name();
		}
		return clazz.getSimpleName();
	}

	private void parseUrl(Class<T> type, URL url, List<String> classNames) {
		try (InputStream ins = url.openStream();
		     BufferedReader reader = new BufferedReader(new InputStreamReader(ins))) {

			String line;
			while ((line = reader.readLine()) != null) {
				int i = line.indexOf('#');
				if (i != -1) {
					line = line.substring(0, i);
				}
				line = line.trim();
				if ("".equals(line)) {
					continue;
				}

				if (!classNames.contains(line)) {
					classNames.add(line);
				}
			}
		} catch (IOException e) {
			logger.error("parseUrl:" + url + " error", e);
		}
	}

	public Class<T> getExtensionClass(String spiName) {
		insureInit();

		return extensionClasses.get(spiName);
	}

	public T getExtension(String spiName) {
		insureInit();

		if (spiName == null) {
			return null;
		}

		Spi spi = type.getAnnotation(Spi.class);

		try {
			if (spi.scope() == Scope.SINGLETON) {
				return getSingleInstance(spiName);
			} else {
				Class<T> clazz = extensionClasses.get(spiName);
				if (clazz == null) {
					return null;
				}
				return clazz.newInstance();
			}
		} catch (Exception e) {
			logger.error("getExtension [" + spiName + "] error", e);
		}
		return null;
	}

	private synchronized T getSingleInstance(String spiName) throws IllegalAccessException, InstantiationException {
		T t = singletonInstances.get(spiName);
		if (t == null) {
			Class<T> claz = extensionClasses.get(spiName);
			t = claz.newInstance();
			singletonInstances.put(spiName, t);
		}
		return t;
	}

}
