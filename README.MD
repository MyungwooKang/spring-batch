jobLauncher : 배치 job 실행 역할
- param : job , jobParameter
- return : jobExecution

job : 실행시킬 작업. 논리적인 개념. jobConfiguration 과 대응

jobParameter : job 파라미터 집합. 
- job 식별 용도
- job 에서 참조하는 데이터

jobInstance : job + jobParameter

jobExecution : 단 한 번 시도되는 job 실행을 의미하는 기술적 개념
- 시작시간, 종료시간, 상태(시작/완료/실패), 종료상태의 속성을 가짐

jobRepository : 수행되는 job에 대한 정보를 담는 저장소
- 언제/몇번/결과 등의 metadata 저장

step : job 내 단계
- job은 하나 이상의 step 으로 구성

setpExecution : 단 한번 시도되는 job 실행을 의미
- 시작시간, 종료시간, 상태, 종료상태, commitCount, itemCount 속성

item : 처리할 데이터의 가장 작은 구성 요소
- ex) 파일의 한 줄, DB의 한 Row, XML 내 특정 엘리먼트 등

itemReader : Step 안에서 file 또는 DB 등에서 Item 을 읽어들임
- 더 읽어올 item이 없으면 read() 메소드에서 null 값을 반환하며, 그전까지 순차적으로 Item Return

ItemWriter : Step 안에서 File 또는 DB 등으로 Item 저장

ItemProcessor : ItemReader 에서 읽어 들인 Item에 대하여 필요한 로직처리 작업을 수행

Chunk : 하나의 Transaction 안에서 처리할 Item 집합
- commit 의 단위가됨.
-----

● Tasklet을 사용한 Task 기반 처리
- 배치 처리 과정이 비교적 쉬운 경우 쉽게 사용
- 대량 처리를 하는 경우 더 복잡 
- 하나의 큰 덩어리를 여러 덩어리로 나누어 처리하기 부적합

● Chunk를 사용한 chunk(덩어리) 기반 처리

- ItemReader, ItemProcessor, ItemWriter의 관계 이해 필요 
- 대량 처리를 하는 경우 Tasklet 보다 비교적 쉽게 구현
  (예를 들면 10,000개의 데이터 중 1,000개씩 10개의 덩어리로 수행)
-----

파티셔닝 프로세스 가능한가
: 전문통신이 동기식으로 되어있으니 동기식으로 구현


---
요구사항
1. 쉽게
2. jenkins schedule(trigger)
3. 주의사항
   1. item read/write native query 사용


mvp 구현
asset 개설이체 예시
1. job : 개설이체 
   1. step1 : 개설이체 요청 확인
      1. read : 
   2. step2 : 세틀 전문 전송
      1. read : read settle request data (status : open // 상태코드 확인 필요)
         1. 전문 내용 읽음
      2. process : send socket message 
      3. write : update settle request data (status : Fail or Success // 상태코드 확인 필요)
   3. step3 : 자산 반영 
      1. parmas : settle request row id 
      2. read : read ast base ???? 맞나
      3. process : 전문 파싱
      4. write : udpate

as-is table  
- MVP_SETTLE_REQUEST_DATA // 세틀 송수신 전문 및 상태
  - SEQ // 요청 unique key
  - SEND_MSG //전문 
  - STATUS // 전문 전송 상태
- MVP_US_MEM_AST_BASE // 자산 반영
  - TOT_AST_QTY //자산 수량
  - COIN_TYPE // C0100 : 원화






