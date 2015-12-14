<?php

    namespace PlutusAPI;

    class Config{
        const PRODUCTION = false;       // Production requires HTTPS and disables test user

        const TEST_USER = '';           // fictional user that will return dummy data
        const TEST_PASS = '';           // that user's password

        const EMAIL = '';               // address the API will send e-mail from

        const DB_TYPE = '';             // the type of the API clients database
        const DB_HOST = '';             // the host of the API clients database
        const DB_USER = '';             // the user that will connect to the database
        const DB_PASS = '';             // that user's password
        const DB_NAME = '';             // the name of the API clients database
        const DB_CHARSET = '';          // the charset the connection will use
    }
