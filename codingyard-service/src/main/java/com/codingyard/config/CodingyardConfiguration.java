package com.codingyard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;

public class CodingyardConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private GlobalAdminConfiguration globalAdminConfiguration;

    @Valid
    @NotNull
    private Path solutionDir;

    public CodingyardConfiguration(@JsonProperty("database") DataSourceFactory database,
                                   @JsonProperty("globalAdmin") GlobalAdminConfiguration globalAdminConfiguration,
                                   @JsonProperty("solutionDir") Path solutionDir) {
        this.database = database;
        this.globalAdminConfiguration = globalAdminConfiguration;
        this.solutionDir = solutionDir;
    }

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public GlobalAdminConfiguration getGlobalAdminConfiguration() {
        return globalAdminConfiguration;
    }

    public Path getSolutionDir() {
        return solutionDir;
    }
}
