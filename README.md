![iotoasis](https://github.com/iotoasis/SO/blob/master/logo_oasis_m.png)


## Oasis TAS oneM2M Server

TAS oneM2M Server는  IoT 국제표준인 oneM2M을 지원하지 않는 3’rd Party 디바이스 연동을 지원하는 서버 프레임워크입니다. TAS oneM2M Server 소스를 활용하여 oneM2M을 지원하지 않는 디바이스라도 Oasis SI 서버에 연동할 수 있도록 개발할 수 있습니다. 해당 Open Source는 TAS Emulator Source로 디바이스를 oneM2M Server에 가상으로 연동할 수 있는 기능을 제공합니다.

 - Feature
   - oneM2M 비지원 Device Interface 기능 지원
   - oneM2M Mca 레퍼런스 포인트 지원
   - SO, SDA 연동 기능 지원
   - MariaDB 기반의 리소스 데이터 관리

## Usage
 1.Log4.j.properties 파일을 열어 로그가 기록될 위치 지정
   log4j.appender.filelog.File=F:/log/emul.log   -> 자신의 환경에 맞는 부분으로 변경

 2.bin 폴더아래 device.properties 파일 수정
   Period=    센싱된 값을 올려 주는 주기 (단위:초)

 3. testgogo.bat을 command 창에서 실행   

## Downloads
 - [Latest Release](https://github.com/iotoasis/TAS/releases/)

<br>

## Q&A
 - [IoT Oasis Q&A -- Commong soon]


## License
Licensed under the BSD License, Version 2.0
<br>


