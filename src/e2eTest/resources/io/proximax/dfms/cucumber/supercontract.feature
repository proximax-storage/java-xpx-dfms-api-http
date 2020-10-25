Feature: Super contract operations
  Use supercontracts

  Scenario: run supercontract for new contract
    Given standard infrastructure is available
    And all replicators accept contracts
    And I generate random prefix
    And I compose new contract
    And I upload file "src/e2eTest/resources/watapp/helloworld.wat" as prefixed "helloworld.wat"
    And prefixed file "helloworld.wat" has size 299309 and cid "bAFYBEIHGELVOKMOEYMSFDEAKV4WYEZP32ATR4H2PPOPKI7E7UG5RE46GPA======"
    And I flush the drive
    When I deploy the supercontract using prefixed file "helloworld.wat"
    And I execute the supercontract function "hello" with gas 1234
    Then the supercontract is listed by the node
