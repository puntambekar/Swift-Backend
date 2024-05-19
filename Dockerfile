FROM openjdk:17
EXPOSE 8080
ADD target/swift-image-v1.jar swift-image-v1.jar
ENTRYPOINT ["java","-jar","/swift-image-v1.jar"]