package com.github.hiendo.tsa.servertests;

import org.springframework.boot.env.PropertySourcesLoader;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * This is used to allow server tests to load the same properties as the server
 */
public class PropertySourcesFactory {
    private static final String NO_PROFILE = null;

    /**
     * Create default property sources from app properties files.
     *
     * @return property sources created.
     * @throws java.io.IOException if error occurs in reading the properties files.
     */
    public static MutablePropertySources createPropertySources() throws IOException {
        PropertySourcesLoader loader = new PropertySourcesLoader();

        MutablePropertySources mutablePropertySources = new MutablePropertySources();
        mutablePropertySources
                .addFirst(loader.load(new ClassPathResource("application.properties"), "app-classpath", NO_PROFILE));

        Resource testResourceOverride = new ClassPathResource("config/application.properties");
        if (testResourceOverride.exists()) {
            mutablePropertySources.addFirst(loader.load(testResourceOverride, "app-test-classpath", NO_PROFILE));
        }

        Resource fileSystemResourceOverride = new FileSystemResource("application.properties");
        if (fileSystemResourceOverride.exists()) {
            mutablePropertySources
                    .addFirst(loader.load(new FileSystemResource("application.properties"), "app-override", NO_PROFILE));
        }

        return mutablePropertySources;
    }
}
