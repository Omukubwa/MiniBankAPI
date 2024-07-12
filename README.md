# MiniBankAPI
Backend API for Mini Banking Application

#Java Version
This application is implemented using Java version 
openjdk version "17.0.11" 2024-04-16
OpenJDK Runtime Environment (build 17.0.11+9-Ubuntu-122.04.1)

#Database used is H2
Credentials and database name available in resources folder (application.properties file)

#The application runs on port 1963 but can  be changed in application.properties file (server.port property)
#To successfully run the application,
clone the main repository
Open using your preferred IDE e.g Intellij 
Let maven resolve the dependencies.
Run the application.

#Access the API endpoints via Swagger endpoint
http://localhost:1963/swagger-ui/index.html#

The application has these properties in application.properties file:
#Email Sender Configurations
org.sender=test@gmail.com
org.password=*******************
org.host=smtp.gmail.com
org.port=587
all these properties will be used in the application to facilitate email sending.

#Default Records
org.senior.admin.Id=212343474
org.senior.admin.name=Default Admin
org.senior.admin.email=test@gmail.com
org.default.roles=ROLE_ADMIN,ROLE_USER
spring.main.allow-circular-references=true

#Used to create the 2nd leg of a transaction  because a financial transaction must have a Debit and a Credit
#An assumption made it that these are some of the office accounts available for use when transacting
office.account=100200400
optional.credit.accounts=201202401,201202402,201202403,201202404
