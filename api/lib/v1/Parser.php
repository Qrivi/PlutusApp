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

        private function printJSON( $name, array $data, $extrameta = null ){

            $arr = [
                $name => [
                    'meta'  => [
                        'timestamp' => gmdate( 'Y-m-d\TH:i:s' ) . date( 'O' ),
                        'studentId' => $this->user
                    ],
                    'error' => null,
                    'data'  => null
                ]
            ];

            if( $extrameta != null )
                $arr[$name]['meta'] = array_merge( $arr[$name]['meta'], $extrameta );
            $arr[$name] = array_merge( $arr[$name], $data );

            echo json_encode( $arr, JSON_PRETTY_PRINT );
            exit;
        }

        public function verify(){
            if( $this->user == null || $this->pass == null ){
                $result['error'] = 'Missing credentials';
            }else{
                $this->scraper->setCredentials( $this->user, $this->pass );
                $this->scraper->fetchPage();

                $result['data'] = [ 'valid' => $this->scraper->isUserValid() ];
            }
            $this->printJSON( 'verify', $result );
        }

        public function balance(){
            if( $this->user == null || $this->pass == null ){
                $result['error'] = 'Missing credentials';
            }else{
                $this->scraper->setCredentials( $this->user, $this->pass );
                $this->scraper->fetchPage();

                $result['data'] = [ 'credit' => $this->scraper->getCredit() ];
            }
            $this->printJSON( 'balance', $result );
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