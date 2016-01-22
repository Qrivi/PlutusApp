<?php
    require_once './vendor/autoload.php';
    require_once './lib/Config.php';
    require_once './lib/DatabasePDO.php';
    require_once './lib/Authenticator.php';

    $app = new \Slim\Slim();

    $app->add(new \SlimNoCache\SlimNoCache());
    $app->add(new \Slim\Middleware\JwtAuthentication([
                                                         "secret" => "supersecretkeyyoushouldnotcommittogithub"
                                                     ]));

    $app->add( new \Slim\Middleware\HttpBasicAuthentication( [
                                                                 'path'          => '/v1',
                                                                 'realm'         => 'Plutus Private API',
                                                                 'secure'        => true,
                                                                 'relaxed'       => $http_allowed,
                                                                 'authenticator' => function ( $arguments ) use ( $app ){
                                                                     if( empty( $arguments ) || empty( $arguments['user'] ) || empty( $arguments['password'] ) )
                                                                         return false;

                                                                     $auth = new PlutusAPI\Authenticator();
                                                                     $result = $auth->authenticate( $arguments['user'], $arguments['password'] );
                                                                     if( is_string( $result ) ){
                                                                         //$arguments['messageinfo'] = $result;
                                                                         return false;
                                                                     }
                                                                     return true;
                                                                 },
                                                                 'error'         => function ( $arguments ) use ( $app ){
                                                                     $response['status'] = 'error';
                                                                     $response['message'] = $arguments['message'];
                                                                     $app->response->write( json_encode( $response, JSON_UNESCAPED_SLASHES ) );
                                                                 }
                                                             ] ) );

    $app->group( '/v1', function () use ( $app ){

        require_once './lib/v1/Parser.php';
        $user = ( empty( $_POST['studentId'] ) ? null : $_POST['studentId'] );
        $pass = ( empty( $_POST['password'] ) ? null : $_POST['password'] );
        $api = new PlutusAPI\Parser( $user, $pass );

        $app->get( '/', function () use ( $app ){

            echo 'valid endpoints: /credit, /verify and /transactions';
        } );

        // verifies user credentials
        $app->post( '/verify', function () use ( $app, $api ){
            $app->response->setStatus( 200 );
            $api->verify();
            exit;
        } );

        // gets the student's current credit
        $app->post( '/credit', function () use ( $app, $api ){
            $app->response->setStatus( 200 );
            $api->credit();
            exit;
        } );

        // fetches the student's credit history
        $app->post( '/transactions(/:page)', function ( $page = 1 ) use ( $app, $api ){
            $app->response->setStatus( 200 );
            $api->transactions( $page );
            exit;
        } );

        // fetches the student's credit history
        $app->post( '/transactions/topups', function () use ( $app ){
            // TODO write cool code
        } );

        // fetches the student's credit history
        $app->post( '/transactions/expenses', function () use ( $app ){
            //TODO write cool code
        } );

        // fetches the student's info like name and swag level
        $app->post( '/info', function () use ( $app ){
            //TODO write cool code
        } );

    } );

    $app->run();
