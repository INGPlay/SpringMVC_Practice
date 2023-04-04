-- conn system/?;

CREATE USER notebook IDENTIFIED BY "notebook";

GRANT CONNECT,RESOURCE TO notebook; 

-- conn notebook/?;

-- table 생성
CREATE TABLE account_tbl (
	a_id NUMBER,
	a_username varchar2(10) NOT NULL,
	a_password varchar2(20) NOT null,
	a_created DATE NOT NULL,
	CONSTRAINT account_pk PRIMARY KEY (a_id),
	CONSTRAINT account_uniq UNIQUE(a_username)
);

CREATE TABLE container_tbl (
	a_id NUMBER,
	c_id number,
	c_title varchar2(50) NOT NULL,
	CONSTRAINT container_pk PRIMARY KEY (a_id, c_id),
	CONSTRAINT container_fk FOREIGN KEY (a_id) REFERENCES account_tbl (a_id) ON DELETE CASCADE
);

CREATE TABLE post_tbl (
	a_id NUMBER,
	c_id number,
	p_id number,
	p_title varchar2(100) NOT NULL,
	p_content varchar2(1000) NOT NULL,
	p_created date NOT NULL,
	p_updated date NOT NULL,
	CONSTRAINT post_pk PRIMARY KEY (a_id, c_id, p_id),
	CONSTRAINT post_fk FOREIGN KEY (a_id, c_id) REFERENCES container_tbl (a_id, c_id) ON DELETE CASCADE
);
--

-- table 삭제
DROP table account_TBL;
DROP table CONTAINER_TBL ;
DROP TABLE POST_TBL ;
--

-- table 내용 삭제
DELETE FROM ACCOUNT_TBL;
DELETE FROM CONTAINER_TBL;
--

-- table 고유값을 위한 시퀀스 테이블 (유사 시퀀스)
CREATE TABLE CONTAINER_seq_tbl(
	a_id NUMBER,
	c_id_seq NUMBER NOT NULL,
	CONSTRAINT container_seq_pk PRIMARY KEY (a_id),
	CONSTRAINT container_seq_fk FOREIGN KEY (a_id) REFERENCES account_tbl (a_id) ON DELETE CASCADE
);

CREATE TABLE POST_seq_tbl(
	a_id NUMBER,
	c_id NUMBER,
	p_id_seq NUMBER NOT NULL,
	CONSTRAINT post_seq_pk PRIMARY KEY (a_id, c_id),
	CONSTRAINT post_seq_fk FOREIGN KEY (a_id, c_id) REFERENCES container_tbl (a_id, c_id) ON DELETE CASCADE
);

DROP TABLE CONTAINER_seq_TBL ;
DROP TABLE post_seq_tbl;
--

SELECT * FROM post_seq_tbl;

UPDATE POST_seq_tbl SET p_id_seq = p_id_seq + 1 WHERE a_id = 1 and c_id = 3;

UPDATE POST_seq_tbl SET p_id_seq = p_id_seq + 1 WHERE a_id = 1 and c_id = 1;

SELECT * from account_tbl;
select C_id_seq from CONTAINER_seq_tbl;

INSERT into container_seq_tbl (a_id, c_id_seq)
VALUES (2, 1);

UPDATE CONTAINER_seq_tbl SET c_id_seq = c_id_seq + 1
WHERE a_id = 1;

-- 시퀀스 테이블 삭제
DROP TABLE CONTAINER_SEQ_TBL ;
DROP TABLE post_seq_tbl;
--

-- Sequence 생성 및 삭제
 
BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE ' || 'a_id_seq';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -2289 THEN
      RAISE;
    END IF;
END;

CREATE SEQUENCE a_id_seq  
    START WITH 1 
    INCREMENT BY 1 
    MINVALUE 1   
    NOCYCLE;
--
select * from container_tbl WHERE a_id = 7;
SELECT * FROM post_tbl;

SELECT nvl(max(c_id),0)+1 from container_tbl WHERE c_id = 1;

insert into post_tbl(a_id, c_id, p_id, p_title, p_content, p_created, p_updated)
VALUES (7,
		1,
        (SELECT max(p_id) + 1 FROM post_tbl WHERE a_id = 7 and c_id = 1),
        '테스트입니다~33333', 
        '테스트입니다~ 내용입니다~3333',
        SYSDATE, 
        SYSDATE);
-- 조회
SELECT * FROM ACCOUNT_TBL at2 ;
select * from container_tbl;
insert into container_tbl (a_id, c_id, c_title)
VALUES (7, (SELECT count(c_id) FROM CONTAINER_TBL), 'dd');

---- account, container join
SELECT a.a_id, a_username, a_password, a_created, c_id, c_title 
	FROM ACCOUNT_TBL a LEFT JOIN CONTAINER_TBL ct 
	ON a.a_id = ct.a_id;

---- container, post join
SELECT ct.a_id, ct.c_id, ct.c_title, pt.p_id, pt.P_TITLE, pt.P_CONTENT, pt.P_CREATED, pt.P_UPDATED 
	FROM container_tbl ct LEFT JOIN post_tbl pt
	ON ct.c_id = pt.c_id;

---- account, container, post join
SELECT a.a_id, a.a_username, a.a_password, a.a_created, c.c_id, c.c_title, c.p_id, c.p_title, c.p_content, c.p_created, c.p_updated
	FROM ACCOUNT_TBL a LEFT JOIN (
	SELECT ct.a_id, ct.c_id, ct.c_title, pt.p_id, pt.P_TITLE, pt.P_CONTENT, pt.P_CREATED, pt.P_UPDATED 
		FROM container_tbl ct LEFT JOIN post_tbl pt
		ON ct.c_id = pt.c_id
	) c
	ON a.a_id = c.a_id;
--

----
SELECT a.a_id, a.a_created, c.c_id, c.c_title, c.p_id, c.p_title, c.p_content, c.p_created, c.p_updated
	FROM ACCOUNT_TBL a LEFT JOIN (
	SELECT ct.a_id, ct.c_id, ct.c_title, pt.p_id, pt.P_TITLE, pt.P_CONTENT, pt.P_CREATED, pt.P_UPDATED 
		FROM container_tbl ct LEFT JOIN post_tbl pt
		ON ct.c_id = pt.c_id
	) c
	ON a.a_id = c.a_id;
--
SELECT * FROM post_tbl where a_id = 7 AND c_id = 1;
-- test
INSERT INTO container_tbl (c_id, c_title) VALUES (c_id_seq.nextval, '아무거나');
INSERT INTO container_tbl (c_id, c_title) VALUES (c_id_seq.nextval, '두번째');
INSERT INTO container_tbl (c_id, c_title) VALUES (c_id_seq.nextval, '세번째');

INSERT INTO post_tbl (c_id, p_id, p_title, p_content, p_created, p_updated)
VALUES (1, (SELECT count(p_id) + 1 FROM post_tbl WHERE c_id = 1) , 'test1', '흠 잘모르겠네요', sysdate, sysdate); 

INSERT INTO post_tbl (c_id, p_id, p_title, p_content, p_created, p_updated)
VALUES (2, (SELECT count(p_id) + 1 FROM post_tbl WHERE c_id = 2) , 'test2', '흠 잘모르겠네요', sysdate, sysdate);

DELETE FROM container_tbl;

SELECT * FROM container_tbl;

SELECT USER FROM DUAL;

SELECT to_char(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') FROM dual;

UPDATE CONTAINER_TBL SET c_title = '바뀐 두번째' WHERE c_id = 2;

SELECT * FROM POST_TBL pt WHERE c_id = 2 AND p_id = 2;

UPDATE POST_TBL  SET p_title = '수정 수정', p_content = '된 컨텐츠입니다', p_updated = sysdate
    WHERE c_id = 2 and p_id = 2;
    
SELECT c_id_seq.currval FROM dual;

SELECT CONTAINER_TBL SET c_title = #{title}
                     WHERE c_id = ${id}