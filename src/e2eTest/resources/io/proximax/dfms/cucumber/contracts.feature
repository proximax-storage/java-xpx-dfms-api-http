Feature: Drive Contract
  I want to use this template for my feature file

  Scenario: Compose new contract
    Given replicators are connected
    And replicators accept contracts
    When I compose new contract
    Then contract is created
  
  Scenario: List all contracts
    Given I can use contract "baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu" for my test
    When I query for all contracts
    Then the response will contain my contract