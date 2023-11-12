# TCPSender POC

This is a POC for the TCPSender class.

## Server
Run the net.cox.messaging.server.TcpServer to create a ServerSocket and wait for data sent by the Spring Boot application.

## Spring Boot Application

Run gradle bootRun and test by sending data as follows:

url --location 'http://localhost:8080/send' \
--header 'Content-Type: application/json' \
--data '{
"ipAddress": "192.168.68.70",
"macAddress": "01:2a:3b:4c:5d:6e",
"binaryData": "VGhpcyBpcyBhIHRlc3QgZGF0YQ==",
"waitForResponse": true
}'


