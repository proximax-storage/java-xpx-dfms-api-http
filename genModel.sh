# generate the model
./gradlew clean openApiGenerate
# remove old generated model from workspace
rm -f src/gen/java/io/proximax/dfms/gen/model/*
# remove inline response classes as those are not used
rm -f build/generated/src/main/java/io/proximax/dfms/gen/model/InlineResponse*
# move the dtos over to workspace
mv build/generated/src/main/java/io/proximax/dfms/gen/model/* src/gen/java/io/proximax/dfms/gen/model/
