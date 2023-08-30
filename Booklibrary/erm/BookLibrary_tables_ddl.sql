
/* Drop Triggers */

DROP TRIGGER TRI_Books_book_id;
DROP TRIGGER TRI_members_member_id;
DROP TRIGGER TRI_Rental_RentNo;



/* Drop Tables */

DROP TABLE Rental CASCADE CONSTRAINTS;
DROP TABLE Books CASCADE CONSTRAINTS;
DROP TABLE members CASCADE CONSTRAINTS;



/* Drop Sequences */

DROP SEQUENCE SEQ_Books_book_id;
DROP SEQUENCE SEQ_members_member_id;
DROP SEQUENCE SEQ_Rental_RentNo;




/* Create Sequences */

CREATE SEQUENCE SEQ_Books_book_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_members_member_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_Rental_RentNo INCREMENT BY 1 START WITH 1;



/* Create Tables */

CREATE TABLE Books
(
	-- 도서 번호
	book_id number NOT NULL,
	-- 도서 제목
	book_name varchar2(150) NOT NULL,
	kwon varchar2(1) DEFAULT on null '1',
	PRIMARY KEY (book_id)
);


CREATE TABLE members
(
	-- 회원아이디
	member_id number NOT NULL,
	-- 중복이름을 구별하기 위한 nickname
	nickname varchar2(18 char) UNIQUE,
    pwd varchar2(100) not null,
	-- 회원이름
    name varchar2(20) NOT NULL,
    
	black Date DEFAULT on null sysdate-1 NOT NULL,
	PRIMARY KEY (member_id)
);



CREATE TABLE Rental
(
	RentNo number NOT NULL,
	-- 회원아이디
	member_id number NOT NULL,
	-- 책 번호
	book_id number NOT NULL,
	-- 대여날짜
	rental_date date DEFAULT on null sysdate NOT NULL,
	-- 반납예정일자
	BeReturn_date date DEFAULT on null sysdate+7 NOT NULL,
	PRIMARY KEY (RentNo)
);



/* Create Foreign Keys */

ALTER TABLE Rental
	ADD FOREIGN KEY (book_id)
	REFERENCES Books (book_id)
;


ALTER TABLE Rental
	ADD FOREIGN KEY (member_id)
	REFERENCES members (member_id)
;



/* Create Triggers */

CREATE OR REPLACE TRIGGER TRI_Books_book_id BEFORE INSERT ON Books
FOR EACH ROW
BEGIN
	SELECT SEQ_Books_book_id.nextval
	INTO :new.book_id
	FROM dual;
END;

/

CREATE OR REPLACE TRIGGER TRI_members_member_id BEFORE INSERT ON members
FOR EACH ROW
BEGIN
	SELECT SEQ_members_member_id.nextval
	INTO :new.member_id
	FROM dual;
END;

/

CREATE OR REPLACE TRIGGER TRI_Rental_RentNo BEFORE INSERT ON Rental
FOR EACH ROW
BEGIN
	SELECT SEQ_Rental_RentNo.nextval
	INTO :new.RentNo
	FROM dual;
END;

/
--기본 admin 계정삽입
insert into members values('','admin','0000','관리자','');


/* Comments */

COMMENT ON COLUMN Books.book_id IS '책 번호';
COMMENT ON COLUMN Books.book_name IS '책 제목';
COMMENT ON COLUMN members.member_id IS '회원아이디';
COMMENT ON COLUMN members.nickname IS '중복이름을 구별하기 위한 nickname';
COMMENT ON COLUMN members.name IS '회원이름';
COMMENT ON COLUMN Rental.member_id IS '회원아이디';
COMMENT ON COLUMN Rental.book_id IS '책 번호';
COMMENT ON COLUMN Rental.rental_date IS '대여날짜';
COMMENT ON COLUMN Rental.BeReturn_date IS '반납예정일자';


commit;
