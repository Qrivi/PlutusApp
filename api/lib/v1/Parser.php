<?php
    namespace PlutusAPI;

    class Parser{

        public $scraper;
        public $user;
        public $pass;

        public function __construct( $user = null, $pass = null ){
            require_once 'Scraper.php';
            $this->user = $user;
            $this->pass = $pass;
            $this->scraper = new Scraper();
        }

        private function printJSON( $endpoint, array $data, $extrameta = null ){

            $arr = [
                'error' => null,
                'meta'  => [
                    'endpoint'  => $endpoint,
                    'timestamp' => date( 'Y-m-d\TH:i:sO' ),
                    'studentId' => $this->user
                ],
                'data'  => null
            ];

            if( $extrameta != null )
                $arr['meta'] = array_merge( $arr['meta'], $extrameta );
            $arr = array_merge( $arr, $data );

            echo json_encode( $arr, JSON_PRETTY_PRINT );
            exit;
        }

        public function verify(){
            if( $this->user == null || $this->pass == null ){
                $result['error'] = 'Missing credentials';
            }else{
                $this->scraper->setCredentials( $this->user, $this->pass );
                $this->scraper->fetchPage();

                $result['data'] = $this->scraper->isUserValid();
            }
            $this->printJSON( 'verify', $result );
        }

        public function credit(){
            if( $this->user == null || $this->pass == null ){
                $result['error'] = 'Missing credentials';
            }else{
                $this->scraper->setCredentials( $this->user, $this->pass );
                $this->scraper->fetchPage();

                $result['data'] = [ 'amount' => $this->scraper->getCredit() ];
            }
            $this->printJSON( 'credit', $result );
        }

        public function transactions( $page = 1 ){
            if( $this->user == null || $this->pass == null ){
                $result['error'] = 'Missing credentials';
            }else{
                $this->scraper->setCredentials( $this->user, $this->pass );
                $this->scraper->fetchPage();

                $result['data'] = $this->scraper->getTransactions( $page );
            }
            $this->printJSON( 'transactions', $result, [ 'page' => $page ] );
        }

    }