<?php
    namespace PlutusAPI;

    class Scraper{

        public $user;
        public $pass;

        public $page;

        public function __construct( $user = null, $pass = null ){
            if( $user != null && $pass != null )
                $this->setCredentials( $user, $pass );
        }

        public function setCredentials( $user, $pass ){
            $this->user = $user;
            $this->pass = $pass;
        }

        public function fetchPage( $page = 1 ){
            if( Config::PRODUCTION ){
                // TODO Fetch a page with transactions history on intranet using e.g. curl and parse it to $page as a DOMDocument
                $p = null;
            }else{
                $p = new \DOMDocument();
            }
            $this->page = $p;
        }

        public function isUserValid(){
            if( Config::PRODUCTION ){
                // TODO Check whether or not nav#block-system-user-menu exists on the page (means user is logged in successfully)
            }else{
                if( $this->user == Config::TEST_USER && $this->pass == Config::TEST_PASS )
                    return [
                        'valid' => true,
                        'firstName' => 'Benny',
                        'lastName' => 'Echtman'
                    ];
            }
            return [
                'valid' => false,
                'firstName' => null,
                'lastName' => null
            ];
        }

        public function getCredit(){
            if( Config::PRODUCTION ){
                // TODO Returns the current credit displayed on the page
            }else{
                return 38.52;
            }
            return -1;
        }

        public function getTransactions( $page ){
            if( Config::PRODUCTION ){
                // TODO Returns the current credit displayed on the page
            }else{
                if( $page == 1 ){
                    return [
                        [
                            'amount'    => 1.5,
                            'type'      => 'expense',
                            'timestamp' => '2015-11-28T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Drankenautomaat',
                                'description' => 'Test met wat <b>bold tekst</b> :) <br>hupla nieuwe lijn!'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 4.5,
                            'type'      => 'expense',
                            'timestamp' => '2015-11-26T18:34:07+0100',
                            'details'   => [
                                'title'       => 'Studentenservice',
                                'description' => 'Test met wat <u>onderlijnde tekst</u> :) <br>hupla nieuwe lijn!'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Proximus',
                                'lat'  => 50.845546,
                                'lng'  => 4.728498
                            ]
                        ], [
                            'amount'    => 25,
                            'type'      => 'topup',
                            'timestamp' => '2015-11-26T18:24:07+0100',
                            'details'   => [
                                'title'       => 'Oplaadpunt',
                                'description' => 'Test met wat <i>scheve tekst</i> :) <br><br>hupla GANSE lijn!'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Proximus',
                                'lat'  => 50.845546,
                                'lng'  => 4.728498
                            ]
                        ], [
                            'amount'    => .7,
                            'type'      => 'expense',
                            'timestamp' => '2015-11-26T18:14:07+0100',
                            'details'   => [
                                'title'       => 'Koffieautomaat',
                                'description' => 'Wat gebeurt er met hele lange strings? Wat gebeurt er met hele lange strings? Wat gebeurt er met hele lange strings? Wat gebeurt er met hele lange strings? Wat gebeurt er met hele lange strings? Wat gebeurt er met hele lange strings? Wat gebeurt er met hele lange strings? Wat gebeurt er met hele lange strings? Wat gebeurt er met hele lange strings? '
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Proximus',
                                'lat'  => 50.845546,
                                'lng'  => 4.728498
                            ]
                        ], [
                            'amount'    => 6.1,
                            'type'      => 'expense',
                            'timestamp' => '2015-10-30T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Studentenrestaurant',
                                'description' => 'Pas op hier zit een <div>div goed</div>verstopt!'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Hertogstraat',
                                'lat'  => 50.856518,
                                'lng'  => 4.703312
                            ]
                        ], [
                            'amount'    => 5,
                            'type'      => 'topup',
                            'timestamp' => '2015-10-29T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Oplaadpunt',
                                'description' => '<ul><li>List item 1</li><li>List item 1</li><li>List item 2</li><li>List item 3</li><li>List item 69</li><li>List item 420</li></ul>'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Hertogstraat',
                                'lat'  => 50.856518,
                                'lng'  => 4.703312
                            ]
                        ], [
                            'amount'    => .67,
                            'type'      => 'expense',
                            'timestamp' => '2015-10-27T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Printer',
                                'description' => '<p>List werkt blijkbaar (nog) niet</p><p>JAmmer</p><p>Hier gebruik ik paragraph</p>'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 1,
                            'type'      => 'expense',
                            'timestamp' => '2015-10-27T17:44:07+0100',
                            'details'   => [
                                'title'       => 'Drankenautomaat',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 1.5,
                            'type'      => 'expense',
                            'timestamp' => '2015-10-21T17:44:07+0100',
                            'details'   => [
                                'title'       => 'Drankenautomaat',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 6.8,
                            'type'      => 'expense',
                            'timestamp' => '2015-10-02T17:44:07+0100',
                            'details'   => [
                                'title'       => 'Studentenrestaurant',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ]
                    ];
                }else if( $page == 2 ){
                    return [
                        [
                            'amount'    => 1.5,
                            'type'      => 'expense',
                            'timestamp' => '2015-10-01T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Drankenautomaat',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 4.5,
                            'type'      => 'expense',
                            'timestamp' => '2015-09-26T18:34:07+0100',
                            'details'   => [
                                'title'       => 'Studentenservice',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Proximus',
                                'lat'  => 50.845546,
                                'lng'  => 4.728498
                            ]
                        ], [
                            'amount'    => 25,
                            'type'      => 'topup',
                            'timestamp' => '2015-09-26T18:24:07+0100',
                            'details'   => [
                                'title'       => 'Oplaadpunt',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Proximus',
                                'lat'  => 50.845546,
                                'lng'  => 4.728498
                            ]
                        ], [
                            'amount'    => .7,
                            'type'      => 'expense',
                            'timestamp' => '2015-09-26T18:14:07+0100',
                            'details'   => [
                                'title'       => 'Koffieautomaat',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Proximus',
                                'lat'  => 50.845546,
                                'lng'  => 4.728498
                            ]
                        ], [
                            'amount'    => 6.1,
                            'type'      => 'expense',
                            'timestamp' => '2015-05-30T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Studentenrestaurant',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Hertogstraat',
                                'lat'  => 50.856518,
                                'lng'  => 4.703312
                            ]
                        ], [
                            'amount'    => 5,
                            'type'      => 'topup',
                            'timestamp' => '2015-05-29T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Oplaadpunt',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Hertogstraat',
                                'lat'  => 50.856518,
                                'lng'  => 4.703312
                            ]
                        ], [
                            'amount'    => .67,
                            'type'      => 'expense',
                            'timestamp' => '2015-05-27T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Printer',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 1,
                            'type'      => 'expense',
                            'timestamp' => '2015-05-27T17:44:07+0100',
                            'details'   => [
                                'title'       => 'Drankenautomaat',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 1.5,
                            'type'      => 'expense',
                            'timestamp' => '2015-05-21T17:44:07+0100',
                            'details'   => [
                                'title'       => 'Drankenautomaat',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 6.8,
                            'type'      => 'expense',
                            'timestamp' => '2015-05-2T17:44:07+0100',
                            'details'   => [
                                'title'       => 'Studentenrestaurant',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ]
                    ];
                } else if( $page == 3 ){
                    return [
                        [
                            'amount'    => 1.5,
                            'type'      => 'expense',
                            'timestamp' => '2015-04-01T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Drankenautomaat',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 4.5,
                            'type'      => 'expense',
                            'timestamp' => '2015-03-26T18:34:07+0100',
                            'details'   => [
                                'title'       => 'Studentenservice',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Proximus',
                                'lat'  => 50.845546,
                                'lng'  => 4.728498
                            ]
                        ], [
                            'amount'    => 25,
                            'type'      => 'topup',
                            'timestamp' => '2015-03-26T18:24:07+0100',
                            'details'   => [
                                'title'       => 'Oplaadpunt',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Proximus',
                                'lat'  => 50.845546,
                                'lng'  => 4.728498
                            ]
                        ], [
                            'amount'    => .7,
                            'type'      => 'expense',
                            'timestamp' => '2015-03-26T18:14:07+0100',
                            'details'   => [
                                'title'       => 'Koffieautomaat',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Proximus',
                                'lat'  => 50.845546,
                                'lng'  => 4.728498
                            ]
                        ], [
                            'amount'    => 6.1,
                            'type'      => 'expense',
                            'timestamp' => '2015-03-30T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Studentenrestaurant',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Hertogstraat',
                                'lat'  => 50.856518,
                                'lng'  => 4.703312
                            ]
                        ], [
                            'amount'    => 5,
                            'type'      => 'topup',
                            'timestamp' => '2015-02-29T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Oplaadpunt',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Hertogstraat',
                                'lat'  => 50.856518,
                                'lng'  => 4.703312
                            ]
                        ], [
                            'amount'    => .67,
                            'type'      => 'expense',
                            'timestamp' => '2015-02-27T18:44:07+0100',
                            'details'   => [
                                'title'       => 'Printer',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ], [
                            'amount'    => 1,
                            'type'      => 'expense',
                            'timestamp' => '2014-12-27T17:44:07+0100',
                            'details'   => [
                                'title'       => 'Drankenautomaat',
                                'description' => 'Lorem ipsum dolor sit amet (HTML is ok)'
                            ],
                            'location'  => [
                                'name' => 'UCLL Campus Gasthuisberg',
                                'lat'  => 50.881079,
                                'lng'  => 4.672941
                            ]
                        ]
                    ];
                }else{
                    return [];
                }
            }
            return null;
        }

    }