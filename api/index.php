<?php
    require( './vendor/autoload.php' );
    $app = new \Slim\Slim();

    // app routes
    $app->get( '/', function () use ( $app ){
        $app->response->setStatus( 200 );
        echo 'Hello World';
    } );

    $app->run();