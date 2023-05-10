## 프로젝트 정보
JVM 17 기준으로 개발<br />
Kotlin + SpringBoot + JUnit<br />
Kotest 활용 테스트 개발<br />
InMemoryDB로 H2 DB MySQL 형태로 활용<br />

### 테스트 실행법
./gradlew test

### API 실행
http 폴더의 stock.http를 사용하면 됩니다.<br />
POST http://localhost:8080/api/v1/stocks/samsung

### DB 조회
H2 Console로 조회 가능<br />
http://localhost:8080/h2-console <br />
url, id, password는 application-local 확인 <br />

### API Doc 조회
http://localhost:8080/swagger-ui/index.html
