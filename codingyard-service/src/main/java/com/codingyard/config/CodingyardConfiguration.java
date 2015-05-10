package com.codingyard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.List;

public class CodingyardConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private Path solutionDir;

    @Valid
    private List<UserConfiguration> users = Lists.newArrayList();

    public CodingyardConfiguration(@JsonProperty("database") DataSourceFactory database,
                                   @JsonProperty("users") List<UserConfiguration> users,
                                   @JsonProperty("solutionDir") Path solutionDir) {
        this.database = database;
        this.users = users;
        this.solutionDir = solutionDir;
    }

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public List<UserConfiguration> getUsers() {
        return users;
    }

    public Path getSolutionDir() {
        return solutionDir;
    }
}
