Feature: Network endpoints
  I want to make sure that network endpoints work as expected

  Scenario: Check that connected client is among replicator peers
    Given standard infrastructure is available
    When I retrieve DFMS client ID
    And I retrieve DFMS client addresses
    Then DFMS client is present among "replicator1" peers
    And DFMS client is present among "replicator2" peers
    And DFMS client is present among "replicator3" peers

  Scenario: Check that we can connect and disconnect
    Given standard infrastructure is available
    And I retrieve DFMS client ID
    And I retrieve DFMS client addresses
    When replicator "replicator1" is disconnected from DFMS client
    Then DFMS client is not present among "replicator1" peers
    When replicator "replicator1" is connected to DFMS client
    Then DFMS client is present among "replicator1" peers
 