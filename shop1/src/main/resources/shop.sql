-- src/main/resources/shop.sql

create table item (
id int primary key,
name varchar(30),
price int,
description varchar(100),
pictureUrl varchar(30)
);
 

insert into item values (1, '레몬', 50, '레몬에 포함된 구연산은 피로회복에 좋습니다. 비타민C 도 풍부합니다.','lemon.jpg');
insert into item values (2, '오렌지', 100, '비타민C 가 풍부합니다. 맛도 좋습니다.','orange.jpg');
insert into item values (3, '키위', 200, '비타민C 가 풍부합니다. 다이어트에 좋습니다.','kiui.jpg');
insert into item values (4, '포도', 300, '폴리페놀을 다량 함유하고 있어 항산화 작용을 합니다.', 'podo.jpg');
insert into item values (5, '딸기', 800, '비타민C를 다량 함유하고 있습니다.', 'ichigo.jpg');
insert into item values (6, '귤', 1000, '시네피린을 다량 함유하고 있어 감기예방에 좋습니다.', 'mikan.jpg');
commit;

select * from item

create table useraccount (
   userid varchar(10) primary key, -- 아이디
   password varchar(15),           -- 비밀번호 
   username varchar(20),           -- 이름 
   phoneno varchar(20),            -- 전화번호
   postcode varchar(7),            -- 우편번호
   address varchar(30),            -- 주소
   email varchar(50),              -- 이메일
   birthday datetime               -- 생일
);

select * from useraccount

CREATE TABLE sale (  -- 주문정보
	saleid int PRIMARY KEY,  
	userid varchar(10) NOT NULL, 
	saledate datetime,
	foreign key (userid) references useraccount (userid)
);

CREATE TABLE saleitem (   -- 주문상품
	saleid int ,
	seq int ,
	itemid int NOT NULL,
	quantity int,
	PRIMARY KEY (saleid, seq),
	foreign key (saleid) references sale (saleid),
	foreign key (itemid) references item (id)
);

create table board (
   num int primary key,   -- 게시글번호. 기본키
   writer varchar(30),    -- 작성자이름
   pass varchar(20),      -- 비밀번호
   title varchar(100),    -- 글제목
   content varchar(2000), -- 글내용
   file1 varchar(200),    -- 첨부파일명
   boardid varchar(2),    -- 게시판종류:1:공지사항,2:자유게시판,3:QNA
   regdate datetime,      -- 등록일시
   readcnt int(10),       -- 조회수. 상세보기 시 1씩증가
   grp int,               -- 답글 작성시 원글의 게시글번호
   grplevel int(3),       -- 답글의 레벨. 
   grpstep int(5)         -- 그룹의 출력 순서
);
select * from board

drop table comment;
create table comment ( -- 댓글등록
   num int  references board (num),  -- 게시물 번호
   seq int,                          -- 댓글번호
   writer varchar(30),               -- 댓글 작성
   content varchar(2000),            -- 댓글 내용
   regdate datetime,                 -- 댓글 작성일시
   primary key (num,seq)
)
select * from comment
# comment 테이블에 비밀번호 컬럼 추가하기
alter table comment add column pass varchar(20)   #가장 마지막에 컬럼 추가
# comment 테이블에 비밀번호 컬럼 제거하기
alter table comment drop column pass
# comment 테이블에 writer 컬럼 다음에 비밀번호 컬럼 추가하기
alter table comment add column pass varchar(20) after writer


