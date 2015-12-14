<?php

    namespace PlutusAPI;

    class Authenticator{
        public $pdo;

        public function __construct(){
            $this->pdo = DatabasePDO::getInstance();
        }

        public function authenticate( $user, $password ){
            $sql = 'SELECT name, status, user
                    FROM apps
                    WHERE name = :user
                      AND password = :password;';
            $stmt = $this->pdo->prepare( $sql );
            $stmt->bindValue( ':user', $user );
            $stmt->bindValue( ':password', $password );

            if( $stmt->execute() ){
                $user = $stmt->fetch( \PDO::FETCH_ASSOC );

                if( !empty( $user ) ){
                    switch( $user['status'] ){
                        case 'active':
                            return true;
                        case 'too_many_requests':
                            return 'Access was revoked because of regressive spamming of this app.';
                        default:
                            return 'Something went horribly wrong :(';
                    }
                }
            }
            return 'Failed to connect to the database.';
        }

        private function sendMail( $to, $message ){
            // TODO
        }
    }
