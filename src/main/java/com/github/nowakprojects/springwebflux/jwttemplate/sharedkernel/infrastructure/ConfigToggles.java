package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure;

public class ConfigToggles {

    private ConfigToggles() {
    }

    public static class Database {

        private Database() {
        }
        public static final String IN_MEMORY = "database_inmemory";
        public static final String MONGO_DB = "database_mongodb";
    }
}
