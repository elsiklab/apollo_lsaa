environments {
    development {
        // sample config to turn on debug logging in development e.g. for apollo run-local
        dataSource{
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            username = "biocmd"
            url = "jdbc:postgresql://localhost/apollo_lsaa"
            driverClassName = "org.postgresql.Driver"
            dialect = "org.hibernate.dialect.PostgresPlusDialect"
        }
    }
    test {
        dataSource{
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            username = "biocmd"
            url = "jdbc:postgresql://localhost/apollo-test"
            driverClassName = "org.postgresql.Driver"
            dialect = "org.elsiklab.ImprovedPostgresDialect"
        }
    }
    production {
        dataSource{
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            username = "biocmd"
            url = "jdbc:postgresql://localhost/apollo_lsaa"
            driverClassName = "org.postgresql.Driver"
            dialect = "org.elsiklab.ImprovedPostgresDialect"
        }
    }
}

lsaa {
    bootstrap = true
}
