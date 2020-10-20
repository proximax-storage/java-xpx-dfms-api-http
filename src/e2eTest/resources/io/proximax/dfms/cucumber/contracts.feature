Feature: Drive Contract
  I want to work with drive contracts

  Scenario: Compose new contract
    Given standard infrastructure is available
    And all replicators accept contracts
    When I compose new contract
    Then contract is created
    And contract was accepted by all replicators
  
  Scenario: List all contracts
    Given standard infrastructure is available
    And I can use contract "baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu" for my test
    When I query for all contracts
    Then the response will contain my contract