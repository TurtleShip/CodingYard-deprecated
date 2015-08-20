# Install guide for codingyard

## Requirements
Make sure you have the below installed
- Java 8
- Maven
- Nginx
- Postgresql

## Step by step
1. Pull codingyard repo.

  ```sh
    ~: git clone git@github.com:TurtleShip/CodingYard.git
  ```

2. Build codingyard jar using maven.
  ```sh
    ~: cd CodingYard
    ~/CodingYard: mvn clean package
  ```

3. Run backend server

  ```sh
    ~/CodingYard: java -jar codingyard-service/target/codingyard-service-0.0.1-SNAPSHOT.jar server codingyard-service/codingyard.yml
  ```

4. Set nginx configuration based on [this](configs/nginx.conf) file.

5. Start nginx.
  ```sh
    sudo nginx Start
  ```
