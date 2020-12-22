# Iceberg

COMS 4156 Advanced software team project @ Columbia. Team Iceberg.
The reimbursement system is based on SpringBoot+Mybatis+Thymeleaf+layui, integrating with external api such as Paypal payout, OAuth, Email sending... 

You can check our back-end API document at [https://jacksnowwolf.github.io/Iceberg/](https://jacksnowwolf.github.io/Iceberg/)

![Java CI with Maven](https://github.com/JackSnowWolf/Iceberg/workflows/Java%20CI%20with%20Maven/badge.svg)
![CodeQL](https://github.com/JackSnowWolf/Iceberg/workflows/CodeQL/badge.svg)
![CheckStyle](https://github.com/JackSnowWolf/Iceberg/workflows/CheckStyle/badge.svg)
[![codecov](https://codecov.io/gh/JackSnowWolf/Iceberg/branch/master/graph/badge.svg?token=UYUKY06UP6)](https://codecov.io/gh/JackSnowWolf/Iceberg)

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

![image](https://github.com/JackSnowWolf/Iceberg/blob/master/picture/architecture.png)

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
- Front-End Framework: LayUI(www.layui.com)
- Layui is a UI framework written with its own module specification, following the writing and organization of native HTML/CSS/JS, with very low thresholds for ready-to-use. Learning tutorial: https://www.layui.com/doc/
- Back-End Framework: https://spring.io/projects/spring-boot

## TODO
- [ ] Optimize backend controller structure.
- [ ] Beatify frontend code.
- [ ] Decouple frontend and backend.

## Documentation

- Preliminary Proposal: [preliminary proposal pdf](docs/Preliminary%20Project%20Proposal.pdf)
- Revised Proposal: [revised proposal pdf](docs/Revised%20Project%20Proposal.pdf)
- First Iteration Report: [first iteration report pdf](docs/First%20Iteration%20Report.pdf)
- First Demo Report: [first demo report pdf](docs/Initial%20Demo.pdf)
- Second Iteration Report: [second iteration report pdf](docs/Second%20Iteration%20Report.pdf)
- Final Demo Report: [final demo report pdf](docs/Final%20Demo.pdf)
- Demo Video: [Demo Video For Reimbursement System(Team Iceberg From COMS 4156)](https://www.youtube.com/watch?v=4-8oI7-U4Xw)

## [Contribution](CONTRIBUTING.md)

All Four of our team try our best to contribute to this project.

## Acknowledgements

Thanks to Professor Gail Kaiser, the instructor of our course, who brought us to the Software Development world and let us realize the importance of the workflow processes, techniques and "best practices" software engineers need to know to develop consumer and business software.

Thanks to Rennah Weng, TA of our course, who provided very inspiring and helpful ideas about our reimbursement system.

## Contact

- Weijie Huang `wh2447`: [email](mailto:wh2447@columbia.edu.com)
- Zeyang Chen `zc2483`: [email](mailto:zc2483@columbia.edu.com)
- Wenjie Chen `wc2685`: [email](mailto:wc2685@columbia.edu.com)
- Chong Hu `ch3467`: [email](mailto:ch3467@columbia.edu.com)

## License

Copyright (c) 2020, `Iceberg`. All rights reserved.

The code is distributed under a MIT license. See [`LICENSE`](LICENSE) for information.

