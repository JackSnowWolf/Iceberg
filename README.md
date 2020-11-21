# Iceberg

COMS 4156 Advanced software team project @ Columbia. Team Iceberg.
The reimbursement system is based on SpringBoot+Mybatis+Thymeleaf+layui, integrating with external api such as Paypal payout, OAuth, Email sending... 

![Java CI with Maven](https://github.com/JackSnowWolf/Iceberg/workflows/Java%20CI%20with%20Maven/badge.svg)
![CodeQL](https://github.com/JackSnowWolf/Iceberg/workflows/CodeQL/badge.svg)

## Introduction

Our project implement a reimbursement platform to manage reimbursement requests. The application value of the budget reimbursement system is reflected in three functional parts: the budget function; the reimbursement function and the data visualization function. The administrator(e.g. CEO of the company) can create groups. Groups specify different sectors of a company, such as development group, testing group, hr group. Each group can create an account for each employee. Employees can use this account to apply for financial reimbursement. The group manager can review and approve employee claims. In addition, the employee can view the financial status of the reimbursement data over a period of time. Through the dashboard, one can see the details of the reimbursements in numbers and different charts in order to insight from historical reimbursements and manage expenses. Please check our doc for detail.

## Tech stack

- IDE: IntelliJ IDEA
- Back-end: SpringBoot 2.1.1
  - Database: AWS RDS/Mysql 5.7.27
  - JDK version: 1.8.0_181
  - Java template engine: Thymeleaf
  - Test: Junit 5/4
- Front-end: 
  - Base: Html+Css+JavaScipt
  - Framework: Layui+JQuery
## Architecture: 

https://github.com/JackSnowWolf/Iceberg/blob/master/picture/architecture.png

## Structure

```bash
.
├── doc
├── pic # -> project architecture
├── src 
│   ├── main # -> source code
│   │   ├── java/com/iceberg # -> spring boot back-end core
│   │   │   ├── configs # -> configuration
│   │   │   ├── controller # -> handle the navigation between the different views
│   │   │   ├── dao # -> data access object
│   │   │   ├── entity # -> data model
│   │   │   ├── externalapi # -> external api used in our reimbursement system
│   │   │   ├── service # -> methods used by controller
│   │   │   ├── utils # -> support
│   │   │   └── IcebergApplication.java # -> starter
│   │   └── resources # -> front-end core
│   │       ├── mappers # -> spring boot mabatis mapper files
│   │       ├── static # -> packages used in front-end
│   │       ├── templates # -> HTML files shown in front-end
│   │       └── application.yml # -> setting
│   └── test # -> all tests (63) 
├── sql # -> sql back up file
├── .circleci
├── .github/workflows
├── LICENSE
├── CONTRIBUTING.md
└── README.md
```
## Usage

### Environment Dependencies

- install `maven`
- install `java 8`
- `mvn install -Dmaven.test.skip=true` download prerequisites dependencies
without test. If failed, please `mvn clean` first.

### Synopsis

```bash
# After git clone the respository
mvn spring-boot:run
```

Then the spring boot application is running on `localhost:8080`.

We have already configured an AWS database (icegerg) for the system. 
If failed or slow, you can create a local database `iceberg` and edit the connection string in application.yml. The back up file in sql folder is created for you.

### How to test

#### command
```bash
mvn test
```

or

```bash
mvn surefire:test
```

### Reference

## TODO
- [ ] TODO1
- [ ] TODO2

## Documentation

- Link to proposal
- Link to report

## [Contribution](CONTRIBUTING.md)

- 
- 

## Acknowledgements

## Contact


- Weijie Huang `wh2447`: [email](mailto:wh2447@columbia.edu.com)
- Zeyang Chen `zc2483`: [email](mailto:zc2483@columbia.edu.com)
- Wenjie Chen `wc2685`: [email](mailto:wc2685@columbia.edu.com)
- Chong Hu `ch3467`: [email](mailto:ch3467@columbia.edu.com)

## License

Copyright (c) 2020, `Iceberg`. All rights reserved.

The code is distributed under a MIT license. See [`LICENSE`](LICENSE) for information.

