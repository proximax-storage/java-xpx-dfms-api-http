Feature: Drive Operations
  I want to use drive to store and retrieve content

  Scenario: Add plain file to the root of drive
    Given I can use contract "baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu" for my test
    And I generate random prefix
    When I upload file "src/e2eTest/resources/simple/subdir/test_image_file.png" as prefixed "plain.png"
    Then prefixed file "plain.png" has size 581312 and cid "zdj7Wge9XgaLSfDQx9w5WrsjCKCgt3rHpLTu2bgTzk43B9wtP"
    
  Scenario: Add plain file to new subdirectory on drive
    Given I can use contract "baegbeibondkkrhxfprzwrlgxxltavqhweh2ylhu4hgo5lxjxpqbpfsw2lu" for my test
    And I generate random prefix
    When I create prefixed directory "plain"
    And I upload file "src/e2eTest/resources/simple/subdir/test_image_file.png" as prefixed "plain/plainfile.png"
    Then prefixed file "plain/plainfile.png" has size 581312 and cid "zdj7Wge9XgaLSfDQx9w5WrsjCKCgt3rHpLTu2bgTzk43B9wtP"
    
    