package com.codingyard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class CodingyardConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private GlobalAdminConfiguration globalAdminConfiguration;

    public CodingyardConfiguration(@JsonProperty("database") DataSourceFactory database,
                                   @JsonProperty("globalAdmin") GlobalAdminConfiguration globalAdminConfiguration) {
        this.database = database;
        this.globalAdminConfiguration = globalAdminConfiguration;
    }

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public GlobalAdminConfiguration getGlobalAdminConfiguration() {
        return globalAdminConfiguration;
    }
}
