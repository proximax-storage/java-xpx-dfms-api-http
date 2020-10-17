Feature: Network endpoints
  I want to make sure that network endpoints work as expected

  Scenario: Check that client is among replicator peers
    Given standard infrastructure is available
    And all replicators are connected
    When I retrieve DFMS client ID
    And I retrieve DFMS client addresses
    Then DFMS client is present among "replicator1" peers
    And DFMS client is present among "replicator2" peers
    And DFMS client is present among "replicator3" peers
    