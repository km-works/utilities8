/*
 * Copyright (c) 2015-2017 Christian P. Lerch, Vienna, Austria <christian.p.lerch@gmail.com>
 * All rights reserved.
 */
package kmworks.util.config;

import static com.google.common.base.Preconditions.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

/** Application configuration data holder.
 *  TODO: move to kmw-chasis-base
 * @author Christian P. Lerch
 */
public class AppConfig {

    private static final String PATH_ENV = "environment";
    private static final String MSG_ENV_NOT_SET = "Mandatory config property 'environment' not set";
    private static final String MSG_FILE_NOT_FOUND = "Config file not found";

    private static final AppConfig INSTANCE = new AppConfig();

    private final Config defaultConfig;
    private Config effectiveConfig;
    private String activeEnv;

    public AppConfig() {
        defaultConfig = ConfigFactory.load();
        if (defaultConfig.hasPath(PATH_ENV)) {
            activeEnv = defaultConfig.getString(PATH_ENV);
        } else {
            throw new IllegalArgumentException(MSG_ENV_NOT_SET);
        }
        effectiveConfig = defaultConfig.getConfig(activeEnv);
    }

    public static void overrideWith(@Nonnull final Config other) {
        if (checkNotNull(other).hasPath(PATH_ENV)) {
            INSTANCE.activeEnv = other.getString(PATH_ENV);
        } else {
            throw new IllegalArgumentException(MSG_ENV_NOT_SET);
        }
        INSTANCE.effectiveConfig = other.getConfig(INSTANCE.activeEnv).withFallback(INSTANCE.effectiveConfig);
    }

    public static void overrideWith(@Nonnull final File configFile) {
        if (!checkNotNull(configFile).exists() || !configFile.canRead()) {
            throw new IllegalArgumentException(MSG_FILE_NOT_FOUND + ": " + configFile.getAbsolutePath());
        }
        AppConfig.overrideWith(ConfigFactory.parseFile(configFile));

    }

    public static PropertyMap startsWith(@Nonnull final String prefix) {
        final Set<Map.Entry<String, ? extends Object>> set = getConfig().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .map(entry -> new AbstractMap.SimpleImmutableEntry<>(
                    entry.getKey().substring(prefix.length()),
                    entry.getValue().unwrapped()))
                .collect(Collectors.toSet());
        return PropertyMap.of(set);
    }

    public static Config getConfig() {
        return INSTANCE.effectiveConfig;
    }

    public static String getEnvironment() {
        return INSTANCE.activeEnv;
    }

    public static String getStringValue(String path) {
        return INSTANCE.getStringValue_(path);
    }

    public static String getStringValue(String path, String defValue) {
        return INSTANCE.getStringValue_(path, defValue);
    }

    public static Integer getIntValue(String path) {
        return INSTANCE.getIntValue_(path);
    }

    public static Integer getIntValue(String path, Integer defValue) {
        return INSTANCE.getIntValue_(path, defValue);
    }

    /*
      INSTANCE methods
     */
    
    private String getStringValue_(String path) {
        return getStringValue_(path, null);
    }

    private String getStringValue_(String path, String defValue) {
        if (effectiveConfig.hasPath(path)) {
            return effectiveConfig.getString(path);
        } else if (defaultConfig.hasPath(path)) {
            return defaultConfig.getString(path);
        } else {
            return defValue;
        }
    }

    private Integer getIntValue_(String path) {
        return getIntValue_(path, null);
    }

    private Integer getIntValue_(String path, Integer defValue) {
        if (effectiveConfig.hasPath(path)) {
            return effectiveConfig.getInt(path);
        } else if (defaultConfig.hasPath(path)) {
            return defaultConfig.getInt(path);
        } else {
            return defValue;
        }
    }

}
